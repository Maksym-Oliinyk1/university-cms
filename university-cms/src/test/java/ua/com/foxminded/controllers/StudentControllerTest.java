package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.controllers.student.StudentGeneralController;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.security.JwtService;
import ua.com.foxminded.service.StudentService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(StudentGeneralController.class)
class StudentControllerTest {

  @MockBean
  private StudentService studentService;

  private static final String token = "validToken";
  private static final Long userId = 1L;

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private JwtService jwtService;
  @MockBean
  private AuthenticationService authenticationService;

  private void configureSecurity() {
    when(jwtService.extractUserId(token)).thenReturn(userId);
  }

  @Test
  void showUpdateForm_ValidToken_ShouldReturnUpdateFormPage() throws Exception {
    configureSecurity();

    StudentDTO mockStudentDTO = new StudentDTO();

    when(studentService.findByIdDTO(userId)).thenReturn(mockStudentDTO);

    mockMvc
            .perform(get("/general/student/updateFormStudent").param("token", token))
            .andExpect(status().isOk())
            .andExpect(view().name("update-form-student"))
            .andExpect(model().attributeExists("student"))
            .andExpect(model().attribute("student", mockStudentDTO));

    verify(jwtService, times(1)).extractUserId(token);
    verify(studentService, times(1)).findByIdDTO(userId);
  }

  @Test
  void updateStudent_ValidInput_ShouldReturnUpdateSuccessPage() throws Exception {

    mockMvc
            .perform(
                    post("/general/student/updateStudent/{id}", userId)
                            .param("firstName", "John")
                            .param("lastName", "Doe")
                            .param("gender", "MALE")
                            .param("password", "testPassword")
                            .param("birthDate", "1990-01-01")
                            .param("email", "john.doe@example.com"))
            .andExpect(status().isOk())
            .andExpect(view().name("update-form-student-successful"));

    verify(authenticationService, times(1)).updateStudent(eq(userId), any());
  }

  @Test
  void updateStudent_InvalidInput_ShouldReturnUpdateFormPageWithErrors() throws Exception {

    mockMvc
            .perform(post("/general/student/updateStudent/{id}", userId))
            .andExpect(status().isBadRequest());

    verify(authenticationService, never()).updateStudent(eq(userId), any());
  }

  @Test
  void deleteStudent_ValidToken_ShouldReturnDeleteSuccessPage() throws Exception {
    configureSecurity();

    mockMvc
            .perform(post("/general/student/deleteStudent").param("token", token))
            .andExpect(status().isOk())
            .andExpect(view().name("delete-form-student-successful"));

    verify(jwtService, times(1)).extractUserId(token);
    verify(studentService, times(1)).delete(userId);
  }
}
