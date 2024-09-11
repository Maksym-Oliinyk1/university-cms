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
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;

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
class GroupControllerIntegrationTest extends BaseIntegrationTest {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"));
    @Autowired
    private MockMvc mvc;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @Test
    void listGroups() throws Exception {
        MvcResult result =
                mvc.perform(get("/listGroups"))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(view().name("manage-group"))
                        .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        assertTrue(model.containsKey("groups"));
        assertTrue(model.containsKey("pageNumber"));
        assertTrue(model.containsKey("totalPages"));

        List<Group> groups = (List<Group>) model.get("groups");
        int pageNumber = (int) model.get("pageNumber");
        int totalPages = (int) model.get("totalPages");
        assertFalse(groups.isEmpty());
        assertEquals(0, pageNumber);
        assertEquals(2, totalPages);
    }

    @Test
    void showGroup() throws Exception {
        Group group = createGroup();
        groupRepository.save(group);

        mvc.perform(get("/showGroup?id=1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("group"))
                .andExpect(model().attributeExists("group"));
    }

    @Test
    void listGroupsByLectureId() throws Exception {
        Lecture lecture = createLecture();
        lectureRepository.save(lecture);

        mvc.perform(get("/listGroupsByLecture/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("lecture"))
                .andExpect(model().attributeExists("lecture", "groups", "pageNumber", "totalPages"));
    }

    @Test
    void createFormGroup() throws Exception {
        mvc.perform(get("/createFormGroup"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-group"))
                .andExpect(model().attributeExists("group"));
    }

    @Test
    void createGroup_successful() throws Exception {
        Group group = createGroup();

        mvc.perform(post("/createGroup").flashAttr("group", group))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-group-successful"));

        Optional<Group> optionalSavedGroup = groupRepository.findById(1L);
        assertTrue(optionalSavedGroup.isPresent());
        assertEquals("AB-12", optionalSavedGroup.get().getName());
    }

    @Test
    void showUpdateForm() throws Exception {
        Group group = createGroup();
        groupRepository.save(group);

        mvc.perform(get("/updateFormGroup/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-group"))
                .andExpect(model().attributeExists("group"));
    }

    @Test
    void updateGroup_successful() throws Exception {
        String updatedGroupName = "CD-32";
        Group group = createGroup();
        groupRepository.save(group);

        Group updatedGroup = new Group();
        updatedGroup.setName(updatedGroupName);

        mvc.perform(post("/updateGroup/1").flashAttr("group", updatedGroup))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-group-successful"));

        Optional<Group> optionalGroup = groupRepository.findById(1L);
        assertTrue(optionalGroup.isPresent());
        assertEquals(optionalGroup.get().getName(), updatedGroupName);
    }

    @Test
    void deleteGroup_successful() throws Exception {
        Group group = createGroup();
        groupRepository.save(group);

        mvc.perform(post("/deleteGroup/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("delete-form-group-successful"));

        assertFalse(groupRepository.findById(1L).isPresent());
    }
}
