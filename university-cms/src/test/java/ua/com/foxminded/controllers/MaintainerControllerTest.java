package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.service.MaintainerService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MaintainerController.class)
class MaintainerControllerTest {

    @MockBean
    private MaintainerService maintainerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void maintainerAuthorization_ShouldReturnMaintainerAuthorizationPage() throws Exception {
        mockMvc.perform(get("/maintainerAuthorization"))
                .andExpect(status().isOk())
                .andExpect(view().name("mock-maintainer-authorization"));
    }

    @Test
    void showMaintainer_ValidId_ShouldReturnMaintainerPage() throws Exception {
        Long maintainerId = 1L;
        Maintainer mockMaintainer = new Maintainer();
        when(maintainerService.findById(maintainerId)).thenReturn(mockMaintainer);

        mockMvc.perform(get("/showMaintainer")
                        .param("id", String.valueOf(maintainerId)))
                .andExpect(status().isOk())
                .andExpect(view().name("maintainer"))
                .andExpect(model().attributeExists("maintainer"))
                .andExpect(model().attribute("maintainer", mockMaintainer));

        verify(maintainerService, times(1)).findById(maintainerId);
    }

    @Test
    void listMaintainers_ShouldReturnListMaintainersPage() throws Exception {
        Page<Maintainer> mockMaintainerPage = mock(Page.class);
        when(mockMaintainerPage.getContent()).thenReturn(Collections.emptyList());
        when(maintainerService.findAll(any())).thenReturn(mockMaintainerPage);

        mockMvc.perform(get("/listMaintainers"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-maintainer"))
                .andExpect(model().attributeExists("maintainers"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"));

        verify(maintainerService, times(1)).findAll(any());
    }

    @Test
    void createMaintainer_ValidInput_ShouldReturnCreateFormMaintainerSuccessfulPage() throws Exception {
        mockMvc.perform(post("/createMaintainer")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("gender", "MALE")
                        .param("birthDate", "1990-01-01")
                        .param("email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-form-maintainer-successful"));

        verify(maintainerService, times(1)).save(any());
    }

    @Test
    void createMaintainer_InvalidInput_ShouldReturnCreateFormMaintainerPageWithErrors() throws Exception {
        mockMvc.perform(post("/createMaintainer"))
                .andExpect(status().isBadRequest());

        verify(maintainerService, never()).save(any());
    }

    @Test
    void showUpdateForm_ValidId_ShouldReturnUpdateFormMaintainerPage() throws Exception {
        Long maintainerId = 1L;
        MaintainerDTO mockMaintainerDTO = new MaintainerDTO();

        when(maintainerService.findByIdDTO(maintainerId)).thenReturn(mockMaintainerDTO);

        mockMvc.perform(get("/updateFormMaintainer/{id}", maintainerId))
                .andExpect(status().isOk())
                .andExpect(view().name("update-form-maintainer"))
                .andExpect(model().attributeExists("maintainer"))
                .andExpect(model().attribute("maintainer", mockMaintainerDTO));

        verify(maintainerService, times(1)).findByIdDTO(maintainerId);
    }


    @Test
    void updateMaintainer_ValidInput_ShouldReturnUpdateFormMaintainerSuccessfulPage() throws Exception {
        Long maintainerId = 1L;

        mockMvc.perform(post("/updateMaintainer/{id}", maintainerId)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("gender", "MALE")
                        .param("birthDate", "1990-01-01")
                        .param("email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-form-maintainer-successful"))
                .andExpect(model().attributeExists("maintainerId"))
                .andExpect(model().attribute("maintainerId", maintainerId));

        verify(maintainerService, times(1)).update(eq(maintainerId), any());
    }

    @Test
    void updateMaintainer_InvalidInput_ShouldReturnUpdateFormMaintainerPageWithErrors() throws Exception {
        Long maintainerId = 1L;

        mockMvc.perform(post("/updateMaintainer/{id}", maintainerId))
                .andExpect(status().isBadRequest());

        verify(maintainerService, never()).update(eq(maintainerId), any());
    }

    @Test
    void deleteMaintainer_ValidId_ShouldReturnDeleteFormMaintainerSuccessfulPage() throws Exception {
        Long maintainerId = 1L;

        mockMvc.perform(post("/deleteMaintainer")
                        .param("id", String.valueOf(maintainerId)))
                .andExpect(status().isOk())
                .andExpect(view().name("delete-form-maintainer-successful"));

        verify(maintainerService, times(1)).delete(maintainerId);
    }
}
