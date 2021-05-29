git reflog expire --all --stale-fix
cat ~/xs.txt
df -h
ls -ltr ~
if ping -c 1 www.github.com > /dev/null 2>&1 ; then
    git fetch -p
    git merge origin/master
    git gc
    rm pictureframe.jar
    git config --global user.email "brotherlogic@gmail.com"
    git config --global user.name "Simon Tucker"
    #ssh-keygen -f "/home/simon/.ssh/known_hosts" -R [73.162.90.182]:2223
    rsync -e 'ssh -p 2223 -o StrictHostKeyChecking=no' -avz --delete-after --progress 98.37.147.121:codestore/pictureframe/ ./codestore
    #ln -s $(find codestore/ -printf "%T+\t%p\n" | sort | grep with-dependencies | awk '{ print $2 }' | grep .jar$ | tail -n 1) ./pictureframe.jar
    ln -s codestore/$(ls codestore/ | sort -n | grep .jar$ | tail -n 1) ./pictureframe.jar
fi
