package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    /**
     * Эндпоинт для фильтрации факультетов по цвету
     * URL: /faculties/by-color?color=red
     */
    @GetMapping("/faculties/by-color")
    public List<Faculty> getFacultiesByColor(@RequestParam String color) {
        return facultyService.findByColor(color);
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