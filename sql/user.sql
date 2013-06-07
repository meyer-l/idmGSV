DROP TABLE IF EXISTS users;
CREATE TABLE users (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      username VARCHAR(255),
      hash text,
      cookie_id VARCHAR(255),
      cookie_expires timestamp,
      session_id VARCHAR(255),
      session_expires timestamp,
       remember_me boolean
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
