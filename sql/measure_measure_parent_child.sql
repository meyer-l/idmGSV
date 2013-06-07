DROP TABLE IF EXISTS measure_measure_parent_childs;
CREATE TABLE measure_measure_parent_childs (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      parent_id int(11),
      child_id int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
