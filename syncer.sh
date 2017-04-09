cat /home/simon/.xsession
git fetch -p
git merge origin/master
git gc
sudo cat /var/spool/cron/crontabs/simon
sudo sed -i 's/67.174.252.40/www.brotherlogic.com/' /var/spool/cron/crontabs/simon
sudo cat /var/spool/cron/crontabs/simon
rm pictureframe.jar
rsync -e 'ssh -p 2223' -avz --progress $(curl -s "$1"/resolve | awk -F ':' '{print $1}'):codestore/pictureframe/ ./codestore
ln -s $(find codestore/ -printf "%T+\t%p\n" | sort | grep with-dependencies | awk '{ print $2 }' | grep .jar$ | tail -n 1) ./pictureframe.jar
