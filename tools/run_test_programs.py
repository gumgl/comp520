#!/usr/bin/python3

import sys, os, logging, argparse, cmd, posixpath
from subprocess import Popen, PIPE, check_call
from collections import OrderedDict

# --- Logging configuration ---
logger = logging.getLogger(__file__)

# Log test failures at a level above warnings but below errors
LOG_TEST_FAILURE = (logging.WARNING + logging.ERROR) // 2
logging.addLevelName(LOG_TEST_FAILURE, 'TEST FAILED')

# --- Constants ---

# Return codes
TESTS_GOOD = 0
TESTS_FAILED = 1
TEST_ERROR = 2
PROGRAM_ERROR = 3
USER_INTERRUPT = 4

# Compiler stages
STAGES = [
    'lexer',
    'parser',
    'weeding',
    'type'
]

ALL_STAGES = len(STAGES)
UNDETECTED_STAGE = ALL_STAGES + 1

STAGE_ALIASES = {
    'scanner': STAGES.index('lexer'),
    'syntax': STAGES.index('parser'),
    'types': STAGES.index('type'),
    'semantic': ALL_STAGES
}

# --- Command-line interface ---

def initialize_config(args):
    ns = get_cli_parser().parse_args(args)

    handler = logging.StreamHandler(sys.stderr)
    handler.setFormatter(logging.Formatter('%(levelname)s: %(message)s'))
    logger.addHandler(handler)

    if not ns.warn:
        class WarningFilter(logging.Filter):
            def filter(self, record):
                return record.levelno != logging.WARNING

        handler.addFilter(WarningFilter())

    if ns.verbose == 1:
        logger.setLevel(logging.INFO)
    elif ns.verbose >= 2:
        logger.setLevel(logging.DEBUG)

    return ns.targets, ns.exclude, ns.interactive

def get_cli_parser():
    parser = argparse.ArgumentParser(description='Run test programs and validate errors')
    parser.add_argument('targets', nargs='+',
        help='''\
Files or directories containing test programs. If target is
a directory, each file with a .go extension in it and its subdirectories
will be tested.

The expected result (whether it compiles successfully and what
error it gives) is determined automagically from the path.
''')

    parser.add_argument('-i', '--interactive', action='store_true', help='Run an interactive prompt for failing cases')
    parser.add_argument('--no-warn', action='store_false', dest='warn', help='Suppress warnings')
    parser.add_argument('-v', '--verbose', action='count', default=0, help='Increase output verbosity')
    parser.add_argument('-x', '--exclude', action='append', help='Exclude files found on this path')

    return parser

# --- Test runner ---

def main(targets, exclude, interactive=False):
    if len(targets) == 0:
        logger.debug('Got no arguments')
        return PROGRAM_ERROR

    if interactive:
        runner = InteractiveTestRunner(exclude)
    else:
        runner = TestRunner(exclude)

    for target in targets:
        runner.test(target)

    runner.print_results()

    if runner.status == PROGRAM_ERROR:
        logger.warn('test runner encountered errors')

    return runner.status

