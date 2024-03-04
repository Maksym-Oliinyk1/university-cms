package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Faculty;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Testcontainers
@SpringBootTest
class CourseControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void listCourses() throws Exception {
        mvc.perform(get("/listCourses"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("manage-course"));
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

        mvc.perform(post("/createCourse")
                        .flashAttr("course", course))
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

        mvc.perform(post("/updateCourse/1")
                        .flashAttr("course", updatedCourse))
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
