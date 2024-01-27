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
import ua.com.foxminded.repository.FacultyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {FacultyServiceImpl.class})
class FacultyServiceImplTest {

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private FacultyServiceImpl facultyService;

    @BeforeEach
    void setUp() {
        Mockito.reset(facultyRepository, courseRepository);
    }

    @Test
    void saveFaculty_ValidName_Success() {
        Faculty faculty = new Faculty(1L, "Engineering");
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        facultyService.save(faculty);

        verify(facultyRepository, times(1)).save(faculty);
    }

    @Test
    void saveFaculty_InvalidName_ThrowsException() {
        Faculty faculty = new Faculty(1L, "I2");
        assertThrows(RuntimeException.class, () -> facultyService.save(faculty));

        verify(facultyRepository, never()).save(faculty);
    }

    @Test
    void updateFaculty_ValidIdAndData_Success() {
        Long id = 1L;
        Faculty existingFaculty = new Faculty(id, "Engineering");
        Faculty updatedFaculty = new Faculty(null, "Science");

        when(facultyRepository.findById(id)).thenReturn(Optional.of(existingFaculty));

        facultyService.update(id, updatedFaculty);

        verify(facultyRepository, times(1)).save(existingFaculty);
        assertEquals("Science", existingFaculty.getName());
    }

    @Test
    void updateFaculty_InvalidId_ThrowsException() {
        Long id = 1L;
        Faculty updatedFaculty = new Faculty(null, "Science");

        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> facultyService.update(id, updatedFaculty));

        verify(facultyRepository, never()).save(any());
    }

    @Test
    void updateFaculty_NullData_ThrowsException() {
        Long id = 1L;

        assertThrows(RuntimeException.class, () -> facultyService.update(id, null));

        verify(facultyRepository, never()).save(any());
    }

    @Test
    void deleteFaculty_Exists_Success() {
        Long id = 1L;
        when(facultyRepository.existsById(id)).thenReturn(true);

        facultyService.delete(id);

        verify(facultyRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteFaculty_NotExists_ThrowsException() {
        Long id = 1L;
        when(facultyRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> facultyService.delete(id));

        verify(facultyRepository, never()).deleteById(id);
    }

    @Test
    void findByIdFaculty_Exists_Success() {
        Long id = 1L;
        Faculty faculty = new Faculty(0L, "Engineering");
        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        Faculty result = facultyService.findById(id);

        assertNotNull(result);
        assertEquals(faculty, result);
    }

    @Test
    void findByIdFaculty_NotExists_ThrowsException() {
        Long id = 1L;
        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> facultyService.findById(id));
    }

    @Test
    void attachCourseToFaculty_Success() {
        Long courseId = 1L;
        Long facultyId = 2L;
        Faculty faculty = new Faculty(0L, "Engineering");
        Course course = new Course(0L, "Math", faculty);

        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        facultyService.attachCourseToFaculty(courseId, facultyId);

        verify(courseRepository, times(1)).save(course);
        verify(facultyRepository, times(1)).save(faculty);
    }

    @Test
    void detachCourseFromFaculty_Success() {
        Long courseId = 1L;
        Long facultyId = 2L;
        Faculty faculty = new Faculty(0L, "Engineering");
        Course course = new Course(0L, "Math", faculty);

        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        facultyService.detachCourseFromFaculty(courseId, facultyId);

        verify(courseRepository, times(1)).save(course);
        verify(facultyRepository, times(1)).save(faculty);
    }

    @Test
    void findAllFacultiesToPage_Success() {
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty(1L, "Engineering"));
        faculties.add(new Faculty(2L, "Math"));

        when(facultyRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(faculties));

        Pageable pageable = PageRequest.of(0, 10);
        facultyService.findAll(pageable);

        verify(facultyRepository, times(1)).findAll(eq(pageable));
    }
}
