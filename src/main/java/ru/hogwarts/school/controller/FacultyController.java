package ru.hogwarts.school.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudentsOfFaculty(@PathVariable Long id) {
        List<Student> students = facultyService.getStudentsByFacultyId(id);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/search-by-keyword")
    public ResponseEntity<List<Faculty>> searchFacultiesByKeyword(@RequestParam String keyword) {
        List<Faculty> result = facultyService.searchByKeyword(keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/faculties/by-color")
    public ResponseEntity<List<Faculty>> getFacultiesByColor(@RequestParam String color) {
        List<Faculty> result = facultyService.findByColor(color);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.save(faculty);
        return new ResponseEntity<>(createdFaculty, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        List<Faculty> faculties = facultyService.findAll();
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable Long id) {
        Optional<Faculty> facultyOpt = facultyService.findById(id);
        return facultyOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        Optional<Faculty> updatedFacultyOpt = facultyService.update(id, faculty);
        return updatedFacultyOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        boolean deleted = facultyService.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}