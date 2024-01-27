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
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.repository.CourseRepository;
import ua.com.foxminded.repository.LectureRepository;

import java.util.ArrayList;
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
    void updateCourse_ValidIdAndData_Success() {
        Long id = 1L;
        Course existingCourse = new Course(id, "Mathematics", new Faculty(1L, "Engineering"));
        Course updatedCourse = new Course(null, "Physics", new Faculty(2L, "Science"));

        when(courseRepository.findById(id)).thenReturn(Optional.of(existingCourse));

        courseService.update(id, updatedCourse);

        verify(courseRepository, times(1)).save(existingCourse);
        assertEquals("Physics", existingCourse.getName());
        assertEquals(new Faculty(2L, "Science"), existingCourse.getFaculty());
    }

    @Test
    void updateCourse_NullData_ThrowsException() {
        Long id = 1L;

        assertThrows(RuntimeException.class, () -> courseService.update(id, null));

        verify(courseRepository, never()).save(any());
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
    void findAllCoursesToPage_Success() {
        Faculty faculty = new Faculty();
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(1L, "Math", faculty));
        courses.add(new Course(2L, "Physics", faculty));

        when(courseRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(courses));

        Pageable pageable = PageRequest.of(0, 10);
        courseService.findAll(pageable);

        verify(courseRepository, times(1)).findAll(eq(pageable));
    }
}

