package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Testcontainers
@SpringBootTest
class GroupControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void listGroups() throws Exception {
        mvc.perform(get("/listGroups"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("manage-group"));
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

        mvc.perform(post("/createGroup")
                        .flashAttr("group", group))
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

        mvc.perform(post("/updateGroup/1")
                        .flashAttr("group", updatedGroup))
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
