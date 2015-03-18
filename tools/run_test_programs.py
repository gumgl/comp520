#!/usr/bin/python3

import sys, os, logging, argparse
from subprocess import Popen, PIPE


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
    'weed',
    'type'
]

ALL_STAGES = len(STAGES)
UNDETECTED_STAGE = ALL_STAGES + 1

STAGE_ALIASES = {
    'scanner': STAGES.index('lexer'),
    'syntax': STAGES.index('parser'),
    'types': STAGES.index('type'),
    'weeding': STAGES.index('weed'),
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
        logger.setLevel(logging.VERBOSE)
    elif ns.verbose >= 2:
        logger.setLevel(logging.DEBUG)

    return ns.targets

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

    parser.add_argument('--no-warn', action='store_false', dest='warn', help='Suppress warnings')
    parser.add_argument('-v', '--verbose', action='count', default=0, help='Increase output verbosity')

    return parser

# --- Test runner ---

def main(targets):
    if len(targets) == 0:
        logger.debug('Got no arguments')
        return PROGRAM_ERROR

    runner = TestRunner()

    for target in targets:
        runner.test(target)

    runner.print_results()

    if runner.status == PROGRAM_ERROR:
        logger.warn('test runner encountered errors')

    return runner.status

class TestRunner:
    def __init__(self, cmd=None):
        self.status = TESTS_GOOD
        self.cmd = cmd or ('golite' if sys.platform == 'win32' else './golite')

        # Succeed, fail, error
        self.counts = [0, 0, 0]

    def print_results(self):
        print('Succeeded: {}, Failed: {}, Raised error: {}'.format(*self.counts))

    def update(self, status):
        self.counts[status] += 1

        if status > self.status:
            self.status = status

    def test(self, target):
        if os.path.isdir(target):
            for (directory, _, files) in os.walk(target):
                for f in files:
                    if not f.endswith('.go'):
                        continue

                    self.testfile(os.path.join(directory,f))
            return

        if os.path.isfile(target):
            self.testfile(target)
            return

        logger.error('%s is not a file or directory', target)
        self.status = PROGRAM_ERROR

    def testfile(self, target):
        dirs = list(all_directories(os.path.dirname(target)))

        expect_success = 'invalid' not in dirs
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
                self.update(TESTS_GOOD)
                return

            output_fail(filename, describe_for_stage('error', test_stage), 'the test passed all stages')
            self.update(TESTS_FAILED)
            return

        # Return code 1 means a controlled compiler error
        if returncode == 1:
            error_stage = parse_stage(err_msg)

            if error_stage is None:
                if expect_success:
                    expected = 'to pass ' + describe_for_stage('stage', test_stage, 'all stages')
                else:
                    expected = describe_for_stage('error', test_stage)
                output_fail(filename, expected, 'got error and could not identify type', err_msg)
                self.update(TESTS_FAILED)
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
                    self.update(TESTS_GOOD)
                    return

                output_fail(filename, 'test to pass', 'got '+error_description, err_msg)
                self.update(TESTS_FAILED)
                return

            if expect_success:
                if error_stage < test_stage:
                    output_fail(filename, describe_for_stage('stage', test_stage, 'all stages') + ' to pass', 'got '+error_description, err_msg)
                    self.update(TESTS_FAILED)
                    return

                self.update(TESTS_GOOD)
                return

            if error_stage != test_stage:
                output_fail(filename, describe_for_stage('error', test_stage), 'got '+error_description, err_msg)
                self.update(TESTS_FAILED)
                return

            self.update(TESTS_GOOD)
            return

        # Any other return code means an internal error
        expected = describe_for_stage('to pass' if expect_success else 'error', test_stage)
        output_fail(filename, expected, 'got internal error', err_msg)

        self.update(TEST_ERROR)

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
    if 0 < stage < len(STAGES):
        return STAGES[stage] + ' ' + term
    if fallback:
        return fallback
    return term

def output_fail(filename, expected, actual_result, err_msg=None):
    fail_msg = [filename, '   Expected ' + expected + ' but ' + actual_result]

    if err_msg:
        err_msg = err_msg.strip()
        if err_msg:
            fail_msg.extend(('   > '+line) for line in err_msg.split('\n'))

    logger.log(LOG_TEST_FAILURE, '\n'.join(fail_msg))

def all_directories(path):
    while path:
        path, base = os.path.split(path)
        yield base

if __name__ == '__main__':
    try:
        targets = initialize_config(sys.argv[1:])
        sys.exit(main(targets))
    except KeyboardInterrupt as e:
        logger.debug('Execution interrupted', exc_info=e)
        sys.exit(USER_INTERRUPT)
