package ua.com.foxminded.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.repository.StudentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {StudentServiceImpl.class})
class StudentServiceImplTest {

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        Mockito.reset(studentRepository);
    }

    @Test
    void createStudent_ValidName_Success() {
        Student student = new Student(1L, "John", "Doe");
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        studentService.create(student);

        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void createStudent_InvalidName_ThrowsException() {
        Student student = new Student(1L, "John1", "Doe");
        assertThrows(RuntimeException.class, () -> studentService.create(student));

        verify(studentRepository, never()).save(student);
    }

    @Test
    void updateStudent_ValidName_Success() {
        Student student = new Student(1L, "John", "Doe");
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        studentService.update(student);

        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void updateStudent_InvalidName_ThrowsException() {
        Student student = new Student(1L, "John1", "Doe");
        assertThrows(RuntimeException.class, () -> studentService.update(student));

        verify(studentRepository, never()).save(student);
    }

    @Test
    void deleteStudent_Exists_Success() {
        Long id = 1L;
        when(studentRepository.existsById(id)).thenReturn(true);

        studentService.delete(id);

        verify(studentRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteStudent_NotExists_ThrowsException() {
        Long id = 1L;
        when(studentRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> studentService.delete(id));

        verify(studentRepository, never()).deleteById(id);
    }

    @Test
    void findByIdStudent_Exists_Success() {
        Long id = 1L;
        Student student = new Student(id, "John", "Doe");
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        Student result = studentService.findById(id);

        assertNotNull(result);
        assertEquals(student, result);
    }

    @Test
    void findByIdStudent_NotExists_ThrowsException() {
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.findById(id));
    }

    @Test
    void findAllStudents_Success() {
        Student student1 = new Student(1L, "John", "Doe");
        Student student2 = new Student(2L, "Jane", "Doe");
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        List<Student> result = studentService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(student1));
        assertTrue(result.contains(student2));
    }
}
