package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    private final Object outputLock = new Object();

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public List<Student> getAllStudents() {
        logger.debug("Получаем всех студентов");
        return studentRepository.findAll();
    }


    public Optional<Student> getStudentById(Long id) {
        logger.info("Ищем студента с ID {}", id);
        return studentRepository.findById(id);
    }


    public Student createStudent(Student student) {
        logger.info("Создаём студента {}", student);
        return studentRepository.save(student);
    }


    public Optional<Student> updateStudent(Long id, Student updatedStudent) {
        logger.info("Обновляем студента с ID {}", id);
        Optional<Student> existingStudentOptional = studentRepository.findById(id);
        if (existingStudentOptional.isPresent()) {
            Student existingStudent = existingStudentOptional.get();
            existingStudent.setName(updatedStudent.getName());
            existingStudent.setAge(updatedStudent.getAge());
            return Optional.of(studentRepository.save(existingStudent));
        }
        logger.warn("Студент с ID {} не найден", id);
        return Optional.empty();
    }


    public boolean deleteStudent(Long id) {
        logger.info("Удаляем студента с ID {}", id);
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void syncPrint(String message) {
        synchronized (outputLock) {
            System.out.println(message);
        }
    }


    public void printStudentsInParallel(List<Student> students) {
        if (students.size() >= 6) {
            ExecutorService executor = Executors.newFixedThreadPool(2);
            try {

                System.out.println(students.get(0).getName());
                System.out.println(students.get(1).getName());


                executor.submit(() -> {
                    System.out.println(students.get(2).getName());
                    System.out.println(students.get(3).getName());
                });


                executor.submit(() -> {
                    System.out.println(students.get(4).getName());
                    System.out.println(students.get(5).getName());
                });


                executor.shutdown();
                executor.awaitTermination(1L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                if (!executor.isTerminated()) {
                    executor.shutdownNow();
                }
            }
        } else {
            throw new IllegalArgumentException("Минимально допустимое количество студентов — 6.");
        }
    }


    public void printStudentsSynchronized(List<Student> students) {
        if (students.size() >= 6) {
            ExecutorService executor = Executors.newFixedThreadPool(2);
            try {

                syncPrint(students.get(0).getName());
                syncPrint(students.get(1).getName());


                executor.submit(() -> {
                    syncPrint(students.get(2).getName());
                    syncPrint(students.get(3).getName());
                });


                executor.submit(() -> {
                    syncPrint(students.get(4).getName());
                    syncPrint(students.get(5).getName());
                });


                executor.shutdown();
                executor.awaitTermination(1L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                if (!executor.isTerminated()) {
                    executor.shutdownNow();
                }
            }
        } else {
            throw new IllegalArgumentException("Минимально допустимое количество студентов — 6.");
        }
    }

    public long getTotalNumberOfStudents() {
        logger.debug("Подсчитываем общее число студентов");
        return studentRepository.count();
    }

    public double getAverageStudentAge() {
        logger.debug("Рассчитываем средний возраст студентов");
        List<Student> students = studentRepository.findAll();
        if (!students.isEmpty()) {
            double sum = students.stream().mapToDouble(Student::getAge).sum();
            return sum / students.size();
        }
        return 0.0;
    }

    public List<Student> getLastFiveStudents() {
        logger.debug("Получаем последних 5 студентов");
        return studentRepository.findTop5ByOrderByIdDesc();
    }

    public Double calculateAverageAge() {
        logger.debug("Расчёт средней оценки");
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
        logger.debug("Получаем список студентов с именами, начинающимися на букву \"A\"");
        return studentRepository.findAllByNameStartingWith("A")
                .stream()
                .map(Student::getName)
                .collect(Collectors.toList());
    }
}