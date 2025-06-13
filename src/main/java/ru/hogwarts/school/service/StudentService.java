package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Optional<Faculty> getFacultyByStudentId(Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        return studentOptional.map(Student::getFaculty);
    }

    public List<Student> findByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> update(Long id, Student newData) {
        Optional<Student> existingOpt = studentRepository.findById(id);
        if (existingOpt.isPresent()) {
            Student existing = existingOpt.get();
            existing.setName(newData.getName());
            existing.setEmail(newData.getEmail());
            studentRepository.save(existing);
            return Optional.of(existing);
        }
        return Optional.empty();
    }

    public boolean deleteById(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}