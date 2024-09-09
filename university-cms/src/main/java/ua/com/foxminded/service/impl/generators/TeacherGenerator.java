package ua.com.foxminded.service.impl.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.enums.Authorities;
import ua.com.foxminded.service.TeacherService;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherGenerator extends DataGenerator {

    private static final Logger logger = LoggerFactory.getLogger(TeacherGenerator.class);
    private static final String ACADEMIC_DEGREES_DIRECTORY = "/populate/education_degrees";

    private static final List<String> ACADEMIC_DEGREES =
            readFilePerOneLine(ACADEMIC_DEGREES_DIRECTORY);

    private static final int AMOUNT_OF_TEACHERS = 30;

    private final TeacherService teacherService;

    public TeacherGenerator(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    public void generateIfEmpty() {
        if (teacherService.count() == 0) {
            generateTeachers();
        }
    }

    @Override
    public int getOrder() {
        return 3;
    }

    private void generateTeachers() {
        for (int i = 0; i < AMOUNT_OF_TEACHERS; i++) {
            TeacherDTO teacherDTO = new TeacherDTO();
            fillUserFields(teacherDTO);
            teacherDTO.setAuthority(Authorities.TEACHER);
            String academicDegree = ACADEMIC_DEGREES.get(random.nextInt(ACADEMIC_DEGREES.size()));
            teacherDTO.setAcademicDegree(academicDegree);
            teacherDTO.setLectures(new ArrayList<>());
            logger.info("Created teacher: {} {}", teacherDTO.getFirstName(), teacherDTO.getLastName());
            teacherService.save(teacherDTO);
        }
    }
}
