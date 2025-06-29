package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/count-all-students")
    public ResponseEntity<Long> getTotalNumberOfStudents() {
        long totalCount = studentService.getTotalNumberOfStudents();
        return new ResponseEntity<>(totalCount, HttpStatus.OK);
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageStudentAge() {
        double avgAge = studentService.getAverageStudentAge();
        return new ResponseEntity<>(avgAge, HttpStatus.OK);
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAge() {
        double averageAge = studentService.calculateAverageAge();
        return new ResponseEntity<>(averageAge, HttpStatus.OK);
    }

    @GetMapping("/last-five-students")
    public ResponseEntity<List<Student>> getLastFiveStudents() {
        List<Student> lastFiveStudents = studentService.getLastFiveStudents();
        return new ResponseEntity<>(lastFiveStudents, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.save(student);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> studentOpt = studentService.findById(id);
        return studentOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        Optional<Student> updatedStudentOpt = studentService.update(id, student);
        return updatedStudentOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        boolean deleted = studentService.deleteStudent(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/names-starting-with-A")
    public ResponseEntity<List<String>> getStudentsNamesStartingWithA() {
        List<String> names = studentService.getStudentsNamesStartingWithA();
        return new ResponseEntity<>(names, HttpStatus.OK);
    }

    @GetMapping("/efficient-sum")
    public ResponseEntity<Integer> efficientSumEndpoint() {
        int sum = (1_000_000 * (1_000_000 + 1)) / 2;
        return new ResponseEntity<>(sum, HttpStatus.OK);
    }


    @GetMapping("/students/print-parallel")
    public void printParallelStudents() {

        List<Student> students = studentService.findAll();


        if (students.size() >= 6) {

            System.out.println("Main Thread: " + students.get(0).getName());
            System.out.println("Main Thread: " + students.get(1).getName());


            CompletableFuture.runAsync(() -> {
                System.out.println("Thread #1: " + students.get(2).getName());
                System.out.println("Thread #1: " + students.get(3).getName());
            });

            CompletableFuture.runAsync(() -> {
                System.out.println("Thread #2: " + students.get(4).getName());
                System.out.println("Thread #2: " + students.get(5).getName());
            });
        } else {
            throw new IllegalStateException("Not enough students to perform parallel printing");
        }
    }


    private synchronized void synchronizedPrint(String message) {
        System.out.println(message);
    }


    @GetMapping("/students/print-synchronized")
    public void printSynchronizedStudents() {

        List<Student> students = studentService.findAll();


        if (students.size() >= 6) {

            synchronizedPrint("Main Thread: " + students.get(0).getName());
            synchronizedPrint("Main Thread: " + students.get(1).getName());


            CompletableFuture.runAsync(() -> {
                synchronizedPrint("Thread #1: " + students.get(2).getName());
                synchronizedPrint("Thread #1: " + students.get(3).getName());
            });


            CompletableFuture.runAsync(() -> {
                synchronizedPrint("Thread #2: " + students.get(4).getName());
                synchronizedPrint("Thread #2: " + students.get(5).getName());
            });
        } else {
            throw new IllegalStateException("Not enough students to perform synchronized printing");
        }
    }
}