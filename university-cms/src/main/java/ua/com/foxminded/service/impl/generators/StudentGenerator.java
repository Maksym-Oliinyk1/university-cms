package ua.com.foxminded.service.impl.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.enums.Authorities;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

@Service
public class StudentGenerator extends DataGenerator {

  private static final Logger logger = LoggerFactory.getLogger(StudentGenerator.class);

  private static final int AMOUNT_OF_STUDENTS = 200;
  private static final int AMOUNT_OF_GROUPS = 20;

  private final StudentService studentService;
  private final AuthenticationService authenticationService;
  private final GroupService groupService;

  public StudentGenerator(
          StudentService studentService,
          AuthenticationService authenticationService,
          GroupService groupService) {
    this.studentService = studentService;
    this.authenticationService = authenticationService;
    this.groupService = groupService;
  }

  public void generateIfEmpty() {
    if (studentService.count() == 0) {
      generateStudents();
    }
  }

  @Override
  public int getOrder() {
    return 7;
  }

  private void generateStudents() {
    for (int i = 0; i < AMOUNT_OF_STUDENTS; i++) {
      StudentDTO studentDTO = new StudentDTO();
      fillUserFields(studentDTO);
      studentDTO.setAuthority(Authorities.STUDENT);
      Group randomGroup = groupService.findById(Math.abs(random.nextLong() % AMOUNT_OF_GROUPS) + 1);
      studentDTO.setGroup(randomGroup);
      logger.info("Created student: {} {}", studentDTO.getFirstName(), studentDTO.getLastName());
      authenticationService.registerStudent(studentDTO);
    }
  }
}
