package ua.com.foxminded.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.enums.Gender;
import ua.com.foxminded.repository.StudentRepository;
import ua.com.foxminded.service.ImageService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {StudentServiceImpl.class})
class StudentServiceImplTest {
    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private ImageService imageService;

    @Autowired
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        Mockito.reset(studentRepository);
    }

    @Test
    void save_ValidDataWithImage_SaveSuccessful() {
        Student student = createStudent();
        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageService.saveUserImage(anyString(), anyLong(), any(MultipartFile.class))).thenReturn("32.png");
        when(studentRepository.save(student)).thenReturn(student);

        studentService.save(student, imageFile);

        verify(studentRepository, times(2)).save(student);
        verify(imageService, times(1)).saveUserImage(anyString(), eq(student.getId()), eq(imageFile));
        assertEquals("32.png", student.getImageName());
    }

    @Test
    void save_ShouldSetDefaultImageWhenImageFileIsEmpty() {
        Student student = createStudent();
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);

        studentService.save(student, emptyFile);

        verify(studentRepository, times(1)).save(student);
        verify(imageService, times(1)).setDefaultImageForUser(student);
    }

    @Test
    void save_ValidDataWithoutImage_SaveSuccessful() {
        Student student = createStudent();

        studentService.save(student, null);

        verify(studentRepository, times(1)).save(student);
        verify(imageService, times(1)).setDefaultImageForUser(student);
        verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
    }

    @Test
    void save_InvalidData_ThrowException() {
        Student student = createStudent();
        student.setEmail("invalidemail");

        assertThrows(RuntimeException.class, () -> studentService.save(student, null));

        verify(studentRepository, never()).save(any(Student.class));
        verify(imageService, never()).setDefaultImageForUser(any(Student.class));
        verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
    }

    @Test
    void update_ExistingStudentWithValidDataAndImage_UpdateSuccessful() {
        Long id = 1L;
        Student existingStudent = createStudent();
        Student updatedStudent = createStudent();
        MultipartFile imageFile = mock(MultipartFile.class);
        when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageService.saveUserImage(anyString(), eq(id), any(MultipartFile.class))).thenReturn("32.png");

        studentService.update(id, updatedStudent, imageFile);

        verify(studentRepository, times(1)).save(existingStudent);
        verify(imageService, times(1)).saveUserImage(anyString(), eq(id), eq(imageFile));
        assertEquals("32.png", existingStudent.getImageName());
    }

    @Test
    void update_ShouldSetDefaultImageWhenImageFileIsNull() {
        Long id = 1L;
        Student existingStudent = createStudent();
        Student updatedStudent = createStudent();
        updatedStudent.setImageName(null);
        when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));

        studentService.update(id, updatedStudent, null);

        verify(studentRepository, times(1)).save(existingStudent);
        verify(imageService, times(1)).setDefaultImageForUser(existingStudent);
    }

    @Test
    void update_ShouldSetDefaultImageWhenImageFileIsEmpty() {
        Long id = 1L;
        Student existingStudent = createStudent();
        Student updatedStudent = createStudent();
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);
        when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));

        studentService.update(id, updatedStudent, emptyFile);

        verify(studentRepository, times(1)).save(existingStudent);
        verify(imageService, times(1)).setDefaultImageForUser(existingStudent);
    }

    @Test
    void update_NonExistingStudent_ThrowException() {
        Long id = 1L;
        Student updatedStudent = createStudent();
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.update(id, updatedStudent, null));

        verify(studentRepository, never()).save(any(Student.class));
        verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
        verify(imageService, never()).setDefaultImageForUser(any(Student.class));
    }

    @Test
    void delete_ExistingStudent_DeleteSuccessful() {
        Long id = 1L;
        Student student = createStudent();
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        studentService.delete(id);

        verify(studentRepository, times(1)).deleteById(id);
        verify(imageService, times(1)).deleteUserImage(anyString(), id);
    }

    @Test
    void findById_ExistingStudent_ReturnStudent() {
        Long id = 1L;
        Student student = createStudent();
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        Student result = studentService.findById(id);

        assertEquals(student, result);
    }

    @Test
    void findById_NonExistingStudent_ThrowException() {
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.findById(id));
    }

    @Test
    void findAll_ReturnPageOfStudents() {
        Pageable pageable = mock(Pageable.class);
        Page page = mock(Page.class);

        when(studentRepository.findAll(pageable)).thenReturn(page);

        Page<Student> result = studentService.findAll(pageable);

        assertEquals(page, result);
    }

    private Student createStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        student.setGender(Gender.MALE);
        student.setGroup(new Group());
        return student;
    }
}

