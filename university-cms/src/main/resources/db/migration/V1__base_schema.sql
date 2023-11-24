CREATE TABLE administrators
(
    administrator_id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255)
);

CREATE TABLE teachers
(
    teacher_id              BIGSERIAL PRIMARY KEY,
    first_name      VARCHAR(255),
    last_name       VARCHAR(255),
    teacher_academic_degree VARCHAR(255)
);

CREATE TABLE faculties
(
    faculty_id   BIGSERIAL PRIMARY KEY,
    faculty_name VARCHAR(255)
);

CREATE TABLE courses
(
    course_id   BIGSERIAL PRIMARY KEY,
    faculty_id  BIGSERIAL,
    course_name VARCHAR(255),
    FOREIGN KEY (faculty_id) REFERENCES faculties (faculty_id)

);

CREATE TABLE groups
(
    group_id   BIGSERIAL PRIMARY KEY,
    course_id  BIGSERIAL,
    group_name VARCHAR(255),
    FOREIGN KEY (course_id) REFERENCES courses (course_id)
);

CREATE TABLE students
(
    student_id BIGSERIAL PRIMARY KEY,
    group_id   BIGSERIAL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    FOREIGN KEY (group_id) REFERENCES groups (group_id)
);

CREATE TABLE lectures
(
    lectures_id   BIGSERIAL PRIMARY KEY,
    course_id     BIGSERIAL,
    teacher_id    BIGSERIAL,
    lectures_name VARCHAR(255),
    description   VARCHAR(2000),
    lectures_date TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses (course_id),
    FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id)
);

CREATE TABLE group_lecture
(
    group_id   BIGSERIAL,
    lecture_id BIGSERIAL,
    FOREIGN KEY (group_id) REFERENCES groups (group_id),
    FOREIGN KEY (lecture_id) REFERENCES lectures (lectures_id)
);
