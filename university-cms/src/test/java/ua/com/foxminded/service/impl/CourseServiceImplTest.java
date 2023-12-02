package ua.com.foxminded.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.repository.CourseRepository;
import ua.com.foxminded.repository.LectureRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CourseServiceImpl.class})
class CourseServiceImplTest {

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private LectureRepository lectureRepository;

    @Autowired
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        Mockito.reset(courseRepository, lectureRepository);
    }

    @Test
    void saveCourse_ValidName_Success() {
        Faculty faculty = new Faculty();
        Course course = new Course(1L, "Mathematics", faculty);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        courseService.save(course);

        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void saveCourse_InvalidName_ThrowsException() {
        Faculty faculty = new Faculty();
        Course course = new Course(1L, "I1", faculty);
        assertThrows(RuntimeException.class, () -> courseService.save(course));

        verify(courseRepository, never()).save(course);
    }

    @Test
    void deleteCourse_Exists_Success() {
        Long id = 1L;
        when(courseRepository.existsById(id)).thenReturn(true);

        courseService.delete(id);

        verify(courseRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteCourse_NotExists_ThrowsException() {
        Long id = 1L;
        when(courseRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> courseService.delete(id));

        verify(courseRepository, never()).deleteById(id);
    }

    @Test
    void findByIdCourse_Exists_Success() {
        Long id = 1L;
        Faculty faculty = new Faculty();
        Course course = new Course(0L, "Math", faculty);
        when(courseRepository.findById(id)).thenReturn(Optional.of(course));

        Course result = courseService.findById(id);

        assertNotNull(result);
        assertEquals(course, result);
    }

    @Test
    void findByIdCourse_NotExists_ThrowsException() {
        Long id = 1L;
        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> courseService.findById(id));
    }

    @Test
    void findAllCourses_Success() {
        Faculty faculty = new Faculty();
        Course course1 = new Course(0L, "Math", faculty);
        Course course2 = new Course(0L, "Physics", faculty);
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> result = courseService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(course1));
        assertTrue(result.contains(course2));
    }
}

