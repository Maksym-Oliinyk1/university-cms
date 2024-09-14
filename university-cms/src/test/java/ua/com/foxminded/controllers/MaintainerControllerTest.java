package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.controllers.maintainer.MaintainerGeneralController;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.security.JwtService;
import ua.com.foxminded.service.MaintainerService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(MaintainerGeneralController.class)
class MaintainerControllerTest {

  @MockBean
  private MaintainerService maintainerService;

  private static final Long userId = 1L;
  private static final String token = "validToken";

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
  void showMaintainer_ValidToken_ShouldReturnMaintainerPage() throws Exception {
    Maintainer mockMaintainer = new Maintainer();
    configureSecurity();
    when(maintainerService.findById(userId)).thenReturn(mockMaintainer);

    mockMvc
            .perform(get("/general/maintainer/showMaintainer").param("token", token))
            .andExpect(status().isOk())
            .andExpect(view().name("maintainer"))
            .andExpect(model().attributeExists("maintainer"))
            .andExpect(model().attribute("maintainer", mockMaintainer));

    verify(jwtService, times(1)).extractUserId(token);
    verify(maintainerService, times(1)).findById(userId);
  }

  @Test
  void showUpdateForm_ValidToken_ShouldReturnUpdateFormPage() throws Exception {
    MaintainerDTO mockMaintainerDTO = new MaintainerDTO();
    configureSecurity();

    when(maintainerService.findByIdDTO(userId)).thenReturn(mockMaintainerDTO);

    mockMvc
            .perform(get("/general/maintainer/updateFormMaintainer").param("token", token))
            .andExpect(status().isOk())
            .andExpect(view().name("update-form-maintainer"))
            .andExpect(model().attributeExists("maintainer"))
            .andExpect(model().attribute("maintainer", mockMaintainerDTO));

    verify(jwtService, times(1)).extractUserId(token);
    verify(maintainerService, times(1)).findByIdDTO(userId);
  }

  @Test
  void updateMaintainer_ValidInput_ShouldReturnUpdateSuccessPage() throws Exception {
    Long maintainerId = 1L;

    mockMvc
            .perform(
                    post("/general/maintainer/updateMaintainer/{id}", maintainerId)
                            .param("firstName", "John")
                            .param("lastName", "Doe")
                            .param("gender", "MALE")
                            .param("password", "testPassword")
                            .param("birthDate", "1990-01-01")
                            .param("email", "john.doe@example.com"))
            .andExpect(status().isOk())
            .andExpect(view().name("update-form-maintainer-successful"))
            .andExpect(model().attributeExists("maintainerId"))
            .andExpect(model().attribute("maintainerId", maintainerId));

    verify(authenticationService, times(1)).updateMaintainer(eq(maintainerId), any());
  }

  @Test
  void updateMaintainer_InvalidInput_ShouldReturnUpdateFormPageWithErrors() throws Exception {
    Long maintainerId = 1L;

    mockMvc
            .perform(post("/general/maintainer/updateMaintainer/{id}", maintainerId))
            .andExpect(status().isBadRequest());

    verify(authenticationService, never()).updateMaintainer(eq(maintainerId), any());
  }

  @Test
  void showCreateForm_ShouldReturnCreateFormPage() throws Exception {
    mockMvc
            .perform(get("/general/maintainer/createFormMaintainer"))
            .andExpect(status().isOk())
            .andExpect(view().name("create-form-maintainer"))
            .andExpect(model().attributeExists("maintainer"));
  }

  @Test
  void deleteMaintainer_ValidToken_ShouldReturnDeleteSuccessPage() throws Exception {
    configureSecurity();

    mockMvc
            .perform(post("/general/maintainer/deleteMaintainer").param("token", token))
            .andExpect(status().isOk())
            .andExpect(view().name("delete-form-maintainer-successful"));

    verify(jwtService, times(1)).extractUserId(token);
    verify(maintainerService, times(1)).delete(userId);
  }
}
