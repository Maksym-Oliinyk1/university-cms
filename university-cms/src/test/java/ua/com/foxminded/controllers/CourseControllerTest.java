package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.controllers.course.CourseController;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.FacultyService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @MockBean
    private CourseService courseService;

    @MockBean
    private FacultyService facultyService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void manageCourse_ShouldReturnManageCoursePage() throws Exception {
        mockMvc.perform(get("/manageCourse"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-course"));
    }

    @Test
    void showCourse_ValidId_ShouldReturnCoursePage() throws Exception {
        Long courseId = 1L;
        Course mockCourse = new Course();
        when(courseService.findById(courseId)).thenReturn(mockCourse);

        mockMvc.perform(get("/showCourse")
                        .param("id", String.valueOf(courseId)))
                .andExpect(status().isOk())
                .andExpect(view().name("course"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attribute("course", mockCourse));

        verify(courseService, times(1)).findById(courseId);
    }

    @Test
    void listCourses_ShouldReturnManageCoursePage() throws Exception {
        Page<Course> mockCoursePage = mock(Page.class);
        when(mockCoursePage.getContent()).thenReturn(Collections.emptyList());
        when(courseService.findAll(any())).thenReturn(mockCoursePage);

        mockMvc.perform(get("/listCourses"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-course"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"));

        verify(courseService, times(1)).findAll(any());
    }

    @Test
    void listCoursesToLecture_ShouldReturnCreateFormLecturePage() throws Exception {
        Page<Course> mockCoursePage = mock(Page.class);
        when(mockCoursePage.getContent()).thenReturn(Collections.emptyList());
        when(courseService.findAll(any())).thenReturn(mockCoursePage);

        mockMvc.perform(get("/listCoursesToLecture"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-form-lecture"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"));

        verify(courseService, times(1)).findAll(any());
    }

    @Test
    void createCourse_ValidInput_ShouldReturnCreateFormCourseSuccessfulPage() throws Exception {
        mockMvc.perform(post("/createCourse")
                        .param("name", "Math 101")
                        .param("description", "Introduction to Calculus"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-form-course-successful"));

        verify(courseService, times(1)).save(any());
    }

    @Test
    void showUpdateForm_ValidId_ShouldReturnUpdateFormCoursePage() throws Exception {
        Long courseId = 1L;
        Course mockCourse = new Course();
        when(courseService.findById(courseId)).thenReturn(mockCourse);

        mockMvc.perform(get("/updateFormCourse/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(view().name("update-form-course"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attribute("course", mockCourse));

        verify(courseService, times(1)).findById(courseId);
    }

    @Test
    void updateCourse_ValidInput_ShouldReturnUpdateFormCourseSuccessfulPage() throws Exception {
        Long courseId = 1L;
        mockMvc.perform(post("/updateCourse/{id}", courseId)
                        .param("name", "Math 102")
                        .param("description", "Advanced Calculus"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-form-course-successful"));

        verify(courseService, times(1)).update(eq(courseId), any());
    }

    @Test
    void deleteCourse_ValidId_ShouldReturnDeleteFormCourseSuccessfulPage() throws Exception {
        Long courseId = 1L;

        mockMvc.perform(post("/deleteCourse/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(view().name("delete-form-course-successful"));

        verify(courseService, times(1)).delete(courseId);
    }

    @Test
    void listCoursesByFacultyId_ShouldReturnFacultyPageWithCourses() throws Exception {
        Long facultyId = 1L;
        Page<Course> mockCoursePage = mock(Page.class);
        Faculty mockFaculty = new Faculty();
        when(mockCoursePage.getContent()).thenReturn(Collections.emptyList());
        when(courseService.findAllOfFaculty(facultyId, PageRequest.of(0, 10))).thenReturn(mockCoursePage);
        when(facultyService.findById(facultyId)).thenReturn(mockFaculty);

        mockMvc.perform(get("/listCoursesByFaculty/{facultyId}", facultyId))
                .andExpect(status().isOk())
                .andExpect(view().name("faculty"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("faculty"))
                .andExpect(model().attribute("faculty", mockFaculty));

        verify(courseService, times(1)).findAllOfFaculty(facultyId, PageRequest.of(0, 10));
        verify(facultyService, times(1)).findById(facultyId);
    }
}
