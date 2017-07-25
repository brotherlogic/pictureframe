if ping -c 1 www.github.com > /dev/null 2>&1 ; then
    git fetch -p
    git merge origin/master
    git gc
    rm pictureframe.jar
    rsync -e 'ssh -p 2223 -o StrictHostKeyChecking=no' -avz --delete-after --progress $(curl -s "$1"/resolve | awk -F ':' '{print $1}'):codestore/pictureframe/ ./codestore
    ln -s $(find codestore/ -printf "%T+\t%p\n" | sort | grep with-dependencies | awk '{ print $2 }' | grep .jar$ | tail -n 1) ./pictureframe.jar
fi
