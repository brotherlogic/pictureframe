git fetch -p
git merge origin/master
git gc
rm pictureframe.jar
rsync -e 'ssh -p 2223' -avz --progress $1:codestore/pictureframe/ ./codestore
ln -s $(find codestore/ -printf "%T+\t%p\n" | sort | grep with-dependencies | awk '{ print $2 }' | grep .jar$ | tail -n 1) ./pictureframe.jar
