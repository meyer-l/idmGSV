if [ $# == 3 ]
  then
  	mysql -u $1 --password=$2 -e "CREATE DATABASE IF NOT EXISTS $3 ";
    for file in sql/*.sql
      do mysql -u $1 --password=$2 $3 < $file
    done
  else 
  	echo "Usage: migrate.sh username password database"
fi

