DROP TABLE IF EXISTS measures;
CREATE TABLE measures (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      category VARCHAR(255),
      title VARCHAR(255),
      version VARCHAR(255),
      initiator VARCHAR(255),
      implementation VARCHAR(255),
      description text
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
