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
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;

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
class StudentControllerIntegrationTest extends BaseIntegrationTest {

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
    void studentAuthorization() throws Exception {
        mvc.perform(get("/studentAuthorization"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("mock-student-authorization"));
    }

    @Test
    void manageStudent() throws Exception {
        mvc.perform(get("/manageStudent"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("manage-student"));
    }

    @Test
    void showStudent() throws Exception {
        Student student = createStudent();
        studentRepository.save(student);

        MvcResult result =
                mvc.perform(get("/showStudent?id=1"))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(view().name("student"))
                        .andExpect(model().attributeExists("student"))
                        .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        assertTrue(model.containsKey("student"));

        Student studentFromModel = (Student) model.get("student");

        baseTestForUser(studentFromModel);
    }

    @Test
    void listStudents() throws Exception {
        MvcResult result =
                mvc.perform(get("/listStudents"))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(view().name("manage-student"))
                        .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        assertTrue(model.containsKey("students"));
        assertTrue(model.containsKey("pageNumber"));
        assertTrue(model.containsKey("totalPages"));

        List<Student> students = (List<Student>) model.get("students");
        int pageNumber = (int) model.get("pageNumber");
        int totalPages = (int) model.get("totalPages");
        assertFalse(students.isEmpty());
        assertEquals(0, pageNumber);
        assertEquals(20, totalPages);
    }

    @Test
    void listStudentsByGroup() throws Exception {
        Group group = createGroup();
        groupRepository.save(group);

        mvc.perform(get("/listStudentsByGroup/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("group"))
                .andExpect(model().attributeExists("group", "students", "pageNumber", "totalPages"));
    }

    @Test
    void createFormStudent() throws Exception {
        mvc.perform(get("/createFormStudent"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-student"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    void createStudent_successful() throws Exception {
        StudentDTO studentDTO = createStudentDTO();

        mvc.perform(post("/createStudent").flashAttr("studentDTO", studentDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-student-successful"));

        Optional<Student> optionalStudent = studentRepository.findById(DEFAULT_ID);
        assertTrue(optionalStudent.isPresent());
        Student studentFromModel = optionalStudent.get();
        baseTestForUser(studentFromModel);
    }

    @Test
    void showUpdateForm() throws Exception {
        Student student = createStudent();
        studentRepository.save(student);

        mvc.perform(get("/updateFormStudent/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-student"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    void updateStudent_successful() throws Exception {
        String updatedStudentName = "UpdatedStudent";
        Student student = createStudent();
        studentRepository.save(student);

        StudentDTO studentDTO = createStudentDTO();
        studentDTO.setFirstName(updatedStudentName);

        mvc.perform(post("/updateStudent/1").flashAttr("studentDTO", studentDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-student-successful"));

        Optional<Student> optionalStudent = studentRepository.findById(1L);
        assertTrue(optionalStudent.isPresent());
        Student updatedStudent = optionalStudent.get();
        assertEquals(updatedStudent.getFirstName(), updatedStudentName);
    }

    @Test
    void deleteStudent_successful() throws Exception {
        Student student = createStudent();
        studentRepository.save(student);

        mvc.perform(post("/deleteStudent/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("delete-form-student-successful"));

        assertFalse(studentRepository.findById(1L).isPresent());
    }
}
