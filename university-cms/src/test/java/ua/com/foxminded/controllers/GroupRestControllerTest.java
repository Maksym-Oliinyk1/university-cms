package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.GroupService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(GroupRestController.class)
class GroupRestControllerTest {

    @MockBean
    private GroupService groupService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getGroups_ShouldReturnListOfGroups() throws Exception {
        List<Group> mockGroups = Arrays.asList(
                createGroup(1L, "AB-12"),
                createGroup(2L, "CD-32")
        );

        Page<Group> mockGroupPage = new PageImpl<>(mockGroups);

        when(groupService.findAll(any(PageRequest.class))).thenReturn(mockGroupPage);

        mockMvc.perform(get("/getListGroups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("AB-12")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("CD-32")));

        verify(groupService, times(1)).findAll(any(PageRequest.class));
    }

    private Group createGroup(Long id, String name) {
        Group group = new Group();
        group.setId(id);
        group.setName(name);
        return group;
    }
}
