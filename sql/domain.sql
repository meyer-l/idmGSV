DROP TABLE IF EXISTS domains;
CREATE TABLE domains (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      name VARCHAR(56),
      ident VARCHAR(56)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
