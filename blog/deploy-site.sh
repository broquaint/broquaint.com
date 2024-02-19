#!/bin/bash
#
# A bit basic but it'll do!
echo '>>> Building site ...'
if jekyll build
then
    echo '>>> Syncing files ...'
    # rsync -av _site/index.html _site/blog _site/assets miro:dev/broquaint/resources/public/
    rsync -av _site/ miro:dev/broquaint/blog/
fi
