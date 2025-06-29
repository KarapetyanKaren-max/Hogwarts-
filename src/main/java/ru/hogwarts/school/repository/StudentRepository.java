package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    long countAllStudents();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Double averageStudentAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudents();

    List<Student> findTop5ByOrderByIdDesc();


    @Query(value = "SELECT * FROM student WHERE name LIKE CONCAT(:prefix, '%')", nativeQuery = true)
    List<Student> findAllByNameStartingWith(@Param("prefix") String prefix);
}