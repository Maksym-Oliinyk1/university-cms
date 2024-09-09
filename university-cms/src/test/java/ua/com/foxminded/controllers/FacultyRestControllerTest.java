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
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.FacultyService;

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
@WebMvcTest(FacultyRestController.class)
class FacultyRestControllerTest {

    @MockBean
    private FacultyService facultyService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getFaculties_ShouldReturnListOfFaculties() throws Exception {
        List<Faculty> mockFaculties =
                Arrays.asList(createFaculty(1L, "Engineering"), createFaculty(2L, "Science"));

        Page<Faculty> mockFacultyPage = new PageImpl<>(mockFaculties);

        when(facultyService.findAll(any(PageRequest.class))).thenReturn(mockFacultyPage);

        mockMvc
                .perform(get("/getListFaculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Engineering")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Science")));

        verify(facultyService, times(1)).findAll(any(PageRequest.class));
    }

    private Faculty createFaculty(Long id, String name) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        return faculty;
    }
}
