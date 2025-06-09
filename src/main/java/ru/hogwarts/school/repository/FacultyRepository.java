package ru.hogwarts.school.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    /**
     * Найти все факультеты по цвету (игнорируя регистр)
     */
    List<Faculty> findAllByColorIgnoreCase(String color);
}