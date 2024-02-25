package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.GroupService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    private MockMvc mockMvc;

    @Test
    void createGroup_ValidInput_ShouldReturnCreateFormGroupSuccessfulPage() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();

        mockMvc.perform(post("/createGroup")
                        .param("name", "AB-12"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-form-group-successful"));

        verify(groupService, times(1)).save(any());
    }

    @Test
    void createGroup_InvalidInput_ShouldReturnCreateFormGroupPageWithErrors() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();

        mockMvc.perform(post("/createGroup"))
                .andExpect(status().isBadRequest());

        verify(groupService, never()).save(any());
    }

    @Test
    void updateGroup_ValidInput_ShouldReturnUpdateFormGroupSuccessfulPage() throws Exception {
        Long groupId = 1L;
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();

        mockMvc.perform(post("/updateGroup/{id}", groupId)
                        .param("name", "BC-23"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-form-group-successful"))
                .andExpect(model().attributeExists("groupId"))
                .andExpect(model().attribute("groupId", groupId));

        verify(groupService, times(1)).update(eq(groupId), any());
    }

    @Test
    void updateGroup_InvalidInput_ShouldReturnUpdateFormGroupPageWithErrors() throws Exception {
        Long groupId = 1L;
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();

        mockMvc.perform(post("/updateGroup/{id}", groupId))
                .andExpect(status().isBadRequest());

        verify(groupService, never()).update(eq(groupId), any());
    }

    @Test
    void manageGroup_ShouldReturnManageGroupPage() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();

        mockMvc.perform(get("/manageGroup"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-group"));
    }

    @Test
    void showGroup_ValidId_ShouldReturnGroupPage() throws Exception {
        Long groupId = 1L;
        Group mockGroup = new Group();
        when(groupService.findById(groupId)).thenReturn(mockGroup);

        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();

        mockMvc.perform(get("/showGroup")
                        .param("id", String.valueOf(groupId)))
                .andExpect(status().isOk())
                .andExpect(view().name("group"))
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attribute("group", mockGroup));

        verify(groupService, times(1)).findById(groupId);
    }

    @Test
    void listGroups_ShouldReturnManageGroupPage() throws Exception {
        Page<Group> mockGroupPage = mock(Page.class);
        when(mockGroupPage.getContent()).thenReturn(Collections.emptyList());
        when(groupService.findAll(any())).thenReturn(mockGroupPage);

        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();

        mockMvc.perform(get("/listGroups"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-group"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"));

        verify(groupService, times(1)).findAll(any());
    }

    @Test
    void deleteGroup_ValidId_ShouldReturnDeleteFormGroupSuccessfulPage() throws Exception {
        Long groupId = 1L;
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();

        mockMvc.perform(post("/deleteGroup/{id}", groupId))
                .andExpect(status().isOk())
                .andExpect(view().name("delete-form-group-successful"));

        verify(groupService, times(1)).delete(groupId);
    }

    @Test
    void attachStudentToGroup_ShouldAttachStudentToGroup() throws Exception {
        Long studentId = 1L;
        Long groupId = 1L;
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();

        mockMvc.perform(post("/attachStudentToGroup")
                        .param("studentId", String.valueOf(studentId))
                        .param("groupId", String.valueOf(groupId)))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-student"));

        verify(groupService, times(1)).attachStudentToGroup(studentId, groupId);
    }

    @Test
    void detachStudentFromGroup_ShouldDetachStudentFromGroup() throws Exception {
        Long studentId = 1L;
        Long groupId = 1L;
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();

        mockMvc.perform(post("/detachStudentFromGroup")
                        .param("studentId", String.valueOf(studentId))
                        .param("groupId", String.valueOf(groupId)))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-group"));

        verify(groupService, times(1)).detachStudentFromGroup(studentId, groupId);
    }
}
