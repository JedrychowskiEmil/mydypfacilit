USE `diploma_process_facilitator`;

-- Default passwords here are: fun123
INSERT INTO `user` (email,password,academic_title,first_name,last_name)
VALUES 
('a@a','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'Admin',''),
('p0@p','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','prof','p0','Profesor bez studentow, i departmentow'),
('p1@p','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','prof','p1','Profesor z jednym departmentem bez studentow'),
('p2@p','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','prof','p2','Profesor z wieloma departmentami bez studentow'),
('p3@p','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','prof','p3','Profesor z wieloma departmentami ze studentami'),
('s0@s','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'s0','Bez departmentu'),
('s1@s','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'s1','z departmentem bez tematu'),
('s2@s','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'s2','Z dep z zapropnowanym tematem'),
('s3@s','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'s3','z promotorem'),
('s4@s','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'s4','z gotowa praca');

INSERT INTO `department` (department,field,study_mode,study_group)
VALUES 
('Wydział Architektury Budownictwa i Sztuk Stosowanych', 'Informatyka','Stacjonarnie','I'),
('Wydział Architektury Budownictwa i Sztuk Stosowanych', 'Informatyka','Stacjonarnie','II'),
('Wydział Architektury Budownictwa i Sztuk Stosowanych','Informatyka','Zaocznie','I'),
('Wydział Architektury Budownictwa i Sztuk Stosowanych','Informatyka','Zaocznie','II'),
('Wydział Architektury Budownictwa i Sztuk Stosowanych', 'Mechatronika','Stacjonarnie','Grupa I'),
('Wydział Architektury Budownictwa i Sztuk Stosowanych','Architektura','Zaocznie','GrupaI'),
('Wydział Architektury Budownictwa i Sztuk Stosowanych', 'Grafika','Stacjonarnie','1'),
('Wydział Nauk Medycznych','Pielegniarstwo','Stacjonarnie','III');

INSERT INTO `department_user` (user_id,department_id)
VALUES 
(3, 1),
(4, 1),
(4, 2),
(4, 3),
(4, 4),

(5, 1),
(5, 2),
(5, 3),
(5, 4),

(7, 1),
(8, 2),
(9, 3),
(10, 4);



INSERT INTO `diploma_process_facilitator`.`status` (`name`) VALUES ('Nie wybrano tematu pracy');
INSERT INTO `diploma_process_facilitator`.`status` (`name`) VALUES ('Brak promotora');
INSERT INTO `diploma_process_facilitator`.`status` (`name`) VALUES ('Temat odrzucono');
INSERT INTO `diploma_process_facilitator`.`status` (`name`) VALUES ('Zaproponowano temat');
INSERT INTO `diploma_process_facilitator`.`status` (`name`) VALUES ('Temat promotora');
INSERT INTO `diploma_process_facilitator`.`status` (`name`) VALUES ('W trakcie pisania pracy');
INSERT INTO `diploma_process_facilitator`.`status` (`name`) VALUES ('Wymaga poprawy');
INSERT INTO `diploma_process_facilitator`.`status` (`name`) VALUES ('Praca zatwierdzona przez promotora');
INSERT INTO `diploma_process_facilitator`.`status` (`name`) VALUES ('Praca przyjeta i zatwierdzona');

INSERT INTO `diploma_topic` (student_id,promoter_id,subject,description,status,department_id)
VALUES
(null,4,'Zaproponowany temat 1','Zaproponowano opis',1,2),
(null,4,'Zaproponowany temat 2','Zaproponowano opis',1,2),
(null,4,'Zaproponowany temat 3','Zaproponowano opis',1,2),
(null,4,'Zaproponowany temat 4','Zaproponowano opis',1,3),
(null,4,'Zaproponowany temat 5','Zaproponowano opis',1,3),
(null,4,'Zaproponowany temat 6','Zaproponowano opis',1,4),
(8,5,'Zaproponowano','Zaproponowano opis',4,2),
(9,5,'Ma promotora','opis pracy',5,3),
(10,5,'gotowa praca','opis gotowej pracy',9,4);

INSERT INTO `role` (name)
VALUES 
('ROLE_STUDENT'),('ROLE_PROMOTER'),('ROLE_ADMIN');

INSERT INTO `user_role` (user_id,role_id)
VALUES 
(1, 3),
(2, 2),
(3, 2),
(4, 2),
(5, 2),
(6, 1),
(7, 1),
(8, 1),
(9, 1),
(10, 1);