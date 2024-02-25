package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.TeacherService;
import ua.com.foxminded.service.UserMapper;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private TeacherController teacherController;

    private MockMvc mockMvc;

    @Test
    void teacherAuthorization_ShouldReturnTeacherAuthorizationPage() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        mockMvc.perform(get("/teacherAuthorization"))
                .andExpect(status().isOk())
                .andExpect(view().name("mock-teacher-authorization"));
    }

    @Test
    void manageTeacher_ShouldReturnManageTeacherPage() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        mockMvc.perform(get("/manageTeacher"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-teacher"));
    }

    @Test
    void showTeacher_ValidId_ShouldReturnTeacherPage() throws Exception {
        Long teacherId = 1L;
        Teacher mockTeacher = new Teacher();
        when(teacherService.findById(teacherId)).thenReturn(mockTeacher);

        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        mockMvc.perform(get("/showTeacher")
                        .param("id", String.valueOf(teacherId)))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher"))
                .andExpect(model().attributeExists("teacher"))
                .andExpect(model().attribute("teacher", mockTeacher));

        verify(teacherService, times(1)).findById(teacherId);
    }

    @Test
    void listTeacher_ShouldReturnManageTeacherPage() throws Exception {
        Page<Teacher> mockTeacherPage = mock(Page.class);
        when(mockTeacherPage.getContent()).thenReturn(Collections.emptyList());
        when(teacherService.findAll(any())).thenReturn(mockTeacherPage);

        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        mockMvc.perform(get("/listTeachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-teacher"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"));

        verify(teacherService, times(1)).findAll(any());
    }

    @Test
    void createTeacher_ValidInput_ShouldReturnCreateFormTeacherSuccessfulPage() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        mockMvc.perform(post("/createTeacher")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("gender", "MALE")
                        .param("age", "1990-01-01")
                        .param("email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-form-teacher-successful"));

        verify(teacherService, times(1)).save(any());
    }

    @Test
    void createTeacher_InvalidInput_ShouldReturnCreateFormTeacherPageWithErrors() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        mockMvc.perform(post("/createTeacher"))
                .andExpect(status().isBadRequest());

        verify(teacherService, never()).save(any());
    }

    @Test
    void showUpdateForm_ValidId_ShouldReturnUpdateFormTeacherPage() throws Exception {
        Long teacherId = 1L;
        Teacher mockTeacher = new Teacher();
        TeacherDTO mockTeacherDTO = new TeacherDTO();
        when(teacherService.findById(teacherId)).thenReturn(mockTeacher);
        when(userMapper.mapToDto(mockTeacher)).thenReturn(mockTeacherDTO);

        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        mockMvc.perform(get("/updateFormTeacher/{id}", teacherId))
                .andExpect(status().isOk())
                .andExpect(view().name("update-form-teacher"))
                .andExpect(model().attributeExists("teacher"))
                .andExpect(model().attribute("teacher", mockTeacherDTO));

        verify(teacherService, times(1)).findById(teacherId);
    }

    @Test
    void updateTeacher_ValidInput_ShouldReturnUpdateFormTeacherSuccessfulPage() throws Exception {
        Long teacherId = 1L;
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        mockMvc.perform(post("/updateTeacher/{id}", teacherId)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("gender", "MALE")
                        .param("age", "1990-01-01")
                        .param("email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-form-teacher-successful"));

        verify(teacherService, times(1)).update(eq(teacherId), any());
    }

    @Test
    void updateTeacher_InvalidInput_ShouldReturnUpdateFormTeacherPageWithErrors() throws Exception {
        Long teacherId = 1L;
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        mockMvc.perform(post("/updateTeacher/{id}", teacherId))
                .andExpect(status().isBadRequest());

        verify(teacherService, never()).update(eq(teacherId), any());
    }

    @Test
    void deleteTeacher_ValidId_ShouldReturnDeleteFormTeacherSuccessfulPage() throws Exception {
        Long teacherId = 1L;
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        mockMvc.perform(post("/deleteTeacher/{id}", teacherId))
                .andExpect(status().isOk())
                .andExpect(view().name("delete-form-teacher-successful"));

        verify(teacherService, times(1)).delete(teacherId);
    }
}
