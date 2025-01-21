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
insert into professor(name, surname, classroom_id, created_on)
values ('John1', 'Doe', 1, '2024-12-12'),
       ('John2', 'Doe', 2, '2024-12-12'),
       ('John3', 'Doe', 3, '2024-12-12'),
       ('John4', 'Doe', 4, '2024-12-12'),
       ('John5', 'Doe', 5, '2024-12-12'),
       ('John6', 'Doe', 6, '2024-12-12'),
       ('John7', 'Doe', 7, '2024-12-12'),
       ('John8', 'Doe', 8, '2024-12-12'),
       ('John9', 'Doe', 9, '2024-12-12');
insert into car(id, brand, license_plate, professor_name, professor_surname)
values (1, 'BMW 520d', '1234ABC', 'John1', 'Doe');
values (2, 'Mercedes AMG Avant-garde', '1234DFE', 'John1', 'Doe');
insert into course(id, name, credits)
values (1, 'Math', 5);
insert into professor_course(professor_name, professor_surname, course_id)
values ('John1', 'Doe', 1);
insert into student(id, name, surname, professor_name, professor_surname)
values (1, 'Jane', 'Doe', 'John1', 'Doe'),
       (2, 'Meas', 'Doe', 'John1', 'Doe'),
       (3, 'Lore', 'Doe', 'John1', 'Doe'),
       (4, 'Dofe', 'Doe', 'John4', 'Doe'),
       (5, 'Je', 'Doe', 'John4', 'Doe'),
       (6, 'Je', 'Doe', 'John4', 'Doe'),
       (7, 'Je', 'Doe', 'John5', 'Doe'),
       (8, 'Je', 'Doe', 'John5', 'Doe'),
       (9, 'Je', 'Doe', 'John7', 'Doe'),
       (10, 'Je', 'Doe', 'John7', 'Doe'),
       (11, 'Je', 'Doe', 'John8', 'Doe');
