rm pictureframe.jar
rsync -avz --progress 10.0.1.17:codestore/pictureframe/ ./codestore
ln -s $(find codestore/ -printf "%T+\t%p\n" | sort | grep with-dependencies | awk '{ print $2 }' | tail -n 1) ./pictureframe.jar
