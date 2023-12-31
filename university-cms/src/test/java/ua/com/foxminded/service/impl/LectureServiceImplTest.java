package ua.com.foxminded.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.LectureRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {LectureServiceImpl.class})
class LectureServiceImplTest {

    @MockBean
    private LectureRepository lectureRepository;

    @MockBean
    private GroupRepository groupRepository;

    @Autowired
    private LectureServiceImpl lectureService;

    @BeforeEach
    void setUp() {
        Mockito.reset(lectureRepository, groupRepository);
    }

    @Test
    void saveLecture_ValidNameAndDescription_Success() {
        Course testCourse = new Course();
        Teacher testTeacher = new Teacher();
        Lecture lecture = new Lecture(1L, testCourse, testTeacher, "Math101",
                "Mathematics is the universal language of patterns.", LocalDateTime.now());
        when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

        lectureService.save(lecture);

        verify(lectureRepository, times(1)).save(lecture);
    }

    @Test
    void saveLecture_InvalidName_ThrowsException() {
        Course testCourse = new Course();
        Teacher testTeacher = new Teacher();
        Lecture lecture = new Lecture(1L, testCourse, testTeacher, "I2",
                "Mathematics is the universal language of patterns.", LocalDateTime.now());
        assertThrows(RuntimeException.class, () -> lectureService.save(lecture));

        verify(lectureRepository, never()).save(lecture);
    }

    @Test
    void saveLecture_InvalidDescription_ThrowsException() {
        Course testCourse = new Course();
        Teacher testTeacher = new Teacher();
        Lecture lecture = new Lecture(1L, testCourse, testTeacher, "Math101",
                "InvalidDescription123", LocalDateTime.now());
        assertThrows(RuntimeException.class, () -> lectureService.save(lecture));

        verify(lectureRepository, never()).save(lecture);
    }

    @Test
    void deleteLecture_Exists_Success() {
        Long id = 1L;
        when(lectureRepository.existsById(id)).thenReturn(true);

        lectureService.delete(id);

        verify(lectureRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteLecture_NotExists_ThrowsException() {
        Long id = 1L;
        when(lectureRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> lectureService.delete(id));

        verify(lectureRepository, never()).deleteById(id);
    }

    @Test
    void findByIdLecture_Exists_Success() {
        Long id = 1L;
        Lecture lecture = new Lecture(id, null, null, "Introduction to Java", "This is a valid description about Java course for beginners", LocalDateTime.now());
        when(lectureRepository.findById(id)).thenReturn(Optional.of(lecture));

        Lecture result = lectureService.findById(id);

        assertNotNull(result);
        assertEquals(lecture, result);
    }

    @Test
    void findByIdLecture_NotExists_ThrowsException() {
        Long id = 1L;
        when(lectureRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> lectureService.findById(id));
    }

    @Test
    void findAllLectures_Success() {
        Lecture lecture1 = new Lecture(1L, null, null, "Introduction to Java", "This is a valid description about Java course for beginners", LocalDateTime.now());
        Lecture lecture2 = new Lecture(2L, null, null, "Advanced Java Concepts", "This is a valid description about Java course for beginners", LocalDateTime.now());
        when(lectureRepository.findAll()).thenReturn(Arrays.asList(lecture1, lecture2));

        List<Lecture> result = lectureService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(lecture1));
        assertTrue(result.contains(lecture2));
    }

    @Test
    void findAllLecturesToPage_Success() {
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(new Lecture(1L, null, null, "Introduction to Java", "This is a valid description about Java course for beginners", LocalDateTime.now()));
        lectures.add(new Lecture(2L, null, null, "Advanced Java Concepts", "This is a valid description about Java course for beginners", LocalDateTime.now()));
        when(lectureRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(lectures));

        Pageable pageable = PageRequest.of(0, 10);
        lectureService.findAll(pageable);

        verify(lectureRepository, times(1)).findAll(eq(pageable));
    }

    @Test
    void attachGroupToLecture_Success() {
        Long groupId = 1L;
        Long lectureId = 2L;
        Group group = new Group(groupId, "AB-12");
        Lecture lecture = new Lecture(lectureId, null, null, "Introduction to Java", "This is a valid description about Java course for beginners", LocalDateTime.now());
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        lectureService.attachGroupToLecture(groupId, lectureId);

        assertTrue(lecture.getGroups().contains(group));
        assertTrue(group.getLectures().contains(lecture));
        verify(lectureRepository, times(1)).save(lecture);
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void detachGroupFromLecture_Success() {
        Long groupId = 1L;
        Long lectureId = 2L;
        Group group = new Group(groupId, "AB-12");
        Lecture lecture = new Lecture(lectureId, null, null, "Introduction to Java", "This is a valid description about Java course for beginners", LocalDateTime.now());
        lecture.getGroups().add(group);
        group.getLectures().add(lecture);
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        lectureService.detachGroupFromLecture(groupId, lectureId);

        assertFalse(lecture.getGroups().contains(group));
        assertFalse(group.getLectures().contains(lecture));
        verify(lectureRepository, times(1)).save(lecture);
        verify(groupRepository, times(1)).save(group);
    }
}

