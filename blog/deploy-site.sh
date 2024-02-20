#!/bin/bash
#
# A bit basic but it'll do!
echo '>>> Building site ...'
if jekyll build
then
    echo '>>> Syncing files ...'
    # The HTML files are now served by the clojure server, could rejigger so
    # it's all served through apache but that makes local dev fiddlier :shrug:
    rsync -av _site miro:dev/broquaint/blog/
    rsync -av assets/img miro:dev/broquaint/resources/public/assets/
    ( cd .. ; rsync -av resources/public/js resources/public/css miro:dev/broquaint/resources/public/ )
fi
