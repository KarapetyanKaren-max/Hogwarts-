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

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public long getTotalNumberOfStudents() {
        return studentRepository.countAllStudents();
    }

    public double getAverageStudentAge() {
        return studentRepository.averageStudentAge();
    }

    public List<Student> getLastFiveStudents() {
        return studentRepository.findLastFiveStudents();
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

    public Student save(Student student) {

        return student;
    }

    public Object getFacultyByStudentId(long studentId) {

        return null;
    }

    public Object findByAgeBetween(int i, int i1) {
        return null;
    }

    public Object findAll() {

        return null;
    }
}