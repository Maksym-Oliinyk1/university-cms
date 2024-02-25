package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.FacultyService;

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
class FacultyRestControllerTest {

    @Mock
    private FacultyService facultyService;

    @InjectMocks
    private FacultyRestController facultyRestController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getFaculties_ShouldReturnListOfFaculties() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(facultyRestController).build();

        List<Faculty> mockFaculties = new ArrayList<>();
        Faculty facultyOne = new Faculty();
        Faculty facultyTwo = new Faculty();
        facultyOne.setId(1L);
        facultyOne.setName("Engineering");
        facultyTwo.setId(2L);
        facultyTwo.setName("Science");
        mockFaculties.add(facultyOne);
        mockFaculties.add(facultyTwo);

        Page<Faculty> mockFacultyPage = new PageImpl<>(mockFaculties);

        when(facultyService.findAll(any(PageRequest.class))).thenReturn(mockFacultyPage);

        mockMvc.perform(get("/getListFaculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Engineering")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Science")));

        verify(facultyService, times(1)).findAll(any(PageRequest.class));
    }
}