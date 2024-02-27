package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.service.CourseService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(CourseRestController.class)
class CourseRestControllerTest {

    @MockBean
    private CourseService courseService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCourses_ShouldReturnListOfCourses() throws Exception {
        List<Course> mockCourses = Arrays.asList(
                createCourse(1L, "Course One"),
                createCourse(2L, "Course Two")
        );

        Page<Course> mockCoursePage = new PageImpl<>(mockCourses);
        when(courseService.findAll(any(PageRequest.class))).thenReturn(mockCoursePage);

        mockMvc.perform(get("/getListCourses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Course One"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Course Two"));

        verify(courseService, times(1)).findAll(any(PageRequest.class));
    }

    private Course createCourse(Long id, String name) {
        Course course = new Course();
        course.setId(id);
        course.setName(name);
        return course;
    }
}