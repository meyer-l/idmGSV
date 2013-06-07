DROP TABLE IF EXISTS modules;
CREATE TABLE modules (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      category VARCHAR(255),
      title VARCHAR(255),
      version VARCHAR(255),
      module_description text,
      threat_description text,
      measure_description text
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
