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
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Faculty;

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
class CourseControllerIntegrationTest extends BaseIntegrationTest {

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
    void listCourses() throws Exception {
        MvcResult result =
                mvc.perform(get("/listCourses"))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(view().name("manage-course"))
                        .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        assertTrue(model.containsKey("courses"));
        assertTrue(model.containsKey("pageNumber"));
        assertTrue(model.containsKey("totalPages"));

        List<Course> courses = (List<Course>) model.get("courses");
        int pageNumber = (int) model.get("pageNumber");
        int totalPages = (int) model.get("totalPages");
        assertFalse(courses.isEmpty());
        assertEquals(0, pageNumber);
        assertEquals(3, totalPages);
    }

    @Test
    void showCourse() throws Exception {
        Course course = createCourse();
        courseRepository.save(course);

        mvc.perform(get("/showCourse?id=1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("course"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    void listCoursesByFacultyId() throws Exception {
        Faculty faculty = createFaculty();
        facultyRepository.save(faculty);

        mvc.perform(get("/listCoursesByFaculty/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("faculty"))
                .andExpect(model().attributeExists("courses", "pageNumber", "totalPages", "faculty"));
    }

    @Test
    void createFormCourse() throws Exception {
        mvc.perform(get("/createFormCourse"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-course"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    void createCourse_successful() throws Exception {
        Course course = createCourse();

        mvc.perform(post("/createCourse").flashAttr("course", course))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-course-successful"));

        Optional<Course> optionalSavedCourse = courseRepository.findById(1L);
        assertTrue(optionalSavedCourse.isPresent());
        assertEquals(optionalSavedCourse.get().getName(), "Course 101");
    }

    @Test
    void showUpdateForm() throws Exception {
        Course course = createCourse();
        courseRepository.save(course);

        mvc.perform(get("/updateFormCourse/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-course"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    void updateCourse_successful() throws Exception {
        String updatedCourseName = "Updated Course";
        Course course = createCourse();
        courseRepository.save(course);

        Course updatedCourse = new Course();
        updatedCourse.setName(updatedCourseName);

        mvc.perform(post("/updateCourse/1").flashAttr("course", updatedCourse))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-course-successful"));

        Optional<Course> optionalCourse = courseRepository.findById(1L);
        assertTrue(optionalCourse.isPresent());
        assertEquals(optionalCourse.get().getName(), updatedCourseName);
    }

    @Test
    void deleteCourse_successful() throws Exception {
        Course course = createCourse();
        courseRepository.save(course);

        mvc.perform(post("/deleteCourse/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("delete-form-course-successful"));

        assertFalse(courseRepository.findById(1L).isPresent());
    }
}
