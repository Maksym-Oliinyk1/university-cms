package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.foxminded.entity.Faculty;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
class FacultyControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void listFaculties() throws Exception {
        MvcResult result = mvc.perform(get("/listFaculties?pageNumber=0"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("manage-faculty"))
                .andExpect(model().attributeExists("faculties", "pageNumber", "totalPages"))
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        assertTrue(model.containsKey("faculties"));
        assertTrue(model.containsKey("pageNumber"));
        assertTrue(model.containsKey("totalPages"));

        List<Faculty> faculties = (List<Faculty>) model.get("faculties");
        int pageNumber = (int) model.get("pageNumber");
        int totalPages = (int) model.get("totalPages");
        assertFalse(faculties.isEmpty());
        assertEquals(0, pageNumber);
        assertEquals(2, totalPages);
    }

    @Test
    void showFaculty() throws Exception {
        Faculty faculty = createFaculty();
        facultyRepository.save(faculty);

        mvc.perform(get("/showFaculty?id=1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("faculty"))
                .andExpect(model().attributeExists("faculty"));
    }

    @Test
    void createFormFaculty() throws Exception {
        mvc.perform(get("/createFormFaculty"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-faculty"))
                .andExpect(model().attributeExists("faculty"));
    }

    @Test
    void createFaculty_successful() throws Exception {
        Faculty faculty = createFaculty();

        mvc.perform(post("/createFaculty")
                        .flashAttr("faculty", faculty))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-faculty-successful"));

        Optional<Faculty> optionalSavedFaculty = facultyRepository.findById(1L);
        assertTrue(optionalSavedFaculty.isPresent());
        assertEquals(optionalSavedFaculty.get().getName(), "Faculty of Science");
    }

    @Test
    void showUpdateForm() throws Exception {
        Faculty faculty = createFaculty();
        facultyRepository.save(faculty);

        mvc.perform(get("/updateFormFaculty/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-faculty"))
                .andExpect(model().attributeExists("faculty"));
    }

    @Test
    void updateFaculty_successful() throws Exception {
        String updatedFacultyName = "Updated Faculty";
        Faculty faculty = createFaculty();
        facultyRepository.save(faculty);

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setName(updatedFacultyName);

        mvc.perform(post("/updateFaculty/1")
                        .flashAttr("faculty", updatedFaculty))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-faculty-successful"));

        Optional<Faculty> optionalFaculty = facultyRepository.findById(1L);
        assertTrue(optionalFaculty.isPresent());
        assertEquals(optionalFaculty.get().getName(), updatedFacultyName);
    }

    @Test
    void deleteFaculty_successful() throws Exception {
        Faculty faculty = createFaculty();
        facultyRepository.save(faculty);

        mvc.perform(post("/deleteFaculty/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("delete-form-faculty-successful"));

        assertFalse(facultyRepository.findById(1L).isPresent());
    }
}
