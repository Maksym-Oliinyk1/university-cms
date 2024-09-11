package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.enums.Authorities;
import ua.com.foxminded.repository.StudentRepository;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.service.StudentService;
import ua.com.foxminded.service.UserMapper;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
  private static final String STUDENT_ROLE = "STUDENT";
  private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

  private final StudentRepository studentRepository;
  private final ImageService imageService;
  private final UserEmailServiceImpl userEmailService;
  private final UserMapper userMapper;

  @Autowired
  public StudentServiceImpl(
          StudentRepository studentRepository,
          ImageService imageService,
          UserEmailServiceImpl userEmailService,
          UserMapper userMapper) {
    this.studentRepository = studentRepository;
    this.imageService = imageService;
    this.userEmailService = userEmailService;
    this.userMapper = userMapper;
  }

  @Override
  public Student save(StudentDTO studentDTO) {
    if (isEmailFree(studentDTO.getEmail())) {
      studentDTO.setAuthority(Authorities.STUDENT);
      if (studentDTO.getImage() == null || studentDTO.getImage().isEmpty()) {
        Student student = userMapper.mapFromDto(studentDTO);
        student.setImageName(imageService.getDefaultIUserImage(student.getGender(), STUDENT_ROLE));
        logger.info("Saved student: {} {}", studentDTO.getFirstName(), studentDTO.getLastName());
        return studentRepository.save(student);
      } else {
        Student student = userMapper.mapFromDto(studentDTO);
        student = studentRepository.save(student);
        String imageName =
                imageService.saveUserImage(STUDENT_ROLE, student.getId(), studentDTO.getImage());
        student.setImageName(imageName);
        logger.info("Saved student: {} {}", studentDTO.getFirstName(), studentDTO.getLastName());
        return studentRepository.save(student);
      }
    }
    throw new RuntimeException("Email is already existed");
  }

  @Override
  public Student update(Long id, StudentDTO studentDTO) {
    if (isEmailFree(studentDTO.getEmail())) {
      studentDTO.setAuthority(Authorities.STUDENT);
      Student existingStudent =
              studentRepository
                      .findById(id)
                      .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
      existingStudent.setFirstName(studentDTO.getFirstName());
      existingStudent.setLastName(studentDTO.getLastName());
      existingStudent.setGender(studentDTO.getGender());
      existingStudent.setBirthDate(studentDTO.getBirthDate());
      existingStudent.setEmail(studentDTO.getEmail());
      existingStudent.setGroup(studentDTO.getGroup());
      existingStudent.setPassword(studentDTO.getPassword());
      if (studentDTO.getImage() == null || studentDTO.getImage().isEmpty()) {
        imageService.deleteUserImage(existingStudent.getImageName());
        existingStudent.setImageName(
                imageService.getDefaultIUserImage(studentDTO.getGender(), STUDENT_ROLE));
      } else {
        imageService.deleteUserImage(existingStudent.getImageName());
        String imageName = imageService.saveUserImage(STUDENT_ROLE, id, studentDTO.getImage());
        existingStudent.setImageName(imageName);
      }
      logger.info("Student updated by id: {}", id);
      return studentRepository.save(existingStudent);
    }
    throw new RuntimeException("Email is already existed");
  }

  @Override
  public StudentDTO findByIdDTO(Long id) {
    Optional<Student> optionalStudent = studentRepository.findById(id);
    if (optionalStudent.isPresent()) {
      logger.info("Student was found by id: {}", id);
      return userMapper.mapToDto(optionalStudent.get());
    } else {
      throw new RuntimeException("There is no such student");
    }
  }

  @Override
  public Optional<Student> findByEmail(String email) {
    return studentRepository.findByEmail(email);
  }

  @Override
  public void delete(Long id) {
    Student student = findById(id);
    imageService.deleteUserImage(student.getImageName());
    studentRepository.deleteById(id);
    logger.info("Student was deleted by id: {}", id);
  }

  @Override
  public Student findById(Long id) {
    Optional<Student> optionalStudent = studentRepository.findById(id);
    if (optionalStudent.isPresent()) {
      logger.info("Student was found by id: {}", id);
      return optionalStudent.get();
    } else {
      throw new RuntimeException("There is no such student");
    }
  }

  @Override
  public Page<Student> findAll(Pageable pageable) {
    int pageNumber = pageable.getPageNumber();
    int pageSize = pageable.getPageSize();
    int from = pageNumber * pageSize;
    int to = from + pageSize;
    logger.info("Find students from {} to {}", from, to);
    return studentRepository.findAll(pageable);
  }

  @Override
  public Long count() {
    return studentRepository.count();
  }

  @Override
  public Page<Student> findAllStudentByGroup(Long groupId, Pageable pageable) {
    int pageNumber = pageable.getPageNumber();
    int pageSize = pageable.getPageSize();
    int from = pageNumber * pageSize;
    int to = from + pageSize;
    logger.info("Find students if group: {} from {} to {}", groupId, from, to);
    return studentRepository.findByGroupId(groupId, pageable);
  }

  private boolean isEmailFree(String email) {
    return !userEmailService.isUserExistByEmail(email);
  }
}
