rm pictureframe.jar
rsync -avz --progress 10.0.1.17:codestore/pictureframe/ ./
ln -s $(find codestore/ | grep jar-with-dependencies.jar$ | sort -n | tail -n 1) ./pictureframe.jar
