cat /home/simon/.xsession
sed -i 's/--host 67.174.252.40 --port 50055/--server www.brotherlogic.com/' /home/simon/.xsession
cat /home/simon/.xsession
git fetch -p
cat ~/.xsession
git merge origin/master
git gc
rm pictureframe.jar
rsync -e 'ssh -p 2223 -o StrictHostKeyChecking=no' -avz --delete-after --progress $(curl -s "$1"/resolve | awk -F ':' '{print $1}'):codestore/pictureframe/ ./codestore
ln -s $(find codestore/ -printf "%T+\t%p\n" | sort | grep with-dependencies | awk '{ print $2 }' | grep .jar$ | tail -n 1) ./pictureframe.jar
