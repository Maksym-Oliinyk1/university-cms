package ua.com.foxminded.service.impl;

import jakarta.persistence.EntityNotFoundException;
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
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.Gender;
import ua.com.foxminded.repository.LectureRepository;
import ua.com.foxminded.repository.TeacherRepository;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.service.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {TeacherServiceImpl.class})
class TeacherServiceImplTest {

    private static final String USER_ROLE = "TEACHER";

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private LectureRepository lectureRepository;

    @MockBean
    private ImageService imageService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private TeacherServiceImpl teacherService;

    @BeforeEach
    void setUp() {
        Mockito.reset(teacherRepository, lectureRepository, imageService, userMapper);
    }

    @Test
    void save_ValidDataWithImage_SaveSuccessful() {
        TeacherDTO teacherDTO = createTeacherDTO();
        Teacher teacher = createTeacher();
        MultipartFile imageFile = mock(MultipartFile.class);
        teacherDTO.setImage(imageFile);
        when(userMapper.mapFromDto(teacherDTO)).thenReturn(teacher);
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageService.saveUserImage(anyString(), anyLong(), any(MultipartFile.class)))
                .thenReturn("32.png");
        when(teacherRepository.save(any(Teacher.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        teacherService.save(teacherDTO);

        verify(userMapper, times(1)).mapFromDto(teacherDTO);
        verify(imageService, times(1)).saveUserImage(anyString(), anyLong(), eq(imageFile));
        verify(teacherRepository, times(2)).save(teacher);
    }

    @Test
    void save_ShouldSetDefaultImageWhenImageFileIsEmpty() {
        Teacher teacher = createTeacher();
        TeacherDTO teacherDTO = createTeacherDTO();
        when(userMapper.mapFromDto(teacherDTO)).thenReturn(teacher);
        when(imageService.getDefaultIUserImage(any(Gender.class), anyString()))
                .thenReturn("default.png");

        teacherService.save(teacherDTO);

        verify(userMapper, times(1)).mapFromDto(teacherDTO);
        verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void save_ValidDataWithoutImage_SaveSuccessful() {
        TeacherDTO teacherDTO = createTeacherDTO();
        Teacher teacher = createTeacher();
        when(userMapper.mapFromDto(teacherDTO)).thenReturn(createTeacher());

        teacherService.save(teacherDTO);

        verify(userMapper, times(1)).mapFromDto(teacherDTO);
        verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
        verify(teacherRepository, times(1)).save(teacher);
        verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
    }

    @Test
    void update_ExistingTeacherWithValidDataAndImage_UpdateSuccessful() {
        Long id = 1L;
        TeacherDTO teacherDTO = createTeacherDTO();
        MultipartFile imageFile = mock(MultipartFile.class);
        teacherDTO.setImage(imageFile);
        Teacher existingTeacher = createTeacher();
        when(teacherRepository.findById(id)).thenReturn(Optional.of(existingTeacher));
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageService.saveUserImage(anyString(), eq(id), any(MultipartFile.class)))
                .thenReturn("32.png");

        teacherService.update(id, teacherDTO);

        verify(teacherRepository, times(1)).save(existingTeacher);
        verify(imageService, times(1)).saveUserImage(anyString(), eq(id), eq(imageFile));
        assertEquals("32.png", existingTeacher.getImageName());
    }

    @Test
    void update_ShouldSetDefaultImageWhenImageFileIsNull() {
        Long id = 1L;
        TeacherDTO teacherDTO = createTeacherDTO();
        Teacher existingTeacher = createTeacher();
        teacherDTO.setImage(null);
        when(teacherRepository.findById(id)).thenReturn(Optional.of(existingTeacher));

        teacherService.update(id, teacherDTO);

        verify(teacherRepository, times(1)).save(existingTeacher);
        verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
    }

    @Test
    void update_ShouldSetDefaultImageWhenImageFileIsEmpty() {
        Long id = 1L;
        TeacherDTO teacherDTO = createTeacherDTO();
        Teacher existingTeacher = createTeacher();
        MultipartFile emptyFile = mock(MultipartFile.class);

        lenient().when(emptyFile.isEmpty()).thenReturn(true);

        when(teacherRepository.findById(id)).thenReturn(Optional.of(existingTeacher));

        teacherService.update(id, teacherDTO);

        verify(teacherRepository, times(1)).save(existingTeacher);
        verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
    }

    @Test
    void update_NonExistingTeacher_ThrowException() {
        Long id = 1L;
        TeacherDTO teacherDTO = createTeacherDTO();
        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teacherService.update(id, teacherDTO));

        verify(teacherRepository, never()).save(any(Teacher.class));
        verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
        verify(imageService, never()).getDefaultIUserImage(teacherDTO.getGender(), USER_ROLE);
    }

    @Test
    void delete_ExistingTeacher_DeleteSuccessful() {
        Long id = 1L;
        Teacher teacher = createTeacher();
        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        teacherService.delete(id);

        verify(teacherRepository, times(1)).deleteById(id);
        verify(imageService, times(1)).deleteUserImage(teacher.getImageName());
    }

    @Test
    void attachLectureToTeacher_ValidIds_AttachSuccessful() {
        Long lectureId = 1L;
        Long teacherId = 1L;
        Lecture lecture = createLecture();
        Teacher teacher = createTeacher();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));

        teacherService.attachLectureToTeacher(lectureId, teacherId);

