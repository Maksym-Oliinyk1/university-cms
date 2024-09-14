package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.controllers.teacher.TeacherGeneralController;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.security.JwtService;
import ua.com.foxminded.service.TeacherService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TeacherGeneralController.class)
class TeacherControllerTest {

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;

  @Test
  void showTeacher_ValidToken_ShouldReturnTeacherPage() throws Exception {
      String token = "validToken";
      Long userId = 1L;
    Teacher mockTeacher = new Teacher();

      when(jwtService.extractUserId(token)).thenReturn(userId);
      when(teacherService.findById(userId)).thenReturn(mockTeacher);

    mockMvc
            .perform(get("/general/teacher/showTeacher").param("token", token))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher"))
            .andExpect(model().attributeExists("teacher"))
            .andExpect(model().attribute("teacher", mockTeacher));

      verify(jwtService, times(1)).extractUserId(token);
      verify(teacherService, times(1)).findById(userId);
  }

  @Test
  void showUpdateForm_ValidToken_ShouldReturnUpdateFormPage() throws Exception {
      String token = "validToken";
      Long userId = 1L;
    TeacherDTO mockTeacherDTO = new TeacherDTO();

      when(jwtService.extractUserId(token)).thenReturn(userId);
      when(teacherService.findByIdDTO(userId)).thenReturn(mockTeacherDTO);

    mockMvc
            .perform(get("/general/teacher/updateFormTeacher").param("token", token))
            .andExpect(status().isOk())
            .andExpect(view().name("update-form-teacher"))
            .andExpect(model().attributeExists("teacher"))
            .andExpect(model().attribute("teacher", mockTeacherDTO));

      verify(jwtService, times(1)).extractUserId(token);
      verify(teacherService, times(1)).findByIdDTO(userId);
  }

  @Test
  void updateTeacher_ValidInput_ShouldReturnUpdateSuccessPage() throws Exception {
    Long teacherId = 1L;

    mockMvc
            .perform(
                    post("/general/teacher/updateTeacher/{id}", teacherId)
                            .param("firstName", "John")
                            .param("lastName", "Doe")
                            .param("gender", "MALE")
                            .param("password", "testPassword")
                            .param("birthDate", "1985-05-15")
                            .param("email", "john.doe@example.com"))
            .andExpect(status().isOk())
            .andExpect(view().name("update-form-teacher-successful"));

      verify(authenticationService, times(1)).updateTeacher(eq(teacherId), any());
  }

  @Test
  void updateTeacher_InvalidInput_ShouldReturnUpdateFormPageWithErrors() throws Exception {
    Long teacherId = 1L;

      mockMvc
              .perform(post("/general/teacher/updateTeacher/{id}", teacherId))
              .andExpect(status().isBadRequest());

      verify(authenticationService, never()).updateTeacher(eq(teacherId), any());
  }

  @Test
  void deleteTeacher_ValidToken_ShouldReturnDeleteSuccessPage() throws Exception {
      String token = "validToken";
      Long userId = 1L;

      when(jwtService.extractUserId(token)).thenReturn(userId);

    mockMvc
            .perform(post("/general/teacher/deleteTeacher").param("token", token))
            .andExpect(status().isOk())
            .andExpect(view().name("delete-form-teacher-successful"));

      verify(jwtService, times(1)).extractUserId(token);
      verify(teacherService, times(1)).delete(userId);
  }
}
