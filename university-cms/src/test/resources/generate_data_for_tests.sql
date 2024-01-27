INSERT INTO administrators (administrator_id, first_name, last_name)
VALUES (2, 'Admin2', 'LastName2'),
       (3, 'Admin3', 'LastName3');

INSERT INTO teachers (teacher_id, first_name, last_name, academic_degree)
VALUES (2, 'Teacher2', 'LastName2', 'PhD'),
       (3, 'Teacher3', 'LastName3', 'MSc');

INSERT INTO faculties (faculty_id, faculty_name)
VALUES (2, 'Faculty2'),
       (3, 'Faculty3');

INSERT INTO courses (course_id, faculty_id, course_name)
VALUES (2, 3, 'Course2'),
       (3, 3, 'Course3');

INSERT INTO groups (group_id, group_name)
VALUES (2, 'Group2'),
       (3, 'Group3');

INSERT INTO students (student_id, group_id, first_name, last_name)
VALUES (2, 3, 'Student2', 'LastName2'),
       (3, 3, 'Student3', 'LastName3');

INSERT INTO lectures (lecture_id, course_id, teacher_id, lecture_name, description, lecture_date)
VALUES (2, 2, 3, 'Lecture2', 'Description2', '2023-12-01 12:00:00'),
       (3, 2, 3, 'Lecture3', 'Description3', '2023-12-04 15:00:00');

INSERT INTO group_lecture (group_id, lecture_id)
VALUES (3, 3),
       (3, 2);