        assertTrue(teacher.getLectures().contains(lecture));
        assertEquals(teacher, lecture.getTeacher());
        verify(teacherRepository, times(1)).save(teacher);
        verify(lectureRepository, times(1)).save(lecture);
    }

    @Test
    void attachLectureToTeacher_NonExistingTeacher_ThrowException() {
        Long lectureId = 1L;
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());
        Lecture lecture = createLecture();
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));

        assertThrows(
                EntityNotFoundException.class,
                () -> teacherService.attachLectureToTeacher(lectureId, teacherId));

        verify(teacherRepository, never()).save(any(Teacher.class));
        verify(lectureRepository, never()).save(any(Lecture.class));
    }

    @Test
    void attachLectureToTeacher_NonExistingLecture_ThrowException() {
        Long lectureId = 1L;
        Long teacherId = 1L;
        Teacher teacher = createTeacher();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> teacherService.attachLectureToTeacher(lectureId, teacherId));

        verify(teacherRepository, never()).save(any(Teacher.class));
        verify(lectureRepository, never()).save(any(Lecture.class));
    }

    @Test
    void detachLectureFromTeacher_ValidIds_DetachSuccessful() {
        Long lectureId = 1L;
        Long teacherId = 1L;
        Lecture lecture = createLecture();
        Teacher teacher = createTeacher();
        teacher.getLectures().add(lecture);
        lecture.setTeacher(teacher);
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));

        teacherService.detachLectureFromTeacher(lectureId, teacherId);

        assertFalse(teacher.getLectures().contains(lecture));
        assertNull(lecture.getTeacher());
        verify(teacherRepository, times(1)).save(teacher);
        verify(lectureRepository, times(1)).save(lecture);
    }

    @Test
    void detachLectureFromTeacher_NonExistingTeacher_ThrowException() {
        Long lectureId = 1L;
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());
        Lecture lecture = createLecture();
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));

        assertThrows(
                EntityNotFoundException.class,
                () -> teacherService.detachLectureFromTeacher(lectureId, teacherId));

        verify(teacherRepository, never()).save(any(Teacher.class));
        verify(lectureRepository, never()).save(any(Lecture.class));
    }

    @Test
    void detachLectureFromTeacher_NonExistingLecture_ThrowException() {
        Long lectureId = 1L;
        Long teacherId = 1L;
        Teacher teacher = createTeacher();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> teacherService.detachLectureFromTeacher(lectureId, teacherId));

        verify(teacherRepository, never()).save(any(Teacher.class));
        verify(lectureRepository, never()).save(any(Lecture.class));
    }

    @Test
    void showLecturesBetweenDates_ValidIds_ReturnLectures() {
        Long teacherId = 1L;
        LocalDateTime firstDate = LocalDateTime.now().minusDays(1);
        LocalDateTime secondDate = LocalDateTime.now();
        Lecture lecture = createLecture();
        when(teacherRepository.existsById(teacherId)).thenReturn(true);
        when(teacherRepository.findLecturesByDateBetween(teacherId, firstDate, secondDate))
                .thenReturn(Collections.singletonList(lecture));

        List<Lecture> result =
                teacherService.showLecturesBetweenDates(teacherId, firstDate, secondDate);

        assertEquals(Collections.singletonList(lecture), result);
    }

    @Test
    void showLecturesBetweenDates_TeacherNotFound_ThrowException() {
        Long teacherId = 1L;
        LocalDateTime firstDate = LocalDateTime.now();
        LocalDateTime secondDate = firstDate.plusDays(7);
        when(teacherRepository.existsById(teacherId)).thenReturn(false);

        assertThrows(
                RuntimeException.class,
                () -> teacherService.showLecturesBetweenDates(teacherId, firstDate, secondDate));
        verify(teacherRepository, times(1)).existsById(teacherId);
        verify(teacherRepository, never())
                .findLecturesByDateBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void findByIdDTO_ExistingTeacher_ReturnTeacherDTO() {
        Long id = 1L;
        Teacher teacher = createTeacher();
        TeacherDTO teacherDTO = createTeacherDTO();

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));
        when(userMapper.mapToDto(teacher)).thenReturn(teacherDTO);

        TeacherDTO result = teacherService.findByIdDTO(id);

        assertEquals(teacherDTO, result);
    }

    @Test
    void findByIdDTO_NonExistingTeacher_ThrowException() {
        Long id = 1L;

        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teacherService.findByIdDTO(id));
    }

    @Test
    void findById_ExistingTeacher_ReturnTeacher() {
        Long id = 1L;
        Teacher teacher = createTeacher();
        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.findById(id);

        assertEquals(teacher, result);
    }

    @Test
    void findById_NonExistingTeacher_ThrowException() {
        Long id = 1L;
        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teacherService.findById(id));
    }

    @Test
    void findAll_ReturnPageOfTeachers() {
        Pageable pageable = mock(Pageable.class);
        Page page = mock(Page.class);

        when(teacherRepository.findAll(pageable)).thenReturn(page);

        Page<Teacher> result = teacherService.findAll(pageable);

        assertEquals(page, result);
    }

    private TeacherDTO createTeacherDTO() {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setFirstName("John");
        teacherDTO.setLastName("Doe");
        teacherDTO.setEmail("john.doe@example.com");
        teacherDTO.setGender(Gender.MALE);
        teacherDTO.setAcademicDegree("Ph.D.");
        return teacherDTO;
    }

    private Teacher createTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setEmail("john.doe@example.com");
        teacher.setGender(Gender.MALE);
        teacher.setAcademicDegree("Ph.D.");
        teacher.setLectures(new ArrayList<>());
        return teacher;
    }

    private Lecture createLecture() {
        Lecture lecture = new Lecture();
        lecture.setId(1L);
        lecture.setDate(LocalDateTime.now());
        return lecture;
    }
}
