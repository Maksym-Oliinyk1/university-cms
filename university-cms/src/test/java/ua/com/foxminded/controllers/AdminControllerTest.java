package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.controllers.admin.AdminGeneralController;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.service.AdministratorService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AdminGeneralController.class)
class AdminControllerTest {

  @MockBean
  private AdministratorService administratorService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void adminAuthorization_ShouldReturnAdminAuthorizationPage() throws Exception {
    mockMvc
            .perform(get("/adminAuthorization"))
            .andExpect(status().isOk())
            .andExpect(view().name("mock-admin-authorization"));
  }

  @Test
  void manageAdmin_ShouldReturnManageAdministratorPage() throws Exception {
    mockMvc
            .perform(get("/manageAdmin"))
            .andExpect(status().isOk())
            .andExpect(view().name("manage-administrator"));
  }

  @Test
  void showAdministrator_ValidId_ShouldReturnAdminPage() throws Exception {
    Long adminId = 1L;
    Administrator mockAdministrator = new Administrator();
    when(administratorService.findById(adminId)).thenReturn(mockAdministrator);

    mockMvc
            .perform(get("/showAdmin").param("id", String.valueOf(adminId)))
            .andExpect(status().isOk())
            .andExpect(view().name("admin"))
            .andExpect(model().attributeExists("administrator"))
            .andExpect(model().attribute("administrator", mockAdministrator));

    verify(administratorService, times(1)).findById(adminId);
  }

  @Test
  void listAdministrators_ShouldReturnManageAdministratorPage() throws Exception {
    Page<Administrator> mockAdminPage = mock(Page.class);
    when(mockAdminPage.getContent()).thenReturn(Collections.emptyList());
    when(administratorService.findAll(any())).thenReturn(mockAdminPage);

    mockMvc
            .perform(get("/listAdmins"))
            .andExpect(status().isOk())
            .andExpect(view().name("manage-administrator"))
            .andExpect(model().attributeExists("administrators"))
            .andExpect(model().attributeExists("pageNumber"))
            .andExpect(model().attributeExists("totalPages"));

    verify(administratorService, times(1)).findAll(any());
  }

  @Test
  void createAdmin_ValidInput_ShouldReturnCreateFormAdministratorSuccessfulPage() throws Exception {
    mockMvc
            .perform(
                    post("/createAdmin")
                            .param("firstName", "John")
                            .param("lastName", "Doe")
                            .param("gender", "MALE")
                            .param("birthDate", "1990-01-01")
                            .param("email", "john.doe@example.com"))
            .andExpect(status().isOk())
            .andExpect(view().name("create-form-administrator-successful"));

    verify(administratorService, times(1)).save(any());
  }

  @Test
  void createAdmin_InvalidInput_ShouldReturnCreateFormAdministratorPageWithErrors()
          throws Exception {
    mockMvc.perform(post("/createAdmin")).andExpect(status().isBadRequest());

    verify(administratorService, never()).save(any());
  }

  @Test
  void showUpdateForm_ValidId_ShouldReturnUpdateFormAdministratorPage() throws Exception {
    Long adminId = 1L;
    AdministratorDTO mockAdministratorDTO = new AdministratorDTO();

    when(administratorService.findByIdDTO(adminId)).thenReturn(mockAdministratorDTO);

    mockMvc
            .perform(get("/updateFormAdmin/{id}", adminId))
            .andExpect(status().isOk())
            .andExpect(view().name("update-form-administrator"))
            .andExpect(model().attributeExists("administrator"))
            .andExpect(model().attribute("administrator", mockAdministratorDTO));

    verify(administratorService, times(1)).findByIdDTO(adminId);
  }

  @Test
  void updateAdmin_ValidInput_ShouldReturnUpdateFormAdministratorSuccessfulPage() throws Exception {
    Long adminId = 1L;

    mockMvc
            .perform(
                    post("/updateAdmin/{id}", adminId)
                            .param("firstName", "John")
                            .param("lastName", "Doe")
                            .param("gender", "MALE")
                            .param("birthDate", "1990-01-01")
                            .param("email", "john.doe@example.com"))
            .andExpect(status().isOk())
            .andExpect(view().name("update-form-administrator-successful"))
            .andExpect(model().attributeExists("adminId"))
            .andExpect(model().attribute("adminId", adminId));

    verify(administratorService, times(1)).update(eq(adminId), any());
  }

  @Test
  void updateAdmin_InvalidInput_ShouldReturnUpdateFormAdministratorPageWithErrors()
          throws Exception {
    Long adminId = 1L;

    mockMvc.perform(post("/updateAdmin/{id}", adminId)).andExpect(status().isBadRequest());

    verify(administratorService, never()).update(eq(adminId), any());
  }

  @Test
  void deleteAdmin_ValidId_ShouldReturnDeleteFormAdministratorSuccessfulPage() throws Exception {
    Long adminId = 1L;

    mockMvc
            .perform(post("/deleteAdmin/{id}", adminId))
            .andExpect(status().isOk())
            .andExpect(view().name("delete-form-administrator-successful"));

    verify(administratorService, times(1)).delete(adminId);
  }
}
