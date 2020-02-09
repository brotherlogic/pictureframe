import os
import sys

verbose = True

if verbose:
    print "Running in verbose mode"

lines_before = len(os.popen('find ./codestore').readlines())
if verbose:
    print "\n".join(os.popen('./syncer.sh ' + sys.argv[1]).readlines())
else:
    os.popen('./syncer.sh ' + sys.argv[1]).readlines()
lines_after = len(os.popen('find ./codestore').readlines())

if lines_before != lines_after:
    os.popen('sudo reboot').readlines()
