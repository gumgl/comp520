#!/usr/bin/env python3

import sys, os, difflib

def main(targets):
    for target in targets:
        if os.path.isfile(target):
            diff_file(target)
        else:
            diff_dir(target)

def diff_file(target):
    tofile = target

    base = os.path.relpath(tofile, 'peepholebenchmarks')
    fromfile = os.path.join('unopt', base)

    with open(fromfile) as fromf, open(tofile) as tof:
        fromlines, tolines = list(fromf), list(tof)

    diff = difflib.ndiff(fromlines, tolines)

    print(tofile)
    print(*diff, sep='')

    print('That was', tofile)
    resp = input('Okay? [Y/n] ').strip()
    if resp and resp.lower() != 'y':
        print('Well fine then')
        return False

    return True

def diff_dir(target):
    for (path, dirs, files) in os.walk(target):
        for f in files:
            if not f.endswith('.j'):
                continue

            if not diff_file(os.path.join(path, f)):
                return

if __name__ == '__main__':
    targets = [os.path.join('peepholebenchmarks', arg) for arg in sys.argv[1:]]

    if not targets:
        targets.append('peepholebenchmarks')

    main(targets)
