package ua.com.foxminded.controllers.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.entity.Maintainer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class MaintainerControllerIntegrationTest extends BaseIntegrationTest {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"));

    @Autowired
    MockMvc mvc;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

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

        MvcResult result =
                mvc.perform(get("/showMaintainer?id=1"))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(view().name("maintainer"))
                        .andExpect(model().attributeExists("maintainer"))
                        .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        assertTrue(model.containsKey("maintainer"));

        Maintainer maintainerFromModel = (Maintainer) model.get("maintainer");

        baseTestForUser(maintainerFromModel);
    }

    @Test
    void listMaintainers() throws Exception {
        MvcResult result =
                mvc.perform(get("/listMaintainers"))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(view().name("manage-maintainer"))
                        .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        assertTrue(model.containsKey("maintainers"));
        assertTrue(model.containsKey("pageNumber"));
        assertTrue(model.containsKey("totalPages"));

        List<Maintainer> maintainers = (List<Maintainer>) model.get("maintainers");
        int pageNumber = (int) model.get("pageNumber");
        int totalPages = (int) model.get("totalPages");
        assertFalse(maintainers.isEmpty());
        assertEquals(0, pageNumber);
        assertEquals(1, totalPages);
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

        mvc.perform(post("/createMaintainer").flashAttr("maintainerDTO", maintainerDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-maintainer-successful"));

        Optional<Maintainer> optionalMaintainer = maintainerRepository.findById(DEFAULT_ID);
        assertTrue(optionalMaintainer.isPresent());
        Maintainer maintainerFromModel = optionalMaintainer.get();
        baseTestForUser(maintainerFromModel);
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

        mvc.perform(post("/updateMaintainer/1").flashAttr("maintainerDTO", maintainerDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-maintainer-successful"))
                .andExpect(model().attributeExists("maintainerId"));

        Optional<Maintainer> optionalMaintainer = maintainerRepository.findById(DEFAULT_ID);
        assertTrue(optionalMaintainer.isPresent());
        Maintainer updatedMaintainer = optionalMaintainer.get();
        assertEquals(updatedMaintainer.getFirstName(), updatedMaintainerName);
    }

    @Test
    void deleteMaintainer_successful() throws Exception {
        Maintainer maintainer = createMaintainer();
        maintainerRepository.save(maintainer);

        mvc.perform(post("/deleteMaintainer").param("id", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("delete-form-maintainer-successful"));

        assertFalse(maintainerRepository.findById(DEFAULT_ID).isPresent());
    }
}
