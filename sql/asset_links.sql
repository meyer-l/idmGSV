DROP TABLE IF EXISTS asset_links;
CREATE TABLE asset_links (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      domain_id int(11),
      parent_id int(11),
      linked_asset_id int(11)
  ) DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
