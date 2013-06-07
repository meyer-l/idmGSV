DROP TABLE IF EXISTS change_events;
CREATE TABLE change_events (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      created_at timestamp,
      domain_id int(11),
      change_item_id int(11),
      change_type int(11),
      process_type varchar(56),
      applied boolean
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
