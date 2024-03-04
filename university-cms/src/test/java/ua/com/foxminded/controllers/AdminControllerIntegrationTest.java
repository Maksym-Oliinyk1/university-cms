package ua.com.foxminded.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.entity.Administrator;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Testcontainers
@SpringBootTest
class AdminControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void listAdmins() throws Exception {
        mvc.perform(get("/listAdmins"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("manage-administrator"));
    }

    @Test
    void showAdministrator() throws Exception {
        Administrator administrator = createAdministrator();
        administratorRepository.save(administrator);

        mvc.perform(get("/showAdmin?id=1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("admin"))
                .andExpect(content().string(containsString("<h2>John Doe</h2>")));
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

        mvc.perform(post("/createAdmin")
                        .flashAttr("administratorDTO", administratorDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-administrator-successful"));
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

        mvc.perform(post("/updateAdmin/1")
                        .flashAttr("administratorDTO", administratorDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-administrator-successful"))
                .andExpect(model().attributeExists("adminId"));

        Optional<Administrator> optionalAdministrator = administratorRepository.findById(1L);
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

        assertFalse(administratorRepository.findById(1L).isPresent());
    }

}
