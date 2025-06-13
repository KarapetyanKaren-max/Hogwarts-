package ru.hogwarts.school;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Mock
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    void setup() {
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());
    }


    @Test
    void testGetFacultyOfStudent() {
        long studentId = 1L;
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Магия");


        when(studentService.getFacultyByStudentId(studentId)).thenReturn(Optional.of(faculty));

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/student/{id}/faculty", Faculty.class, studentId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(faculty);
    }

    @Test
    void testGetStudentsByAgeRange() {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Гарри Поттер");
        student1.setAge(13);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Гермиона Грейнджер");
        student2.setAge(14);

        when(studentService.findByAgeBetween(13, 14)).thenReturn(Arrays.asList(student1, student2));

        ParameterizedTypeReference<List<Student>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Student>> response = restTemplate.exchange("/student/between-ages?min=13&max=14", HttpMethod.GET, null, typeRef);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyInAnyOrder(student1, student2);
    }


    @Test
    void testCreateStudent() {
        Student newStudent = new Student();
        newStudent.setName("Рон Уизли");
        newStudent.setAge(13);

        when(studentService.save(newStudent)).thenReturn(newStudent);

        ResponseEntity<Student> response = restTemplate.postForEntity("/student", newStudent, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(newStudent);
    }

    @Test
    void testGetAllStudents() {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Невилл Долгопупс");
        student1.setAge(12);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Минерва Макгонагалл");
        student2.setAge(14);

        when(studentService.findAll()).thenReturn(Arrays.asList(student1, student2));

        ParameterizedTypeReference<List<Student>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Student>> response = restTemplate.exchange("/student", HttpMethod.GET, null, typeRef);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyInAnyOrder(student1, student2);
    }

    @Test
    void testGetStudentById() {
        long studentId = 1L;
        Student student = new Student();
        student.setId(studentId);
        student.setName("Джинни Уизли");
        student.setAge(13);

        when(studentService.findById(studentId)).thenReturn(Optional.of(student));

        ResponseEntity<Student> response = restTemplate.getForEntity("/student/{id}", Student.class, studentId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(student);
    }

    @Test
    void testUpdateStudent() {
        long studentId = 1L;
        Student oldStudent = new Student();
        oldStudent.setId(studentId);
        oldStudent.setName("Альбус Дамблдор");
        oldStudent.setAge(50);

        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setName("Обновлённый Альбус");
        updatedStudent.setAge(51);

        when(studentService.update(studentId, updatedStudent)).thenReturn(Optional.of(updatedStudent));

        ResponseEntity<Student> response = restTemplate.exchange("/student/{id}", HttpMethod.PUT, new ResponseEntity<>(updatedStudent, HttpStatus.OK), Student.class, studentId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedStudent);
    }

    @Test
    void testDeleteStudent() {
        long studentId = 1L;
        when(studentService.deleteById(studentId)).thenReturn(true);

        ResponseEntity<Void> response = restTemplate.exchange("/student/{id}", HttpMethod.DELETE, null, Void.class, studentId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
