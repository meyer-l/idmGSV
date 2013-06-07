DROP TABLE IF EXISTS assets;
CREATE TABLE assets (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      assettype_id int(11),
      parent_id int(11),
      domain_id int(11),
      responsible_person_id int(11),
      identifier VARCHAR(255),
      status VARCHAR(255)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
