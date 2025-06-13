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
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private FacultyService facultyService;

    @BeforeEach
    void setUp() {}


    @Test
    void testGetStudentsOfFaculty() throws Exception {
        long facultyId = 1L;
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Студент 1");
        student1.setAge(13);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Студент 2");
        student2.setAge(14);

        given(facultyService.getStudentsByFacultyId(facultyId))
                .willReturn(List.of(student1, student2));

        mvc.perform(MockMvcRequestBuilders.get("/faculty/{id}/students", facultyId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[*].id").value(Collections.singletonList(1L)))
                .andExpect(jsonPath("$.[*].name").value(Collections.singletonList("Студент 1")));
    }

    @Test
    void testSearchFacultiesByKeyword() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Факультет Волшебства");
        faculty1.setColor("фиолетовый");

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("Факультет Природоведения");
        faculty2.setColor("зеленый");

        given(facultyService.searchByKeyword("факультет"))
                .willReturn(List.of(faculty1, faculty2));

        mvc.perform(MockMvcRequestBuilders.get("/faculty/search-by-keyword?keyword=fakultet"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[*].id").value(Collections.singletonList(1L)));
    }


    @Test
    void testGetFacultiesByColor() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Красочный Факультет");
        faculty1.setColor("желтый");

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("Радужный Факультет");
        faculty2.setColor("розовый");

        given(facultyService.findByColor("желтый"))
                .willReturn(List.of(faculty1));

        mvc.perform(MockMvcRequestBuilders.get("/faculty/faculties/by-color?color=zheltyi"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[*].id").value(Collections.singletonList(1L)));
    }

    @Test
    void testCreateFaculty() throws Exception {
        Faculty newFaculty = new Faculty();
        newFaculty.setName("Новенький Факультет");
        newFaculty.setColor("серебряный");

        given(facultyService.save(any(Faculty.class)))
                .willReturn(newFaculty);

        mvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFaculty)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name").value("Новенький Факультет"));
    }

    @Test
    void testGetAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Первый Факультет");
        faculty1.setColor("голубой");

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("Второй Факультет");
        faculty2.setColor("оранжевый");

        given(facultyService.findAll())
                .willReturn(List.of(faculty1, faculty2));

        mvc.perform(MockMvcRequestBuilders.get("/faculty"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[*].id").value(Collections.singletonList(1L)));
    }

    @Test
    void testGetFacultyById() throws Exception {
        long facultyId = 1L;
        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName("Специальный Факультет");
        faculty.setColor("золотой");

        given(facultyService.findById(facultyId))
                .willReturn(Optional.of(faculty));

        mvc.perform(MockMvcRequestBuilders.get("/faculty/{id}", facultyId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()));
    }

    @Test
    void testUpdateFaculty() throws Exception {
        long facultyId = 1L;
        Faculty originalFaculty = new Faculty();
        originalFaculty.setId(facultyId);
        originalFaculty.setName("Старое Название");
        originalFaculty.setColor("черный");

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(facultyId);
        updatedFaculty.setName("Обновленное Название");
        updatedFaculty.setColor("белый");

        given(facultyService.update(facultyId, any(Faculty.class)))
                .willReturn(Optional.of(updatedFaculty));

        mvc.perform(MockMvcRequestBuilders.put("/faculty/{id}", facultyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFaculty)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value("Обновленное Название"));
    }

    @Test
    void testDeleteFaculty() throws Exception {
        long facultyId = 1L;
        given(facultyService.deleteById(facultyId))
                .willReturn(true);

        mvc.perform(MockMvcRequestBuilders.delete("/faculty/{id}", facultyId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
