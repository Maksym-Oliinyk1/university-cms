package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.controllers.admin.AdminGeneralController;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.security.JwtService;
import ua.com.foxminded.service.AdministratorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AdminGeneralController.class)
class AdminControllerTest {

  @MockBean
  private AdministratorService administratorService;

  @MockBean
  private JwtService jwtService;

  @MockBean
  private AuthenticationService authenticationService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void showAdministrator_ValidToken_ShouldReturnAdminPage() throws Exception {
    String token = "validToken";
    Long adminId = 1L;
    Administrator mockAdministrator = new Administrator();

    when(jwtService.extractUserId(token)).thenReturn(adminId);
    when(administratorService.findById(adminId)).thenReturn(mockAdministrator);

    mockMvc
            .perform(get("/admin/showAdmin").param("token", token))
            .andExpect(status().isOk())
            .andExpect(view().name("admin"))
            .andExpect(model().attributeExists("administrator"))
            .andExpect(model().attribute("administrator", mockAdministrator));

    verify(jwtService, times(1)).extractUserId(token);
    verify(administratorService, times(1)).findById(adminId);
  }

  @Test
  void showCreateForm_ShouldReturnCreateFormPage() throws Exception {
    mockMvc
            .perform(get("/admin/createFormAdmin"))
            .andExpect(status().isOk())
            .andExpect(view().name("create-form-administrator"))
            .andExpect(model().attributeExists("administrator"));
  }

  @Test
  void showUpdateForm_ValidToken_ShouldReturnUpdateFormPage() throws Exception {
    String token = "validToken";
    Long adminId = 1L;
    AdministratorDTO mockAdministratorDTO = new AdministratorDTO();

    when(administratorService.findByIdDTO(adminId)).thenReturn(mockAdministratorDTO);

    mockMvc
            .perform(get("/admin/updateFormAdmin").param("token", token))
            .andExpect(status().isOk())
            .andExpect(view().name("update-form-administrator"))
            .andExpect(model().attributeExists("administrator"))
            .andExpect(model().attribute("administrator", mockAdministratorDTO));

    verify(jwtService, times(1)).extractUserId(token);
    verify(administratorService, times(1)).findByIdDTO(adminId);
  }

  @Test
  void updateAdmin_ValidInput_ShouldReturnUpdateSuccessPage() throws Exception {
    Long adminId = 1L;

    mockMvc
            .perform(
                    post("/admin/updateAdmin/{id}", adminId)
                            .param("firstName", "John")
                            .param("lastName", "Doe")
                            .param("gender", "MALE")
                            .param("password", "testPassword")
                            .param("birthDate", "1990-01-01")
                            .param("email", "john.doe@example.com"))
            .andExpect(status().isOk())
            .andExpect(view().name("update-form-administrator-successful"))
            .andExpect(model().attributeExists("adminId"))
            .andExpect(model().attribute("adminId", adminId));

    verify(authenticationService, times(1)).updateAdmin(eq(adminId), any());
  }

  @Test
  void updateAdmin_InvalidInput_ShouldReturnUpdateFormPageWithErrors() throws Exception {
    Long adminId = 1L;

    mockMvc.perform(post("/admin/updateAdmin/{id}", adminId)).andExpect(status().isBadRequest());

    verify(authenticationService, never()).updateAdmin(eq(adminId), any());
  }

  @Test
  void deleteAdmin_ValidToken_ShouldReturnDeleteSuccessPage() throws Exception {
    String token = "validToken";
    Long adminId = 1L;

    when(jwtService.extractUserId(token)).thenReturn(adminId);

    mockMvc
            .perform(post("/admin/deleteAdmin").param("token", token))
            .andExpect(status().isOk())
            .andExpect(view().name("delete-form-administrator-successful"));

    verify(jwtService, times(1)).extractUserId(token);
    verify(administratorService, times(1)).delete(adminId);
  }
}
