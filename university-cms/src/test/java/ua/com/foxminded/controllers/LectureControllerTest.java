package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.LectureService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(LectureController.class)
class LectureControllerTest {

    @MockBean
    private LectureService lectureService;

    @MockBean
    private CourseService courseService;

    @MockBean
    private GroupService groupService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createLecture_ValidInput_ShouldReturnCreateFormLectureSuccessfulPage() throws Exception {
        mockMvc.perform(post("/createLecture")
                        .param("course.id", "1")
                        .param("teacher.id", "1")
                        .param("name", "LectureName")
                        .param("description", "Valid Lecture Description.Valid Lecture Description")
                        .param("date", "2024-02-24T12:00"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-form-lecture-successful"));

        verify(lectureService, times(1)).save(any());
    }

    @Test
    void createLecture_InvalidInput_ShouldReturnCreateFormLecturePageWithErrors() throws Exception {
        mockMvc.perform(post("/createLecture"))
                .andExpect(status().isBadRequest());

        verify(lectureService, never()).save(any());
    }

    @Test
    void updateLecture_ValidInput_ShouldReturnUpdateFormLectureSuccessfulPage() throws Exception {
        Long lectureId = 1L;
        mockMvc.perform(post("/updateLecture/{id}", lectureId)
                        .param("course.id", "1")
                        .param("teacher.id", "1")
                        .param("name", "UpdatedLecture")
                        .param("description", "Updated description between 50 and 2000 characters.")
                        .param("date", "2023-01-01T11:00:00"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-form-lecture-successful"))
                .andExpect(model().attributeExists("lectureId"))
                .andExpect(model().attribute("lectureId", lectureId));

        verify(lectureService, times(1)).update(eq(lectureId), any());
    }

    @Test
    void updateLecture_InvalidInput_ShouldReturnUpdateFormLecturePageWithErrors() throws Exception {
        Long lectureId = 1L;

        mockMvc.perform(post("/updateLecture/{id}", lectureId))
                .andExpect(status().isBadRequest());

        verify(lectureService, never()).update(eq(lectureId), any());
    }

    @Test
    void showLecture_ValidId_ShouldReturnLecturePage() throws Exception {
        Long lectureId = 1L;
        Lecture mockLecture = createMockLecture();
        when(lectureService.findById(lectureId)).thenReturn(mockLecture);

        mockMvc.perform(get("/showLecture")
                        .param("id", String.valueOf(lectureId)))
                .andExpect(status().isOk())
                .andExpect(view().name("lecture"))
                .andExpect(model().attributeExists("lecture"))
                .andExpect(model().attribute("lecture", mockLecture));

        verify(lectureService, times(1)).findById(lectureId);
    }

    @Test
    void listLectures_ShouldReturnManageLecturePage() throws Exception {
        Page<Lecture> mockLecturePage = mock(Page.class);
        when(mockLecturePage.getContent()).thenReturn(Collections.emptyList());
        when(lectureService.findAll(any())).thenReturn(mockLecturePage);

        mockMvc.perform(get("/listLectures"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-lecture"))
                .andExpect(model().attributeExists("lectures"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"));

        verify(lectureService, times(1)).findAll(any());
    }

    @Test
    void listLecturesByCourseId_ShouldReturnCoursePageWithLectures() throws Exception {
        Long courseId = 1L;
        Page<Lecture> mockLecturePage = mock(Page.class);
        Course mockCourse = new Course();
        when(mockLecturePage.getContent()).thenReturn(Collections.emptyList());
        when(lectureService.findAllByCourse(courseId, PageRequest.of(0, 10))).thenReturn(mockLecturePage);
        when(courseService.findById(courseId)).thenReturn(mockCourse);

        mockMvc.perform(get("/listLecturesByCourse/{courseId}", courseId))
                .andExpect(status().isOk())
                .andExpect(view().name("course"))
                .andExpect(model().attributeExists("lectures"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attribute("course", mockCourse));

        verify(lectureService, times(1)).findAllByCourse(courseId, PageRequest.of(0, 10));
        verify(courseService, times(1)).findById(courseId);
    }

    @Test
    void listLecturesByGroupId_ShouldReturnGroupPageWithLectures() throws Exception {
        Long groupId = 1L;
        Page<Lecture> mockLecturePage = mock(Page.class);
        Group mockGroup = new Group();
        when(mockLecturePage.getContent()).thenReturn(Collections.emptyList());
        when(lectureService.findAllByGroup(groupId, PageRequest.of(0, 10))).thenReturn(mockLecturePage);
        when(groupService.findById(groupId)).thenReturn(mockGroup);

        mockMvc.perform(get("/listLecturesByGroup/{groupId}", groupId))
                .andExpect(status().isOk())
                .andExpect(view().name("group"))
                .andExpect(model().attributeExists("lectures"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attribute("group", mockGroup));

        verify(lectureService, times(1)).findAllByGroup(groupId, PageRequest.of(0, 10));
        verify(groupService, times(1)).findById(groupId);
    }

    @Test
    void attachGroupToLecture_ShouldReturnOkResponse() throws Exception {
        Long groupId = 1L;
        Long lectureId = 1L;

        mockMvc.perform(post("/attachGroupToLecture")
                        .param("groupId", String.valueOf(groupId))
                        .param("lectureId", String.valueOf(lectureId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Group attached to lecture successfully"));

        verify(lectureService, times(1)).attachGroupToLecture(groupId, lectureId);
    }

    @Test
    void detachGroupFromLecture_ShouldReturnOkResponse() throws Exception {
        Long groupId = 1L;
        Long lectureId = 1L;

        mockMvc.perform(post("/detachGroupFromLecture")
                        .param("groupId", String.valueOf(groupId))
                        .param("lectureId", String.valueOf(lectureId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Group detached from lecture successfully"));

        verify(lectureService, times(1)).detachGroupFromLecture(groupId, lectureId);
    }

    @Test
    void createLecture_InvalidInput_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/createLecture"))
                .andExpect(status().isBadRequest());

        verify(lectureService, never()).save(any());
    }

    @Test
    void updateLecture_InvalidInput_ShouldReturnBadRequest() throws Exception {
        Long lectureId = 1L;

        mockMvc.perform(post("/updateLecture/{id}", lectureId))
                .andExpect(status().isBadRequest());

        verify(lectureService, never()).update(eq(lectureId), any());
    }

    @Test
    void deleteLecture_ValidId_ShouldReturnDeleteFormLectureSuccessfulPage() throws Exception {
        Long lectureId = 1L;

        mockMvc.perform(post("/deleteLecture/{id}", lectureId))
                .andExpect(status().isOk())
                .andExpect(view().name("delete-form-lecture-successful"));

        verify(lectureService, times(1)).delete(lectureId);
    }

    private Lecture createMockLecture() {
        Lecture mockLecture = new Lecture();
        Course mockCourse = new Course();
        Teacher mockTeacher = new Teacher();
        mockCourse.setId(1L);
        mockTeacher.setId(1L);
        mockLecture.setCourse(mockCourse);
        mockLecture.setTeacher(mockTeacher);
        return mockLecture;
    }
}




