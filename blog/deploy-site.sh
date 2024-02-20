#!/bin/bash
#
# A bit basic but it'll do!
echo '>>> Building blog ...'
if jekyll build
then
    echo '>>> Syncing blog files ...'
    # The HTML files are now served by the clojure server, could rejigger so
    # it's all served through apache but that makes local dev fiddlier :shrug:
    rsync -av _site miro:dev/broquaint/blog/
    echo '>>> Syncing image files ...'
    rsync -av assets/img miro:dev/broquaint/resources/public/assets/
else
    echo '!!! Blog build failed, aborting'
    exit 1
fi
