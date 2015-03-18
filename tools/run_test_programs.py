#!/usr/bin/python3

import sys, os, logging, argparse
from subprocess import Popen, PIPE, DEVNULL


# --- Logging configuration ---
logging.basicConfig(format='%(levelname)s: %(message)s\n')

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
    'syntax': STAGES.index('parser'),
    'types': STAGES.index('type'),
    'semantic': ALL_STAGES
}

# --- Command-line interface ---

def get_cli_input(args):
    ns = get_cli_parser().parse_args(args)
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
    return parser

# --- Test runner ---

def main(targets):
    if len(targets) == 0:
        logging.debug('Got no arguments')
        return PROGRAM_ERROR

    status = TESTS_GOOD
    for target in targets:
        status = max(status, test(target))

    return status

def test(target):
    if os.path.isdir(target):
        status = TESTS_GOOD
        for (directory, _, files) in os.walk(target):
            status = max(status, 0,
                *(testfile(os.path.join(directory,f))
                for f in files
                if f.endswith('.go')))

        return status

    if os.path.isfile(target):
        return testfile(target)

    logging.error('%s is not a file or directory', target)
    return PROGRAM_ERROR

def testfile(target):
    dirs = list(all_directories(os.path.dirname(target)))

    expect_success = 'invalid' not in dirs
    test_stage = autodetect_stage(dirs)

    # TODO: these shouldn't need to run sequentially
    process = Popen(['golite', target], shell=True, stdout=DEVNULL, stderr=PIPE, universal_newlines=True)
    process.wait()

    returncode = process.returncode
    err_msg = process.stderr.read()

    return evaluate_test(target, expect_success, test_stage, returncode, err_msg)

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


def evaluate_test(filename, expect_success, test_stage, returncode, err_msg):
    if returncode == 0:
        if expect_success:
            return TESTS_GOOD

        output_fail(filename, describe_for_stage('error', test_stage), 'the test passed all stages')
        return TESTS_FAILED

    # Return code 1 means a controlled compiler error
    if returncode == 1:
        if test_stage == UNDETECTED_STAGE:
            logging.warn('cannot validate error for %s because the expected error type was not detected', filename)
            return TESTS_GOOD

        error_stage = parse_stage(err_msg)

        if error_stage is None:
            if expect_success:
                expected = 'to pass ' + describe_for_stage('stage', test_stage, 'all stages')
            else:
                expected = describe_for_stage('error', test_stage)
            output_fail(filename, expected, 'got error and could not identify type', err_msg)
            return PROGRAM_ERROR

        error_description = describe_for_stage('error', error_stage, 'unidentified')

        if expect_success:
            if error_stage < test_stage:
                output_fail(filename, describe_for_stage('stage', test_stage, 'all stages') + ' to pass', 'got '+error_description, err_msg)
                return TESTS_FAILED

            return TESTS_GOOD

        if error_stage != test_stage:
            output_fail(filename, describe_for_stage('error', test_stage), 'got '+error_description, err_msg)
            return TESTS_FAILED

        return TESTS_GOOD

    # Any other return code means an internal error
    expected = describe_for_stage('to pass' if expect_success else 'error', test_stage)
    output_fail(filename, expected, 'internal error', err_msg)

    return TEST_ERROR

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
    fail_msg = [filename, '\nExpected ', expected, ' but ', actual_result, '\n']

    if err_msg:
        err_msg = err_msg.strip()
        if err_msg:
            fail_msg.append('\n'.join(('>  '+line) for line in err_msg.split('\n')))

    logging.log(LOG_TEST_FAILURE, ''.join(fail_msg))

def all_directories(path):
    while path:
        path, base = os.path.split(path)
        yield base

if __name__ == '__main__':
    try:
        targets = get_cli_input(sys.argv[1:])
        sys.exit(main(targets))
    except KeyboardInterrupt as e:
        logging.debug('Execution interrupted', exc_info=e)
        sys.exit(USER_INTERRUPT)
