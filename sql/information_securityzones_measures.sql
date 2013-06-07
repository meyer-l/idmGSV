DROP TABLE IF EXISTS information_securityzones_measures;
CREATE TABLE information_securityzones_measures(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      information_id int(11),      
      securityzones_measures_id int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
