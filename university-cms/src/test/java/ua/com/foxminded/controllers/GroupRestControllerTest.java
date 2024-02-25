package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.GroupService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class GroupRestControllerTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupRestController groupRestController;

    private MockMvc mockMvc;

    @Test
    void getGroups_ShouldReturnListOfGroups() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(groupRestController).build();

        List<Group> mockGroups = new ArrayList<>();
        Group groupOne = new Group();
        Group groupTwo = new Group();
        groupOne.setId(1L);
        groupOne.setName("AB-12");
        groupTwo.setId(2L);
        groupTwo.setName("CD-32");
        mockGroups.add(groupOne);
        mockGroups.add(groupTwo);

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
}