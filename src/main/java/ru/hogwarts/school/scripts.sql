
SELECT * FROM students WHERE age BETWEEN 10 AND 20;


SELECT name FROM students;


SELECT * FROM students WHERE name LIKE '%О%';


SELECT * FROM students WHERE age < id;


SELECT * FROM students ORDER BY age ASC;


ALTER TABLE students ADD CONSTRAINT chk_student_age CHECK (age >= 16);


ALTER TABLE students ALTER COLUMN name SET NOT NULL;


ALTER TABLE students ADD CONSTRAINT unique_name UNIQUE (name);


ALTER TABLE faculties ADD CONSTRAINT unique_faculty_color_pair UNIQUE (name, color);


ALTER TABLE students ALTER COLUMN age SET DEFAULT 20;



CREATE TABLE people (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INTEGER NOT NULL,
    has_driving_license BOOLEAN NOT NULL);



CREATE TABLE cars (
    car_id SERIAL PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL);



CREATE TABLE person_car (
    person_id INT REFERENCES people(id),
    car_id INT REFERENCES cars(car_id),
    PRIMARY KEY(person_id, car_id));



SELECT s.name AS student_name, s.age, f.name AS faculty_name
FROM students s
LEFT JOIN faculties f ON s.faculty_id = f.id;



SELECT s.name AS student_name, a.file_path AS avatar_file_path
FROM students s
INNER JOIN avatars a ON s.id = a.student_id;