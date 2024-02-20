#!/bin/bash

# set -x # is a loud but handy in a pinch
set -eu -o pipefail

start_time=`date +%s`

if [ $# -eq 1 ] && [ "$1" = "full" ]
then
    echo '>>> Building uberjar ...'
    if lein uberjar
    then
        build_file="$(ls -rt target | grep standalone)"
        scp "target/$build_file" miro:dev/broquaint/target
    else
        echo '!!! Build failed, aborting.'
        exit 1
    fi
    sleep 120
fi

cd blog
if bash deploy-site.sh
then
    cd ..
    echo '>>> Syncing JS/CSS files ...'
    rsync -av resources/public/js resources/public/css miro:dev/broquaint/resources/public/
    echo -n '<<< All done, '
    perl -E 'my($then, $now) = @ARGV; my $d=$now-$then; say sprintf qq[only took%s %d seconds.], ($d > 60 ? " ${\int($d/60)} minutes and" : ""), $d%60' "$start_time" `date +%s`
else
    echo '!!! Blog build failed, aborting.'
    exit 1
fi
