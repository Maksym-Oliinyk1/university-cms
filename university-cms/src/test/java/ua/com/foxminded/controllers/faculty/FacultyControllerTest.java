package ua.com.foxminded.controllers.faculty;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.controllers.BaseSecurityTestClass;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.FacultyService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(FacultyController.class)
class FacultyControllerTest extends BaseSecurityTestClass {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;
    @MockBean
    private FacultyService facultyService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void showFaculty_ValidId_ShouldReturnFacultyPage() throws Exception {
        configureSecurity();

        Long facultyId = 1L;
        Faculty mockFaculty = new Faculty();

        when(facultyService.findById(facultyId)).thenReturn(mockFaculty);

        mockMvc
                .perform(get("/general/faculty/showFaculty").param("id", String.valueOf(facultyId)))
                .andExpect(status().isOk())
                .andExpect(view().name("faculty"))
                .andExpect(model().attributeExists("faculty"))
                .andExpect(model().attribute("faculty", mockFaculty));

        verify(facultyService, times(1)).findById(facultyId);
    }

    @Test
    void listFaculties_ValidPageNumber_ShouldReturnManageFacultyPage() throws Exception {
        configureSecurity();

        Page<Faculty> mockPageFaculties =
                new PageImpl<>(
                        List.of(new Faculty()),
                        PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY),
                        1);

        when(facultyService.findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY)))
                .thenReturn(mockPageFaculties);

        mockMvc
                .perform(
                        get("/general/faculty/listFaculties")
                                .param("pageNumber", String.valueOf(DEFAULT_PAGE_NUMBER)))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-faculty"))
                .andExpect(model().attributeExists("faculties"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("faculties", mockPageFaculties.getContent()))
                .andExpect(model().attribute("pageNumber", mockPageFaculties.getNumber()))
                .andExpect(model().attribute("totalPages", mockPageFaculties.getTotalPages()));

        verify(facultyService, times(1))
                .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
    }

    @Test
    void listFacultiesToCourse_ValidPageNumber_ShouldReturnCreateFormCoursePage() throws Exception {
        configureSecurity();

        Page<Faculty> mockPageFaculties =
                new PageImpl<>(
                        List.of(new Faculty()),
                        PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY),
                        1);

        when(facultyService.findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY)))
                .thenReturn(mockPageFaculties);

        mockMvc
                .perform(
                        get("/general/faculty/listFacultiesToCourse")
                                .param("pageNumber", String.valueOf(DEFAULT_PAGE_NUMBER)))
                .andExpect(status().isOk())
                .andExpect(view().name("create-form-course"))
                .andExpect(model().attributeExists("faculties"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("faculties", mockPageFaculties.getContent()))
                .andExpect(model().attribute("pageNumber", mockPageFaculties.getNumber()))
                .andExpect(model().attribute("totalPages", mockPageFaculties.getTotalPages()));

        verify(facultyService, times(1))
                .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
    }
}
