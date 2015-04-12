#!/usr/bin/env python3

import sys, os, difflib, argparse

def main(targets, summary, batch):
    comp = Comparer(not summary, not (batch or summary))

    for target in targets:
        if os.path.isfile(target):
            comp.diff_file(target)
        else:
            comp.diff_dir(target)

    comp.print_report()

class Comparer:
    def __init__(self, verbose=True, prompt=True):
        self.reports = []
        self.verbose = verbose
        self.prompt = prompt

    def diff_file(self, target):
        tofile = target

        base = os.path.relpath(tofile, 'peepholebenchmarks')
        fromfile = os.path.join('unopt', base)

        with open(fromfile) as fromf, open(tofile) as tof:
            fromlines, tolines = list(fromf), list(tof)

        if self.verbose:
            print(tofile)

        added, removed = 0, 0
        for line in difflib.ndiff(fromlines, tolines):
            if line.startswith('+'):
                added += 1
            if line.startswith('-'):
                removed += 1
            if self.verbose:
                print(line, end='')

        self.reports.append((tofile, added, removed))

        if self.prompt:
            print('That was', tofile)
            resp = input('Okay? [Y/n] ').strip()
            if resp and resp.lower() != 'y':
                print('Well fine then')
                return False

        return True

    def diff_dir(self, target):
        for (path, dirs, files) in os.walk(target):
            for f in files:
                if not f.endswith('.j'):
                    continue

                if not self.diff_file(os.path.join(path, f)):
                    return

    def print_report(self):
        fmt = '{:<' + str(max(len(r[0]) for r in self.reports)) + '}  {}'

        print(fmt.format('FILE', 'IMPROVEMENT'))

        added_overall, removed_overall = 0, 0
        for filename, added, removed in self.reports:
            added_overall += added
            removed_overall += removed

            print(fmt.format(filename, removed - added))

        print(fmt.format('OVERALL:', removed_overall - added_overall))

if __name__ == '__main__':
    cli = argparse.ArgumentParser(description='Report on optimizations')
    cli.add_argument('files', nargs='*', help='Specify files and directories to report on')
    cli.add_argument('-s', '--summary', action='store_true', help='Only print a summary')
    cli.add_argument('-b', '--batch', action='store_true', help="Don't prompt after each file")

    args = cli.parse_args()

    targets = [os.path.join('peepholebenchmarks', f) for f in args.files]

    if not targets:
        targets.append('peepholebenchmarks')

    main(targets, args.summary, args.batch)
