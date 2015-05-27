#!/usr/bin/env python3

# This code originated as a project for the COMP 520 class at McGill
# University in Winter 2015. Any subsequent COMP 520 student who is
# viewing this code must follow the course rules and report any viewing
# and/or use of the code.

import os
import re

DISCLAIMER = '''\
This code originated as a project for the COMP 520 class at McGill
University in Winter 2015. Any subsequent COMP 520 student who is
viewing this code must follow the course rules and report any viewing
and/or use of the code.'''

PY_OR_SHELL_DISCLAIMER = '\n'.join(('# ' + line) for line in DISCLAIMER.split('\n')) + '\n\n'
JAVA_OR_GO_DISCLAIMER = '/* ' + '\n * '.join(DISCLAIMER.split('\n')) + ' */\n\n'

SHEBANG_REGEX = re.compile(r'(^#!.*\n\n?)', re.MULTILINE)


def main(root):
    for (directory, subdirs, files) in os.walk(root):
        if os.path.split(directory)[1] == '.git':
            del subdirs[:]
            continue

        for fname in files:
            if fname.endswith('.py') or fname.endswith('.sh'):
                prepend_to_file(PY_OR_SHELL_DISCLAIMER, os.path.join(directory, fname))
            elif fname.endswith('.java') or fname.endswith('.go'):
                prepend_to_file(JAVA_OR_GO_DISCLAIMER, os.path.join(directory, fname))
            else:
                print('Ignoring file', os.path.join(os.path.relpath(directory, root), fname))


def prepend_to_file(message, filename):
    with open(filename, 'r') as f:
        contents = f.read()
        content_newlines = f.newlines

    shebang_match = SHEBANG_REGEX.match(contents)

    if shebang_match:
        shebang, contents = contents[:shebang_match.end()], contents[shebang_match.end():]
    else:
        shebang = None

    if contents.startswith(message):
        return

    with open(filename, 'w', newline=content_newlines or None) as f:
        if shebang:
            f.write(shebang)
        f.write(message)
        f.write(contents)


if __name__ == '__main__':
    main(os.getcwd())
