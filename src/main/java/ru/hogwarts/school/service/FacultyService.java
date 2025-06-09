package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }
    /**
     * Найти факультеты по цвету (игнорируя регистр)
     */
    public List<Faculty> findByColor(String color) {
        return facultyRepository.findAllByColorIgnoreCase(color);
    }
    public Faculty save(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    public Optional<Faculty> findById(Long id) {
        return facultyRepository.findById(id);
    }

    public Optional<Faculty> update(Long id, Faculty newData) {
        Optional<Faculty> existingOpt = facultyRepository.findById(id);
        if (existingOpt.isPresent()) {
            Faculty existing = existingOpt.get();
            existing.setName(newData.getName());
            existing.setId(newData.getId());
            facultyRepository.save(existing);
            return Optional.of(existing);
        }
        return Optional.empty();
    }

    public boolean deleteById(Long id) {
        if (facultyRepository.existsById(id)) {
            facultyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}