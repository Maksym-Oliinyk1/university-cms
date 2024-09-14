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
import ua.com.foxminded.controllers.lecture.LectureController;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.LectureService;
import ua.com.foxminded.service.TeacherService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(LectureController.class)
class LectureControllerTest extends BaseSecurityTestClass {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;
    @MockBean
    private LectureService lectureService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private TeacherService teacherService;
    @Autowired
    private MockMvc mockMvc;

  @Test
  void showCourse_ValidId_ShouldReturnCoursePage() throws Exception {
      configureSecurity();

      Long courseId = 1L;
      Course mockCourse = new Course();

      when(courseService.findById(courseId)).thenReturn(mockCourse);

    mockMvc
            .perform(get("/general/lecture/showCourse").param("id", String.valueOf(courseId)))
            .andExpect(status().isOk())
            .andExpect(view().name("course"))
            .andExpect(model().attributeExists("course"))
            .andExpect(model().attribute("course", mockCourse));

      verify(courseService, times(1)).findById(courseId);
  }

  @Test
  void showTeacher_ValidId_ShouldReturnTeacherPage() throws Exception {
      configureSecurity();

      Long teacherId = 1L;
      Teacher mockTeacher = new Teacher();

      when(teacherService.findById(teacherId)).thenReturn(mockTeacher);

    mockMvc
            .perform(get("/general/lecture/showTeacher/{id}", teacherId))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher"))
            .andExpect(model().attributeExists("teacher"))
            .andExpect(model().attribute("teacher", mockTeacher));

      verify(teacherService, times(1)).findById(teacherId);
  }

  @Test
  void listLectures_ValidPageNumber_ShouldReturnManageLecturePage() throws Exception {
      configureSecurity();

      Page<Lecture> mockPageLectures =
              new PageImpl<>(
                      List.of(new Lecture()),
                      PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY),
                      1);

      when(lectureService.findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY)))
              .thenReturn(mockPageLectures);

    mockMvc
            .perform(
                    get("/general/lecture/listLectures")
                            .param("pageNumber", String.valueOf(DEFAULT_PAGE_NUMBER)))
            .andExpect(status().isOk())
            .andExpect(view().name("manage-lecture"))
            .andExpect(model().attributeExists("lectures"))
            .andExpect(model().attributeExists("pageNumber"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("lectures", mockPageLectures.getContent()))
            .andExpect(model().attribute("pageNumber", mockPageLectures.getNumber()))
            .andExpect(model().attribute("totalPages", mockPageLectures.getTotalPages()));

      verify(lectureService, times(1))
              .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
  }

  @Test
  void listLecturesByCourseId_ValidCourseId_ShouldReturnCoursePage() throws Exception {
      configureSecurity();

    Long courseId = 1L;
      Page<Lecture> mockPageLectures =
              new PageImpl<>(
                      List.of(new Lecture()),
                      PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY),
                      1);
    Course mockCourse = new Course();

      when(lectureService.findAllByCourse(
              courseId, PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY)))
              .thenReturn(mockPageLectures);
    when(courseService.findById(courseId)).thenReturn(mockCourse);

    mockMvc
            .perform(
                    get("/general/lecture/listLecturesByCourse/{courseId}", courseId)
                            .param("pageNumber", String.valueOf(DEFAULT_PAGE_NUMBER)))
            .andExpect(status().isOk())
            .andExpect(view().name("course"))
            .andExpect(model().attributeExists("course"))
            .andExpect(model().attributeExists("lectures"))
            .andExpect(model().attributeExists("pageNumber"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("course", mockCourse))
            .andExpect(model().attribute("lectures", mockPageLectures.getContent()))
            .andExpect(model().attribute("pageNumber", mockPageLectures.getNumber()))
            .andExpect(model().attribute("totalPages", mockPageLectures.getTotalPages()));

      verify(lectureService, times(1))
              .findAllByCourse(
                      courseId, PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
    verify(courseService, times(1)).findById(courseId);
  }

  @Test
  void listLecturesByGroupId_ValidGroupId_ShouldReturnGroupPage() throws Exception {
      configureSecurity();

    Long groupId = 1L;
      Page<Lecture> mockPageLectures =
              new PageImpl<>(
                      List.of(new Lecture()),
                      PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY),
                      1);
    Group mockGroup = new Group();

      when(lectureService.findAllByGroup(
              groupId, PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY)))
              .thenReturn(mockPageLectures);
    when(groupService.findById(groupId)).thenReturn(mockGroup);

    mockMvc
            .perform(
                    get("/general/lecture/listLecturesByGroup/{groupId}", groupId)
                            .param("pageNumber", String.valueOf(DEFAULT_PAGE_NUMBER)))
            .andExpect(status().isOk())
            .andExpect(view().name("group"))
            .andExpect(model().attributeExists("group"))
            .andExpect(model().attributeExists("lectures"))
            .andExpect(model().attributeExists("pageNumber"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("group", mockGroup))
            .andExpect(model().attribute("lectures", mockPageLectures.getContent()))
            .andExpect(model().attribute("pageNumber", mockPageLectures.getNumber()))
            .andExpect(model().attribute("totalPages", mockPageLectures.getTotalPages()));

      verify(lectureService, times(1))
              .findAllByGroup(
                      groupId, PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
    verify(groupService, times(1)).findById(groupId);
  }
}
