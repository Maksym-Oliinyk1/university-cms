package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.entity.Administrator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
class AdminControllerImplIntegrationTest extends BaseIntegrationTest {

    @Test
    void listAdmins() throws Exception {
        MvcResult result =
                mvc.perform(get("/listAdmins"))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(view().name("manage-administrator"))
                        .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        assertTrue(model.containsKey("administrators"));
        assertTrue(model.containsKey("pageNumber"));
        assertTrue(model.containsKey("totalPages"));

        List<Administrator> admins = (List<Administrator>) model.get("administrators");
        int pageNumber = (int) model.get("pageNumber");
        int totalPages = (int) model.get("totalPages");
        assertFalse(admins.isEmpty());
        assertEquals(0, pageNumber);
        assertEquals(2, totalPages);
    }

    @Test
    void showAdministrator() throws Exception {
        Administrator administrator = createAdministrator();
        administratorRepository.save(administrator);

        MvcResult result =
                mvc.perform(get("/showAdmin?id=1"))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(view().name("admin"))
                        .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        assertTrue(model.containsKey("administrator"));

        Administrator adminFromModel = (Administrator) model.get("administrator");

        baseTestForUser(adminFromModel);
    }

    @Test
    void adminAuthorization() throws Exception {
        mvc.perform(get("/adminAuthorization"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("mock-admin-authorization"));
    }

    @Test
    void manageAdmin() throws Exception {
        mvc.perform(get("/manageAdmin"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("manage-administrator"));
    }

    @Test
    void createFormAdmin() throws Exception {
        mvc.perform(get("/createFormAdmin"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-administrator"))
                .andExpect(model().attributeExists("administrator"));
    }

    @Test
    void createAdmin_successful() throws Exception {
        AdministratorDTO administratorDTO = createAdministratorDTO();

        mvc.perform(post("/createAdmin").flashAttr("administratorDTO", administratorDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-administrator-successful"));

        Optional<Administrator> optionalAdministrator = administratorRepository.findById(DEFAULT_ID);
        assertTrue(optionalAdministrator.isPresent());
        baseTestForUser(optionalAdministrator.get());
    }

    @Test
    void showUpdateForm() throws Exception {
        Administrator administrator = createAdministrator();
        administratorRepository.save(administrator);

        mvc.perform(get("/updateFormAdmin/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-administrator"))
                .andExpect(model().attributeExists("administrator"));
    }

    @Test
    void updateAdmin_successful() throws Exception {
        String firstNameUpdate = "Updated";
        Administrator administrator = createAdministrator();
        administratorRepository.save(administrator);

        AdministratorDTO administratorDTO = createAdministratorDTO();
        administratorDTO.setFirstName(firstNameUpdate);

        mvc.perform(post("/updateAdmin/1").flashAttr("administratorDTO", administratorDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-administrator-successful"))
                .andExpect(model().attributeExists("adminId"));

        Optional<Administrator> optionalAdministrator = administratorRepository.findById(DEFAULT_ID);
        assertTrue(optionalAdministrator.isPresent());
        Administrator updatedAdministrator = optionalAdministrator.get();
        assertEquals(updatedAdministrator.getFirstName(), firstNameUpdate);
    }

    @Test
    void deleteAdmin_successful() throws Exception {
        Administrator administrator = createAdministrator();
        administratorRepository.save(administrator);

        mvc.perform(post("/deleteAdmin/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("delete-form-administrator-successful"));

        assertFalse(administratorRepository.findById(DEFAULT_ID).isPresent());
    }
}
