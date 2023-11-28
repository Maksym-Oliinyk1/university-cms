package ua.com.foxminded.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.repository.LectureRepository;
import ua.com.foxminded.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {TeacherServiceImpl.class})
class TeacherServiceImplTest {

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private LectureRepository lectureRepository;

    @Autowired
    private TeacherServiceImpl teacherService;

    @BeforeEach
    void setUp() {
        Mockito.reset(teacherRepository, lectureRepository);
    }

    @Test
    void createTeacher_ValidName_Success() {
        Teacher teacher = new Teacher(1L, "John", "Doe", "Ph.D.");
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        teacherService.create(teacher);

        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void createTeacher_InvalidName_ThrowsException() {
        Teacher teacher = new Teacher(1L, "John1", "Doe", "Ph.D.");
        assertThrows(RuntimeException.class, () -> teacherService.create(teacher));

        verify(teacherRepository, never()).save(teacher);
    }

    @Test
    void updateTeacher_ValidName_Success() {
        Teacher teacher = new Teacher(1L, "John", "Doe", "Ph.D.");
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        teacherService.update(teacher);

        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void updateTeacher_InvalidName_ThrowsException() {
        Teacher teacher = new Teacher(1L, "John1", "Doe", "Ph.D.");
        assertThrows(RuntimeException.class, () -> teacherService.update(teacher));

        verify(teacherRepository, never()).save(teacher);
    }

    @Test
    void deleteTeacher_Exists_Success() {
        Long id = 1L;
        when(teacherRepository.existsById(id)).thenReturn(true);

        teacherService.delete(id);

        verify(teacherRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteTeacher_NotExists_ThrowsException() {
        Long id = 1L;
        when(teacherRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> teacherService.delete(id));

        verify(teacherRepository, never()).deleteById(id);
    }

    @Test
    void findByIdTeacher_Exists_Success() {
        Long id = 1L;
        Teacher teacher = new Teacher(id, "John", "Doe", "Ph.D.");
        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.findById(id);

        assertNotNull(result);
        assertEquals(teacher, result);
    }

    @Test
    void findByIdTeacher_NotExists_ThrowsException() {
        Long id = 1L;
        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teacherService.findById(id));
    }

    @Test
    void findAllTeachers_Success() {
        Teacher teacher1 = new Teacher(1L, "John", "Doe", "Ph.D.");
        Teacher teacher2 = new Teacher(2L, "Jane", "Doe", "M.Sc.");
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher1, teacher2));

        List<Teacher> result = teacherService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(teacher1));
        assertTrue(result.contains(teacher2));
    }

    @Test
    void attachLectureToTeacher_Success() {
        Long lectureId = 1L;
        Long teacherId = 2L;
        Lecture lecture = new Lecture(lectureId, null, null, "Introduction to Java", "This is a valid description about Java course for beginners", LocalDateTime.now());
        Teacher teacher = new Teacher(teacherId, "John", "Doe", "Ph.D.");

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));

        teacherService.attachLectureToTeacher(lectureId, teacherId);

        verify(teacherRepository, times(1)).save(teacher);
        verify(lectureRepository, times(1)).save(lecture);
    }

    @Test
    void detachLectureFromTeacher_Success() {
        Long lectureId = 1L;
        Long teacherId = 2L;
        Lecture lecture = new Lecture(lectureId, null, null, "Introduction to Java", "This is a valid description about Java course for beginners", LocalDateTime.now());
        Teacher teacher = new Teacher(teacherId, "John", "Doe", "Ph.D.");

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));

        teacherService.detachLectureFromTeacher(lectureId, teacherId);

        verify(teacherRepository, times(1)).save(teacher);
        verify(lectureRepository, times(1)).save(lecture);
    }

    @Test
    void showLecturesPerWeek_ValidTeacherId_Success() {
        Long teacherId = 1L;

        when(teacherRepository.existsById(teacherId)).thenReturn(true);
        when(teacherRepository.countLecturesByTeacher(teacherId)).thenReturn(5L);
        when(teacherRepository.findLecturesByDateBetween(eq(teacherId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        List<Lecture> result = teacherService.showLecturesPerWeek(teacherId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void showLecturesPerWeek_InvalidTeacherId_ThrowsException() {
        Long teacherId = 1L;

        when(teacherRepository.existsById(teacherId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> teacherService.showLecturesPerWeek(teacherId));

        verify(teacherRepository, never()).countLecturesByTeacher(teacherId);
        verify(teacherRepository, never()).findLecturesByDateBetween(eq(teacherId), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void showLecturesPerMonth_ValidTeacherId_Success() {
        Long teacherId = 1L;

        when(teacherRepository.existsById(teacherId)).thenReturn(true);
        when(teacherRepository.countLecturesByTeacher(teacherId)).thenReturn(5L);
        when(teacherRepository.findLecturesByDateBetween(eq(teacherId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        List<Lecture> result = teacherService.showLecturesPerMonth(teacherId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void showLecturesPerMonth_InvalidTeacherId_ThrowsException() {
        Long teacherId = 1L;

        when(teacherRepository.existsById(teacherId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> teacherService.showLecturesPerMonth(teacherId));

        verify(teacherRepository, never()).countLecturesByTeacher(teacherId);
        verify(teacherRepository, never()).findLecturesByDateBetween(eq(teacherId), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void showLecturesBetweenDates_ValidTeacherIdAndDates_Success() {
        Long teacherId = 1L;
        LocalDateTime firstDate = LocalDateTime.now();
        LocalDateTime secondDate = firstDate.plusDays(5);

        when(teacherRepository.existsById(teacherId)).thenReturn(true);
        when(teacherRepository.countLecturesByTeacher(teacherId)).thenReturn(5L);
        when(teacherRepository.findLecturesByDateBetween(eq(teacherId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        List<Lecture> result = teacherService.showLecturesBetweenDates(teacherId, firstDate, secondDate);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void showLecturesBetweenDates_InvalidTeacherId_ThrowsException() {
        Long teacherId = 1L;
        LocalDateTime firstDate = LocalDateTime.now();
        LocalDateTime secondDate = firstDate.plusDays(5);

        when(teacherRepository.existsById(teacherId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> teacherService.showLecturesBetweenDates(teacherId, firstDate, secondDate));

        verify(teacherRepository, never()).countLecturesByTeacher(teacherId);
        verify(teacherRepository, never()).findLecturesByDateBetween(eq(teacherId), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void showLecturesBetweenDates_InvalidDateRange_ThrowsException() {
        Long teacherId = 1L;
        LocalDateTime firstDate = LocalDateTime.now();
        LocalDateTime secondDate = firstDate.minusDays(5);

        when(teacherRepository.existsById(teacherId)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> teacherService.showLecturesBetweenDates(teacherId, firstDate, secondDate));

        verify(teacherRepository, never()).findLecturesByDateBetween(eq(teacherId), any(LocalDateTime.class), any(LocalDateTime.class));
    }
}
