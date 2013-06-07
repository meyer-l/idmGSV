DROP TABLE IF EXISTS assettypecategories;
CREATE TABLE assettypecategories (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      name VARCHAR(255),
      icon_name VARCHAR(255)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
