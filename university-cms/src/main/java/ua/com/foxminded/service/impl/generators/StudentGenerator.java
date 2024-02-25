package ua.com.foxminded.service.impl.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

@Service
public class StudentGenerator extends DataGenerator {

    private static final Logger logger = LoggerFactory.getLogger(StudentGenerator.class);

    private static final int AMOUNT_OF_STUDENTS = 200;

    private final StudentService studentService;
    private final GroupService groupService;

    public StudentGenerator(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    public void generateStudentsIfEmpty() {
        if (studentService.count() == 0) {
            generateStudents();
        }
    }

    private void generateStudents() {
        for (int i = 0; i < AMOUNT_OF_STUDENTS; i++) {
            StudentDTO studentDTO = new StudentDTO();
            fillUserFields(studentDTO);
            int AMOUNT_OF_GROUPS = 20;
            Group randomGroup = groupService.findById(Math.abs(random.nextLong() % AMOUNT_OF_GROUPS) + 1);
            studentDTO.setGroup(randomGroup);
            logger.info("Created student: {} {}", studentDTO.getFirstName(), studentDTO.getLastName());
            studentService.save(studentDTO);
        }
    }
}