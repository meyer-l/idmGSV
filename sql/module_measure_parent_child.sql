DROP TABLE IF EXISTS module_measure_parent_childs;
CREATE TABLE module_measure_parent_childs(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      parent_id int(11),
      child_id int(11),
      additional int(11),
      category VARCHAR(56)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
