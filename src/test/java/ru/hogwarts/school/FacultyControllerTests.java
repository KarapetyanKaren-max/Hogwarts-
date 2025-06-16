package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FacultyControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private FacultyRepository facultyRepository;

    @BeforeEach
    void setup() {
        when(facultyRepository.findAll()).thenReturn(Collections.emptyList());
    }

    @Test
    void testGetStudentsOfFaculty() {
        long facultyId = 1L;
        List<Student> students = Arrays.asList(new Student(), new Student());

        when(facultyService.getStudentsByFacultyId(anyLong())).thenReturn(students);

        ParameterizedTypeReference<List<Student>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Student>> response = restTemplate.exchange("/faculty/{id}/students", HttpMethod.GET, null, typeRef, facultyId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyElementsOf(students);
    }

    @Test
    void testSearchFacultiesByKeyword() {
        String keyword = "магический";
        List<Faculty> faculties = Arrays.asList(new Faculty(), new Faculty());

        when(facultyService.searchByKeyword(keyword)).thenReturn(faculties);

        ParameterizedTypeReference<List<Faculty>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Faculty>> response = restTemplate.exchange("/faculty/search-by-keyword?keyword={keyword}", HttpMethod.GET, null, typeRef, keyword);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyElementsOf(faculties);
    }

    @Test
    void testGetFacultiesByColor() {
        String color = "красный";
        List<Faculty> faculties = Arrays.asList(new Faculty(), new Faculty());

        when(facultyService.findByColor(color)).thenReturn(faculties);

        ParameterizedTypeReference<List<Faculty>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Faculty>> response = restTemplate.exchange("/faculty/faculties/by-color?color={color}", HttpMethod.GET, null, typeRef, color);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyElementsOf(faculties);
    }

    @Test
    void testCreateFaculty() {
        Faculty newFaculty = new Faculty();
        newFaculty.setName("Магическая школа");
        newFaculty.setColor("Зелёный");

        when(facultyService.save(newFaculty)).thenReturn(newFaculty);

        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculty", newFaculty, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(newFaculty);
    }

    @Test
    void testGetAllFaculties() {
        List<Faculty> faculties = Arrays.asList(new Faculty(), new Faculty());

        when(facultyService.findAll()).thenReturn(faculties);

        ParameterizedTypeReference<List<Faculty>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Faculty>> response = restTemplate.exchange("/faculty", HttpMethod.GET, null, typeRef);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyElementsOf(faculties);
    }

    @Test
    void testGetFacultyById() {
        long facultyId = 1L;
        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName("Хогвартс");
        faculty.setColor("Красный");

        when(facultyService.findById(facultyId)).thenReturn(java.util.Optional.of(faculty));

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/{id}", Faculty.class, facultyId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(faculty);
    }

    @Test
    void testUpdateFaculty() {
        long facultyId = 1L;
        Faculty originalFaculty = new Faculty();
        originalFaculty.setId(facultyId);
        originalFaculty.setName("Волшебная академия");
        originalFaculty.setColor("Голубой");

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(facultyId);
        updatedFaculty.setName("Обновленная магическая школа");
        updatedFaculty.setColor("Белый");

        when(facultyService.update(facultyId, updatedFaculty)).thenReturn(java.util.Optional.of(updatedFaculty));

        ResponseEntity<Faculty> response = restTemplate.exchange("/faculty/{id}", HttpMethod.PUT, new ResponseEntity<>(updatedFaculty, HttpStatus.OK), Faculty.class, facultyId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedFaculty);
    }

    @Test
    void testDeleteFaculty() {
        long facultyId = 1L;
        when(facultyService.deleteById(facultyId)).thenReturn(true);

        ResponseEntity<Void> response = restTemplate.exchange("/faculty/{id}", HttpMethod.DELETE, null, Void.class, facultyId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
