insert into classroom(id, building_name, building_code, floor, door_code)
values (1, 'Main Building', 'MB', 1, 'A1'),
       (2, 'Main Building', 'MB', 1, 'A2'),
       (3, 'Main Building', 'MB', 1, 'A3'),
       (4, 'Main Building', 'MB', 1, 'A4'),
       (5, 'Main Building', 'MB', 1, 'A5'),
       (6, 'Main Building', 'MB', 1, 'A6'),
       (7, 'Main Building', 'MB', 1, 'A7'),
       (8, 'Main Building', 'MB', 1, 'A8'),
       (9, 'Main Building', 'MB', 1, 'A9');
insert into professor(id, name, surname, classroom_id, created_on)
values (1, 'John', 'Doe', 1, '2024-12-12'),
       (2, 'John', 'Doe', 2, '2024-12-12'),
       (3, 'John', 'Doe', 3, '2024-12-12'),
       (4, 'John', 'Doe', 4, '2024-12-12'),
       (5, 'John', 'Doe', 5, '2024-12-12'),
       (6, 'John', 'Doe', 6, '2024-12-12'),
       (7, 'John', 'Doe', 7, '2024-12-12'),
       (8, 'John', 'Doe', 8, '2024-12-12'),
       (9, 'John', 'Doe', 9, '2024-12-12');
insert into car(id, brand, license_plate, professor_id)
values (1, 'BMW 520d', '1234ABC', 1);
values (2, 'Mercedes AMG Avant-garde', '1234DFE', 1);
insert into course(id, name, credits)
values (1, 'Math', 5);
insert into professor_course(professor_id, course_id)
values (1, 1);
insert into student(id, name, surname, professor_id)
values (1, 'Jane', 'Doe', 1),
       (2, 'Meas', 'Doe', 1),
       (3, 'Lore', 'Doe', 1),
       (4, 'Dofe', 'Doe', 4),
       (5, 'Je', 'Doe', 4),
       (6, 'Je', 'Doe', 4),
       (7, 'Je', 'Doe', 5),
       (8, 'Je', 'Doe', 5),
       (9, 'Je', 'Doe', 7),
       (10, 'Je', 'Doe', 7),
       (11, 'Je', 'Doe', 8);
