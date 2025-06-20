package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        logger.info("Метод getAllStudents вызван.");
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        logger.info("Метод getStudentById вызван с аргументом {}", id);
        return studentRepository.findById(id);
    }

    public Student createStudent(Student student) {
        logger.info("Метод createStudent вызван с аргументами {}", student);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        logger.info("Метод updateStudent вызван с аргументами {}, {}", id, updatedStudent);
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            student.setName(updatedStudent.getName());
            student.setAge(updatedStudent.getAge());
            return studentRepository.save(student);
        }
        throw new RuntimeException("Студент не найден");
    }

    public void deleteStudent(Long id) {
        logger.info("Метод deleteStudent вызван с аргументом {}", id);
        studentRepository.deleteById(id);
    }

    public long getTotalNumberOfStudents() {

        return 0;
    }

    public double getAverageStudentAge() {
        return 0;
    }

    public List<Student> getLastFiveStudents() {
        return null;
    }

    public Student save(Student student) {

        return student;
    }

    public Optional<Student> findById(Long id) {
        return null;
    }

    public Optional<Student> update(Long id, Student student) {
        return null;
    }

    public boolean deleteById(Long id) {
        return false;
    }
}