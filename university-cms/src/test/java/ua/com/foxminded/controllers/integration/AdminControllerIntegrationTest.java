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
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.security.JwtService;
import ua.com.foxminded.service.AdministratorService;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class AdminGeneralControllerIntegrationTest extends BaseIntegrationTest {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"));

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private AuthenticationService authenticationService;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    private String generateTokenForAdministrator(Administrator administrator) {
        // Replace this method with actual logic for generating JWT tokens
        // return jwtService.generateToken(administrator.getId());
        return null;
    }

    @Test
    void showAdministrator() throws Exception {
        Administrator administrator = createAdministrator();
        administratorRepository.save(administrator);

        String token = generateTokenForAdministrator(administrator);

        MvcResult result =
                mvc.perform(get("/admin/showAdmin").param("token", token))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(view().name("admin"))
                        .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        assertTrue(model.containsKey("administrator"));

        Administrator adminFromModel = (Administrator) model.get("administrator");

        baseTestForUser(adminFromModel);
    }

    @Test
    void showCreateForm() throws Exception {
        mvc.perform(get("/admin/createFormAdmin"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-administrator"))
                .andExpect(model().attributeExists("administrator"));
    }

    @Test
    void showUpdateForm() throws Exception {
        Administrator administrator = createAdministrator();
        administratorRepository.save(administrator);

        String token = generateTokenForAdministrator(administrator);

        MvcResult result =
                mvc.perform(get("/admin/updateFormAdmin").param("token", token))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(view().name("update-form-administrator"))
                        .andExpect(model().attributeExists("administrator"))
                        .andReturn();

        AdministratorDTO adminDTOFromModel =
                (AdministratorDTO) result.getModelAndView().getModel().get("administrator");
        assertEquals(administrator.getId(), adminDTOFromModel.getId());
        //        baseTestForUser(adminDTOFromModel);
    }

    @Test
    void updateAdmin_successful() throws Exception {
        Administrator administrator = createAdministrator();
        administratorRepository.save(administrator);

        String token = generateTokenForAdministrator(administrator);

        AdministratorDTO administratorDTO = createAdministratorDTO();
        administratorDTO.setId(administrator.getId());
        administratorDTO.setFirstName("Updated");

        mvc.perform(
                        post("/admin/updateAdmin/{id}", administrator.getId())
                                .flashAttr("administratorDTO", administratorDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-administrator-successful"))
                .andExpect(model().attributeExists("adminId"));

        Optional<Administrator> updatedAdministrator =
                administratorRepository.findById(administrator.getId());
        assertTrue(updatedAdministrator.isPresent());
        assertEquals("Updated", updatedAdministrator.get().getFirstName());
    }

    @Test
    void deleteAdmin_successful() throws Exception {
        Administrator administrator = createAdministrator();
        administratorRepository.save(administrator);

        String token = generateTokenForAdministrator(administrator);

        mvc.perform(post("/admin/deleteAdmin").param("token", token))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("delete-form-administrator-successful"));

        assertFalse(administratorRepository.findById(administrator.getId()).isPresent());
    }
}
