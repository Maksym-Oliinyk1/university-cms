package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.service.CourseService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class CourseRestControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseRestController courseRestController;

    private MockMvc mockMvc;

    @Test
    void getCourses_ShouldReturnListOfCourses() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(courseRestController).build();

        List<Course> mockCourses = new ArrayList<>();
        Course courseOne = new Course();
        Course courseTwo = new Course();
        courseOne.setId(1L);
        courseOne.setName("Course One");
        courseTwo.setId(2L);
        courseTwo.setName("Course Two");
        mockCourses.add(courseOne);
        mockCourses.add(courseTwo);

        Page<Course> mockCoursePage = new PageImpl<>(mockCourses);

        when(courseService.findAll(any(PageRequest.class))).thenReturn(mockCoursePage);

        mockMvc.perform(get("/getListCourses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Course One")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Course Two")));

        verify(courseService, times(1)).findAll(any(PageRequest.class));
    }
}