class TestRunner:
    def __init__(self, exclude=None, cmd=None):
        if exclude is None:
            self.exclude = []
        else:
            self.exclude = [os.path.normpath(x) for x in exclude]
        self.status = TESTS_GOOD
        self.cmd = cmd or ('golite' if sys.platform == 'win32' else './golite')

        # Succeed, fail, error
        self.counts = [0, 0, 0]

        self.config = {}

        self.queue = []

    def print_results(self):
        print('Succeeded: {}, Failed: {}, Raised error: {}'.format(*self.counts))

    def succeed(self, filename, msg):
        self._update(TESTS_GOOD)
        logger.info('%s\n   Test passed: %s', filename, msg)

    def fail(self, filename, expected, actual_result, err_msg=None, status=TESTS_FAILED):
        fail_msg = [filename, '   Expected ' + expected + ' but ' + actual_result]

        if err_msg:
            err_msg = err_msg.strip()
            if err_msg:
                fail_msg.extend(('   > '+line) for line in err_msg.split('\n'))

        logger.log(LOG_TEST_FAILURE if status == TESTS_FAILED else logging.ERROR, '\n'.join(fail_msg))
        self._update(status)

    def _update(self, status):
        self.counts[status] += 1

        if status > self.status:
            self.status = status

    def test(self, target):
        if os.path.isdir(target):
            self.enqueue_dir(target)

        elif os.path.isfile(target):
            self.load_dir_config(os.path.dirname(target))
            self.queue.append(target)

        else:
            logger.error('%s is not a file or directory', target)
            self.status = PROGRAM_ERROR
            return

        while self.queue:
            self.testfile(self.queue.pop(0))

    def enqueue_dir(self, target):
        for (directory, subdirs, files) in os.walk(target, topdown=True):
            directory = os.path.normpath(directory)

            if directory in self.exclude:
                logger.info('skipping directory %s', directory)
                # Jump over subdirectories
                del subdirs[:]
                continue

            self.load_dir_config(directory)

            for f in files:
                if not f.endswith('.go'):
                    continue

                full_path = os.path.join(directory, f)
                if full_path in self.exclude:
                    logger.info('skipping file %s', full_path)
                    continue

                self.queue.append(full_path)

    def load_dir_config(self, directory, accept_dups=False):
        config_path = os.path.join(directory, 'test_configuration.txt')

        if not os.path.exists(config_path):
            return

        try:
            with open(config_path) as f:
                mapping = ConfigFile.load(f).get_map()

        except IOError as e:
            logger.error('Failed to read configuration at %s', config_path, exc_info=e)
            return

        for (key, stage) in mapping.items():
            key = os.path.normpath(os.path.join(directory, key))
            if key in self.config and self.config[key] != stage and not accept_dups:
                logger.error('test runner already has configuration for %s', key)
                continue
            self.config[key] = stage

    def testfile(self, target):
        target = os.path.normpath(target)
        dirs = list(all_directories(os.path.dirname(target)))

        expect_success = 'invalid' not in dirs

        if target in self.config:
            test_stage = self.config[target]
        else:
            test_stage = autodetect_stage(dirs)

        # TODO: these shouldn't need to run sequentially
        args = [self.cmd, target]
        if sys.platform != 'win32':
            args = ' '.join(args)
        process = Popen(args, shell=True, stdout=PIPE, stderr=PIPE, universal_newlines=True)
        process.wait()

        returncode = process.returncode
        err_msg = process.stderr.read()

        return self.evaluate_test(target, expect_success, test_stage, returncode, err_msg)

    def evaluate_test(self, filename, expect_success, test_stage, returncode, err_msg):
        if returncode == 0:
            if expect_success:
                self.succeed(filename, 'no errors')
                return

            self.fail(filename, describe_for_stage('error', test_stage), 'the test passed all stages')
            return

        # Return code 1 means a controlled compiler error
        if returncode == 1:
            error_stage = parse_stage(err_msg)

            if error_stage is None:
                if expect_success:
                    expected = 'to pass ' + describe_for_stage('stage', test_stage, 'all stages')
                else:
                    expected = describe_for_stage('error', test_stage)

                self.fail(filename, expected, 'got error and could not identify type', err_msg)
                self.status = PROGRAM_ERROR
                return

            error_description = describe_for_stage('error', error_stage, 'unidentified error')

            # If the stage the test case targets could not be detected, give a
            # warning but succeed if the test case was expected to fail. If it
            # was expected to succeed, give a warning and also fail.
            #
            # Give a warning for either UNDETECTED_STAGE or ALL_STAGES if a
            # failure was expected, since for failures they basically mean the
            # same thing
            if (expect_success and test_stage == UNDETECTED_STAGE) or (not expect_success and test_stage >= ALL_STAGES):
                logger.warn('cannot validate error raised by %s\n'
                    '   The expected error type was not detected', filename)

                if not expect_success:
                    self.succeed(filename, error_description)
                    return

                self.fail(filename, 'test to pass', 'got '+error_description, err_msg)
                return

            if expect_success:
                if error_stage < test_stage:
                    self.fail(filename, describe_for_stage('stage', test_stage, 'all stages') + ' to pass', 'got '+error_description, err_msg)
                    return

                self.succeed(filename, error_description)
                return

            if error_stage != test_stage:
                self.fail(filename, describe_for_stage('error', test_stage), 'got '+error_description, err_msg)
                return

            self.succeed(filename, error_description)
            return

        # Any other return code means an internal error
        expected = describe_for_stage('to pass' if expect_success else 'error', test_stage)
        self.fail(filename, expected, 'got internal error', err_msg, status=TEST_ERROR)

