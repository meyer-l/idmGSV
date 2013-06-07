DROP TABLE IF EXISTS information_occupations;
CREATE TABLE information_occupations(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      information_id int(11),
      occupation_id int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
