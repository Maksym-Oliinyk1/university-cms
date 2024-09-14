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
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.enums.Gender;
import ua.com.foxminded.repository.StudentRepository;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.service.UserMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {StudentServiceImpl.class})
class StudentServiceImplTest {

  private static final String USER_ROLE = "STUDENT";

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private ImageService imageService;

    @MockBean
    private UserMapper userMapper;
    @MockBean
    private UserEmailServiceImpl userEmailService;

    @Autowired
    private StudentServiceImpl studentService;

  @BeforeEach
  void setUp() {
    Mockito.reset(studentRepository, imageService, userMapper);
  }

  @Test
  void save_ValidDataWithImage_SaveSuccessful() {
    Student student = createStudent();
    StudentDTO studentDTO = createStudentDTO();
    MultipartFile imageFile = mock(MultipartFile.class);
    studentDTO.setImage(imageFile);
    when(userMapper.mapFromDto(studentDTO)).thenReturn(student);
    when(imageFile.isEmpty()).thenReturn(false);
    when(imageService.saveUserImage(anyString(), anyLong(), any(MultipartFile.class)))
            .thenReturn("32.png");
    when(studentRepository.save(any(Student.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
    when(userEmailService.isUserExistByEmail(anyString())).thenReturn(false);

    studentService.save(studentDTO);

    verify(userMapper, times(1)).mapFromDto(studentDTO);
    verify(imageService, times(1)).saveUserImage(anyString(), anyLong(), eq(imageFile));
    verify(studentRepository, times(2)).save(student);
  }

  @Test
  void save_ShouldSetDefaultImageWhenImageFileIsEmpty() {
    Student student = createStudent();
    StudentDTO studentDTO = createStudentDTO();
    when(userMapper.mapFromDto(studentDTO)).thenReturn(student);
    when(imageService.getDefaultIUserImage(any(Gender.class), anyString()))
            .thenReturn("default.png");
    when(userEmailService.isUserExistByEmail(anyString())).thenReturn(false);

    studentService.save(studentDTO);

    verify(userMapper, times(1)).mapFromDto(studentDTO);
    verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
    verify(studentRepository, times(1)).save(student);
  }

  @Test
  void save_ValidDataWithoutImage_SaveSuccessful() {
    StudentDTO studentDTO = createStudentDTO();
    Student student = createStudent();
    when(userMapper.mapFromDto(studentDTO)).thenReturn(student);
    when(userEmailService.isUserExistByEmail(anyString())).thenReturn(false);

    studentService.save(studentDTO);

    verify(userMapper, times(1)).mapFromDto(studentDTO);
    verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
    verify(studentRepository, times(1)).save(student);
    verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
  }

  @Test
  void update_ExistingStudentWithValidDataAndImage_UpdateSuccessful() {
    Long id = 1L;
    StudentDTO studentDTO = createStudentDTO();
    MultipartFile imageFile = mock(MultipartFile.class);
    studentDTO.setImage(imageFile);
    Student existingStudent = createStudent();
    when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));
    when(imageFile.isEmpty()).thenReturn(false);
    when(imageService.saveUserImage(anyString(), eq(id), any(MultipartFile.class)))
            .thenReturn("32.png");
    when(userEmailService.isUserExistByEmail(anyString())).thenReturn(false);

    studentService.update(id, studentDTO);

    verify(studentRepository, times(1)).save(existingStudent);
    verify(imageService, times(1)).saveUserImage(anyString(), eq(id), eq(imageFile));
    assertEquals("32.png", existingStudent.getImageName());
  }

  @Test
  void update_ShouldSetDefaultImageWhenImageFileIsNull() {
    Long id = 1L;
    StudentDTO studentDTO = createStudentDTO();
    Student existingStudent = createStudent();
    studentDTO.setImage(null);
    when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));
    when(userEmailService.isUserExistByEmail(anyString())).thenReturn(false);

    studentService.update(id, studentDTO);

    verify(studentRepository, times(1)).save(existingStudent);
    verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
  }

  @Test
  void update_ShouldSetDefaultImageWhenImageFileIsEmpty() {
    Long id = 1L;
    StudentDTO studentDTO = createStudentDTO();
    Student existingStudent = createStudent();
    MultipartFile emptyFile = mock(MultipartFile.class);

    lenient().when(emptyFile.isEmpty()).thenReturn(true);

    when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));
    when(userEmailService.isUserExistByEmail(anyString())).thenReturn(false);

    studentService.update(id, studentDTO);

    verify(studentRepository, times(1)).save(existingStudent);
    verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
  }

  @Test
  void update_NonExistingStudent_ThrowException() {
    Long id = 1L;
    StudentDTO studentDTO = createStudentDTO();
    when(studentRepository.findById(id)).thenReturn(Optional.empty());
    when(userEmailService.isUserExistByEmail(anyString())).thenReturn(false);

    assertThrows(RuntimeException.class, () -> studentService.update(id, studentDTO));

    verify(studentRepository, never()).save(any(Student.class));
    verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
    verify(imageService, never()).getDefaultIUserImage(Gender.MALE, USER_ROLE);
  }

  @Test
  void delete_ExistingStudent_DeleteSuccessful() {
    Long id = 1L;
    Student student = createStudent();
    when(studentRepository.findById(id)).thenReturn(Optional.of(student));

    studentService.delete(id);

    verify(studentRepository, times(1)).deleteById(id);
    verify(imageService, times(1)).deleteUserImage(student.getImageName());
  }

  @Test
  void findByIdDTO_ExistingStudent_ReturnStudentDTO() {
    Long id = 1L;
    Student student = createStudent();
    StudentDTO studentDTO = createStudentDTO();

    when(studentRepository.findById(id)).thenReturn(Optional.of(student));
    when(userMapper.mapToDto(student)).thenReturn(studentDTO);

    StudentDTO result = studentService.findByIdDTO(id);

    assertEquals(studentDTO, result);
  }

  @Test
  void findByIdDTO_NonExistingStudent_ThrowException() {
    Long id = 1L;

    when(studentRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> studentService.findByIdDTO(id));
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

  @Test
  void findAllStudentByGroup_ReturnPageOfStudents() {
    Long groupId = 1L;
    Pageable pageable = mock(Pageable.class);
    Page page = mock(Page.class);

    when(studentRepository.findByGroupId(groupId, pageable)).thenReturn(page);

    Page<Student> result = studentService.findAllStudentByGroup(groupId, pageable);

    assertEquals(page, result);
  }

  private StudentDTO createStudentDTO() {
    StudentDTO studentDTO = new StudentDTO();
    studentDTO.setFirstName("John");
    studentDTO.setLastName("Doe");
    studentDTO.setEmail("john.doe@example.com");
    studentDTO.setGender(Gender.MALE);
    return studentDTO;
  }

  private Student createStudent() {
    Student student = new Student();
    student.setId(1L);
    student.setFirstName("John");
    student.setLastName("Doe");
    student.setEmail("john.doe@example.com");
    student.setGender(Gender.MALE);
    return student;
  }
}
