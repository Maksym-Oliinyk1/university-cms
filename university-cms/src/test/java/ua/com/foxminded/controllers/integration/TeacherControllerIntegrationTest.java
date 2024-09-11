package ua.com.foxminded.controllers.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Teacher;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerIntegrationTest extends BaseIntegrationTest {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"));
    @Autowired
    private MockMvc mvc;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @Test
    void teacherAuthorization() throws Exception {
        mvc.perform(get("/teacherAuthorization"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("mock-teacher-authorization"));
    }

    @Test
    void manageTeacher() throws Exception {
        mvc.perform(get("/manageTeacher"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("manage-teacher"));
    }

    @Test
    void showTeacher() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        mvc.perform(get("/showTeacher?id=1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("teacher"))
                .andExpect(model().attributeExists("teacher"));
    }

    @Test
    void listTeachers() throws Exception {
        MvcResult result =
                mvc.perform(get("/listTeachers"))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(view().name("manage-teacher"))
                        .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        assertTrue(model.containsKey("teachers"));
        assertTrue(model.containsKey("pageNumber"));
        assertTrue(model.containsKey("totalPages"));

        List<Teacher> teachers = (List<Teacher>) model.get("teachers");
        int pageNumber = (int) model.get("pageNumber");
        int totalPages = (int) model.get("totalPages");
        assertFalse(teachers.isEmpty());
        assertEquals(0, pageNumber);
        assertEquals(3, totalPages);
    }

    @Test
    void createFormTeacher() throws Exception {
        mvc.perform(get("/createFormTeacher"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-teacher"))
                .andExpect(model().attributeExists("teacher"));
    }

    @Test
    void createTeacher_successful() throws Exception {
        TeacherDTO teacherDTO = createTeacherDTO();

        mvc.perform(post("/createTeacher").flashAttr("teacherDTO", teacherDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-teacher-successful"));

        Optional<Teacher> optionalTeacher = teacherRepository.findById(DEFAULT_ID);
        assertTrue(optionalTeacher.isPresent());
        Teacher teacherFromModel = optionalTeacher.get();
        baseTestForUser(teacherFromModel);
        assertEquals("Ph.D", teacherFromModel.getAcademicDegree());
    }

    @Test
    void showUpdateForm() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        MvcResult result =
                mvc.perform(get("/updateFormTeacher/1"))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(view().name("update-form-teacher"))
                        .andExpect(model().attributeExists("teacher"))
                        .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        assertTrue(model.containsKey("teacher"));

        Teacher teacherFromModel = (Teacher) model.get("teacher");

        baseTestForUser(teacherFromModel);
        assertEquals("Ph.D", teacherFromModel.getAcademicDegree());
    }

    @Test
    void updateTeacher_successful() throws Exception {
        String updatedTeacherName = "UpdatedTeacher";
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        TeacherDTO teacherDTO = createTeacherDTO();
        teacherDTO.setFirstName(updatedTeacherName);

        mvc.perform(post("/updateTeacher/1").flashAttr("teacherDTO", teacherDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-teacher-successful"));

        Optional<Teacher> optionalTeacher = teacherRepository.findById(1L);
        assertTrue(optionalTeacher.isPresent());
        Teacher updatedTeacher = optionalTeacher.get();
        assertEquals(updatedTeacher.getFirstName(), updatedTeacherName);
    }

    @Test
    void deleteTeacher_successful() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        mvc.perform(post("/deleteTeacher/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("delete-form-teacher-successful"));

        assertFalse(teacherRepository.findById(1L).isPresent());
    }
}
