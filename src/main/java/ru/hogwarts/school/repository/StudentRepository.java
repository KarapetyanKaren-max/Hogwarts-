package ru.hogwarts.school.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
        /**
         * Найти всех студентов по возрасту
         * @param age возраст студента
         * @return список студентов с этим возрастом
         */
        List<Student> findAllByAge(int age);
    }