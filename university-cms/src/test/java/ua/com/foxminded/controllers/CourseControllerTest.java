package ua.com.foxminded.controllers;

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
import ua.com.foxminded.controllers.course.CourseController;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.FacultyService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CourseController.class)
class CourseControllerTest extends BaseSecurityTestClass {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;
    @MockBean
    private CourseService courseService;
    @MockBean
    private FacultyService facultyService;
    @Autowired
    private MockMvc mockMvc;

  @Test
  void listCourses_ValidPageNumber_ShouldReturnManageCoursePage() throws Exception {
      configureSecurity();

      Page<Course> mockPageCourses =
              new PageImpl<>(
                      List.of(new Course()),
                      PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY),
                      1);

      when(courseService.findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY)))
              .thenReturn(mockPageCourses);

    mockMvc
            .perform(
                    get("/general/course/listCourses")
                            .param("pageNumber", String.valueOf(DEFAULT_PAGE_NUMBER)))
            .andExpect(status().isOk())
            .andExpect(view().name("manage-course"))
            .andExpect(model().attributeExists("courses"))
            .andExpect(model().attributeExists("pageNumber"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("courses", mockPageCourses.getContent()))
            .andExpect(model().attribute("pageNumber", mockPageCourses.getNumber()))
            .andExpect(model().attribute("totalPages", mockPageCourses.getTotalPages()));

      verify(courseService, times(1))
              .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
  }

  @Test
  void listCoursesToLecture_ValidPageNumber_ShouldReturnCreateFormLecturePage() throws Exception {
      configureSecurity();

      Page<Course> mockPageCourses =
              new PageImpl<>(
                      List.of(new Course()),
                      PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY),
                      1);

      when(courseService.findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY)))
              .thenReturn(mockPageCourses);

    mockMvc
            .perform(
                    get("/general/course/listCoursesToLecture")
                            .param("pageNumber", String.valueOf(DEFAULT_PAGE_NUMBER)))
            .andExpect(status().isOk())
            .andExpect(view().name("create-form-lecture"))
            .andExpect(model().attributeExists("courses"))
            .andExpect(model().attributeExists("pageNumber"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("courses", mockPageCourses.getContent()))
            .andExpect(model().attribute("pageNumber", mockPageCourses.getNumber()))
            .andExpect(model().attribute("totalPages", mockPageCourses.getTotalPages()));

      verify(courseService, times(1))
              .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
  }

  @Test
  void listCoursesByFacultyId_ValidFacultyId_ShouldReturnFacultyPage() throws Exception {
      configureSecurity();

    Long facultyId = 1L;
      Page<Course> mockPageCourses =
              new PageImpl<>(
                      List.of(new Course()),
                      PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY),
                      1);
    Faculty mockFaculty = new Faculty();

      when(courseService.findAllOfFaculty(
              facultyId, PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY)))
              .thenReturn(mockPageCourses);
    when(facultyService.findById(facultyId)).thenReturn(mockFaculty);

    mockMvc
            .perform(
                    get("/general/course/listCoursesByFaculty/{facultyId}", facultyId)
                            .param("pageNumber", String.valueOf(DEFAULT_PAGE_NUMBER)))
            .andExpect(status().isOk())
            .andExpect(view().name("faculty"))
            .andExpect(model().attributeExists("courses"))
            .andExpect(model().attributeExists("pageNumber"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("faculty"))
            .andExpect(model().attribute("courses", mockPageCourses.getContent()))
            .andExpect(model().attribute("pageNumber", mockPageCourses.getNumber()))
            .andExpect(model().attribute("totalPages", mockPageCourses.getTotalPages()))
            .andExpect(model().attribute("faculty", mockFaculty));

      verify(courseService, times(1))
              .findAllOfFaculty(
                      facultyId, PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
    verify(facultyService, times(1)).findById(facultyId);
  }
}