class InteractiveTestRunner (TestRunner):
    def fail(self, filename, *args, **kwargs):
        super().fail(filename, *args, **kwargs)

        print()
        InteractiveCmd(self, filename).cmdloop()

class InteractiveCmd (cmd.Cmd):
    intro = 'Response?\n   c: continue\t\te: edit\t\tq: quit\n   s: set expected\tr: rerun\t?: help'
    prompt = '-> '

    def __init__(self, runner, filename):
        super().__init__()

        self.runner = runner
        self.filename = filename

        self.cmds = [c[3:] for c in dir(self) if c.startswith('do_')]

    def precmd(self, line):
        words = line.split()

        if not (words and words[0] not in self.cmds):
            return line

        avail = [c for c in self.cmds if c.startswith(words[0])]

        if len(avail) == 0:
            return line

        if len(avail) > 1:
            print('   Multiple possibilities:', '\t'.join(avail))
            return ''

        words[0] = avail[0]
        return ' '.join(words)

    def do_continue(self, arg):
        """Run next test
        """
        return True

    def do_edit(self, arg):
        """Open the test program in a shell editor
        """
        posix_name = self.filename
        if os.path != posixpath:
            posix_name = posixpath.join(*posix_name.split(os.path.sep))

        check_call('vim "'+posix_name+'"')

    def do_rerun(self, arg):
        """Run this test again
        """
        self.runner.queue.insert(0, self.filename)
        return True

    def do_quit(self, arg):
        """Stop the test running now
        """
        logger.info('Exiting early. %s test cases remaining.', len(self.runner.queue))
        del self.runner.queue[:]
        return True

    def do_set_expected(self, arg):
        """Change the expected outcome of a test case

        For example:
           -> set_expected parser
           -> s weeding

        Note that setting the validity of a file is not supported
        """

        stage = autodetect_stage([arg])
        if stage == UNDETECTED_STAGE:
            print('unknown stage:', arg)
            return

        dirname, basename = os.path.split(self.filename)

        config_path = os.path.join(dirname, 'test_configuration.txt')

        if os.path.exists(config_path):
            with open(config_path, 'r') as f:
                config = ConfigFile.load(f)
        else:
            config = ConfigFile()

        config.get_map()[basename] = stage

        with open(config_path, 'w') as f:
            config.write(f)

        logger.debug('Added configuration: %s %s', basename, STAGES[stage])

        self.runner.load_dir_config(dirname, accept_dups=True)

class ConfigFile:
    def __init__(self):
        self._mapping = OrderedDict()

    @staticmethod
    def load(file_obj):
        config = ConfigFile()

        for line in file_obj:
            if not line:
                continue

            components = line.split()
            if len(components) != 2:
                logger.error('Could not interpret config line: %s', line)
                continue

            stage = autodetect_stage([components[1]])
            if stage == UNDETECTED_STAGE:
                logger.error('Unkown configuration stage: %s', components[1])
                continue

            config._mapping[components[0]] = stage

        return config

    def get_map(self):
        return self._mapping

    def write(self, file_obj):
        for (key, stage) in self._mapping.items():
            file_obj.write('{} {}\n'.format(key, STAGES[stage]))

def autodetect_stage(dirs):
    stage = None

    for directory in dirs:
        try:
            found_stage = STAGE_ALIASES[directory]
        except KeyError:
            try:
                found_stage = STAGES.index(directory)
            except ValueError:
                continue

        if stage is not None and stage != found_stage:
            raise ValueError('automagic test kind detection failed\n'
                "    [honestly this shouldn't throw an error though]")

        stage = found_stage

    if stage is None:
        return UNDETECTED_STAGE

    return stage

def parse_stage(error):
    error = error.lower()
    for (i, stage_name) in enumerate(STAGES):
        if error.startswith(stage_name):
            return i
    return None

def describe_for_stage(term, stage, fallback=None):
    if 0 <= stage < len(STAGES):
        return STAGES[stage] + ' ' + term
    if fallback:
        return fallback
    return term

def all_directories(path):
    while path:
        path, base = os.path.split(path)
        yield base

if __name__ == '__main__':
    try:
        targets, exclude, interactive = initialize_config(sys.argv[1:])
        sys.exit(main(targets, exclude, interactive))
    except KeyboardInterrupt as e:
        logger.debug('Execution interrupted', exc_info=e)
        sys.exit(USER_INTERRUPT)
