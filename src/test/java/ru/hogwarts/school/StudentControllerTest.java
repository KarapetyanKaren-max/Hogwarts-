package ru.hogwarts.school;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private StudentService studentService;

    @BeforeEach
    void setUp() {}

    @Test
    void testGetFacultyOfStudent() throws Exception {
        long studentId = 1L;
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Факультет Магии");

        given(studentService.getFacultyByStudentId(studentId))
                .willReturn(Optional.of(faculty));

        mvc.perform(MockMvcRequestBuilders.get("/student/{id}/faculty", studentId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()));
    }

    @Test
    void testGetStudentsByAgeRange() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Студент 1");
        student1.setAge(13);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Студент 2");
        student2.setAge(14);

        given(studentService.findByAgeBetween(13, 14))
                .willReturn(List.of(student1, student2));

        mvc.perform(MockMvcRequestBuilders.get("/student/between-ages?min=13&max=14"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[*].id").value(Collections.singletonList(1L)))
                .andExpect(jsonPath("$.[*].name").value(Collections.singletonList("Студент 1")));
    }

    @Test
    void testCreateStudent() throws Exception {
        Student newStudent = new Student();
        newStudent.setName("Новый Студент");
        newStudent.setAge(12);

        given(studentService.save(any(Student.class)))
                .willReturn(newStudent);

        mvc.perform(MockMvcRequestBuilders.post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name").value(newStudent.getName(t)));
    }

    @Test
    void testGetAllStudents() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Студент 1");
        student1.setAge(13);

        given(studentService.findAll())
                .willReturn(List.of(student1));

        mvc.perform(MockMvcRequestBuilders.get("/student"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[*].id").value(Collections.singletonList(1L)));
    }

    @Test
    void testGetStudentById() throws Exception {
        long studentId = 1L;
        Student student = new Student();
        student.setId(studentId);
        student.setName("Именованный Студент");
        student.setAge(13);

        given(studentService.findById(studentId))
                .willReturn(Optional.of(student));

        mvc.perform(MockMvcRequestBuilders.get("/student/{id}", studentId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName(t)));
    }

    @Test
    void testUpdateStudent() throws Exception {
        long studentId = 1L;
        Student originalStudent = new Student();
        originalStudent.setId(studentId);
        originalStudent.setName("Первоначальное Имя");
        originalStudent.setAge(13);

        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setName("Изменённое Имя");
        updatedStudent.setAge(14);

        given(studentService.update(studentId, any(Student.class)))
                .willReturn(Optional.of(updatedStudent));

        mvc.perform(MockMvcRequestBuilders.put("/student/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value(updatedStudent.getName(t)));
    }

    @Test
    void testDeleteStudent() throws Exception {
        long studentId = 1L;
        given(studentService.deleteById(studentId))
                .willReturn(true);

        mvc.perform(MockMvcRequestBuilders.delete("/student/{id}", studentId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
