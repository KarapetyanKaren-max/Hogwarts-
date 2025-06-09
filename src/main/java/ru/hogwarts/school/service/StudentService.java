package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
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

    /**
     * Найти студентов по возрасту
     * @param age возраст
     * @return список студентов с этим возрастом
     */
    public List<Student> findByAge(int age) {
        return studentRepository.findAllByAge(age);
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