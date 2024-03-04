package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.entity.Maintainer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
class MaintainerControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void maintainerAuthorization() throws Exception {
        mvc.perform(get("/maintainerAuthorization"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("mock-maintainer-authorization"));
    }

    @Test
    void showMaintainer() throws Exception {
        Maintainer maintainer = createMaintainer();
        maintainerRepository.save(maintainer);

        mvc.perform(get("/showMaintainer?id=1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("maintainer"))
                .andExpect(model().attributeExists("maintainer"));
    }

    @Test
    void listMaintainers() throws Exception {
        mvc.perform(get("/listMaintainers"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("manage-maintainer"));
    }

    @Test
    void createFormMaintainer() throws Exception {
        mvc.perform(get("/createFormMaintainer"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-maintainer"))
                .andExpect(model().attributeExists("maintainer"));
    }

    @Test
    void createMaintainer_successful() throws Exception {
        MaintainerDTO maintainerDTO = createMaintainerDTO();

        mvc.perform(post("/createMaintainer")
                        .flashAttr("maintainerDTO", maintainerDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-maintainer-successful"));
    }

    @Test
    void showUpdateForm() throws Exception {
        Maintainer maintainer = createMaintainer();
        maintainerRepository.save(maintainer);

        mvc.perform(get("/updateFormMaintainer/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-maintainer"))
                .andExpect(model().attributeExists("maintainer"));
    }

    @Test
    void updateMaintainer_successful() throws Exception {
        String updatedMaintainerName = "Updatedmaintainer";
        Maintainer maintainer = createMaintainer();
        maintainerRepository.save(maintainer);

        MaintainerDTO maintainerDTO = createMaintainerDTO();
        maintainerDTO.setFirstName(updatedMaintainerName);

        mvc.perform(post("/updateMaintainer/1")
                        .flashAttr("maintainerDTO", maintainerDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-maintainer-successful"))
                .andExpect(model().attributeExists("maintainerId"));

        Optional<Maintainer> optionalMaintainer = maintainerRepository.findById(1L);
        assertTrue(optionalMaintainer.isPresent());
        Maintainer updatedMaintainer = optionalMaintainer.get();
        assertEquals(updatedMaintainer.getFirstName(), updatedMaintainerName);
    }

    @Test
    void deleteMaintainer_successful() throws Exception {
        Maintainer maintainer = createMaintainer();
        maintainerRepository.save(maintainer);

        mvc.perform(post("/deleteMaintainer")
                        .param("id", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("delete-form-maintainer-successful"));

        assertFalse(maintainerRepository.findById(1L).isPresent());
    }
}
