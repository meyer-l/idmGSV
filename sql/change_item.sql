DROP TABLE IF EXISTS change_items;
CREATE TABLE change_items (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      created_at timestamp,
      domain_id int(11),
      employee_id int(11),
      parsed_employee_name varchar(64),
      old_assets_ids text,
      new_assets_ids text,
      missing_assets_names text,
      old_occupations_ids text,
      new_occupations_ids text,
      missing_occupations_names text,
      old_telefon varchar(64),
      new_telefon varchar(64)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
