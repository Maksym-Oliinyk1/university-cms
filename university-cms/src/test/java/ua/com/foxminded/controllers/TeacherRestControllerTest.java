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
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.TeacherService;

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
class TeacherRestControllerTest {

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherRestController teacherRestController;

    private MockMvc mockMvc;

    @Test
    void getTeachers_ShouldReturnListOfTeachers() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherRestController).build();

        List<Teacher> mockTeachers = new ArrayList<>();
        Teacher teacherOne = new Teacher();
        Teacher teacherTwo = new Teacher();
        teacherOne.setId(1L);
        teacherOne.setFirstName("John");
        teacherTwo.setId(2L);
        teacherTwo.setFirstName("Robert");
        mockTeachers.add(teacherOne);
        mockTeachers.add(teacherTwo);

        Page<Teacher> mockTeacherPage = new PageImpl<>(mockTeachers);

        when(teacherService.findAll(any(PageRequest.class))).thenReturn(mockTeacherPage);

        mockMvc.perform(get("/getListTeachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Robert")));

        verify(teacherService, times(1)).findAll(any(PageRequest.class));
    }
}