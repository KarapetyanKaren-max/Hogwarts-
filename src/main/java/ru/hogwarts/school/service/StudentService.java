package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    @Autowired
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

    public Optional<Student> updateStudent(Long id, Student updatedStudent) {
        logger.info("Был вызван метод updateStudent с аргументами {}, {}", id, updatedStudent);
        Optional<Student> existingStudentOptional = studentRepository.findById(id);
        if (existingStudentOptional.isPresent()) {
            Student existingStudent = existingStudentOptional.get();
            existingStudent.setName(updatedStudent.getName());
            existingStudent.setAge(updatedStudent.getAge());
            return Optional.of(studentRepository.save(existingStudent));
        }
        logger.warn("Студент с id {} не найден.", id);
        return Optional.empty();
    }

    public boolean deleteStudent(Long id) {
        logger.info("Был вызван метод deleteStudent с аргументом {}", id);
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        }
        return false;
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

    public Double calculateAverageAge() {
        logger.debug("Вычисляется средний возраст студентов.");
        List<Student> allStudents = studentRepository.findAll();
        if (allStudents.isEmpty()) {
            return 0.0;
        }
        double sumOfAges = allStudents.stream()
                .mapToInt(Student::getAge)
                .sum();
        return sumOfAges / allStudents.size();
    }

    public List<String> getStudentsNamesStartingWithA() {
        logger.debug("Запрашивается список студентов с именем, начинающимся на A");
        return studentRepository.findAllByNameStartingWith("A").stream()
                .map(Student::getName)
                .collect(Collectors.toList());
    }

    public List<Student> findAll() {
        return null;
    }

    public Optional<Student> findById(Long id) {
        return null;
    }

    public Optional<Student> update(Long id, Student student) {
        return null;
    }

    public Student save(Student student) {
        return null;
    }
}