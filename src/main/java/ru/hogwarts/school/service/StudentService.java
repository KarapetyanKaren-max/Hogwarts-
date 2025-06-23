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
        logger.debug("Был вызван метод getAllStudents");
        return studentRepository.findAll();
    }


    public Optional<Student> getStudentById(Long id) {
        logger.info("Был вызван метод getStudentById с аргументом {}", id);
        return studentRepository.findById(id);
    }


    public Student createStudent(Student student) {
        logger.info("Был вызван метод createStudent с аргументами {}", student);
        return studentRepository.save(student);
    }


    public Student updateStudent(Long id, Student updatedStudent) {
        logger.info("Был вызван метод updateStudent с аргументами {}, {}", id, updatedStudent);
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            student.setName(updatedStudent.getName());
            student.setAge(updatedStudent.getAge());
            return studentRepository.save(student);
        }
        logger.warn("Студент с id {} не найден.", id);
        throw new IllegalArgumentException("Студент не найден");
    }


    public void deleteStudent(Long id) {
        logger.info("Был вызван метод deleteStudent с аргументом {}", id);
        studentRepository.deleteById(id);
    }


    public long getTotalNumberOfStudents() {
        logger.debug("Был вызван метод getTotalNumberOfStudents");
        return studentRepository.count();
    }

    public double getAverageStudentAge() {
        logger.debug("Был вызван метод getAverageStudentAge");
        List<Student> students = studentRepository.findAll();
        if (!students.isEmpty()) {
            double sum = students.stream().mapToDouble(Student::getAge).sum();
            return sum / students.size();
        }
        return 0.0;
    }

    public List<Student> getLastFiveStudents() {
        logger.debug("Был вызван метод getLastFiveStudents");
        return studentRepository.findTop5ByOrderByIdDesc();
    }

    public boolean deleteById(Long id) {
        return false;
    }

    public Student save(Student student) {
        return null;
    }

    public Optional<Student> findById(Long id) {
        return null;
    }

    public Optional<Student> update(Long id, Student student) {
        return null;
    }
}