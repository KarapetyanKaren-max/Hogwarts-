package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = "SELECT COUNT(*) FROM students", nativeQuery = true)
    long countAllStudents();

    @Query(value = "SELECT AVG(age) FROM students", nativeQuery = true)
    Double averageStudentAge();

    @Query(value = "SELECT * FROM students ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudents();
}