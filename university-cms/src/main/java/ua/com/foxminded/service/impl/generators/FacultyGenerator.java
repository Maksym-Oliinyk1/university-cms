package ua.com.foxminded.service.impl.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.FacultyService;

import java.util.List;

@Service
public class FacultyGenerator extends DataGenerator {
    private static final Logger logger = LoggerFactory.getLogger(FacultyGenerator.class);
    private static final String FACULTY_NAMES_DIRECTORY = "/populate/faculties_names";
    private static final List<String> FACULTIES_NAMES = readFilePerOneLine(FACULTY_NAMES_DIRECTORY);


    private final FacultyService facultyService;

    public FacultyGenerator(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    public void generateIfEmpty() {
        if (facultyService.count() == 0) {
            generateFaculties();
        }
    }

    @Override
    public int getOrder() {
        return 4;
    }

    private void generateFaculties() {
        int AMOUNT_OF_FACULTIES = 20;
        for (int i = 0; i < AMOUNT_OF_FACULTIES; i++) {
            String facultyName = FACULTIES_NAMES.get(random.nextInt(FACULTIES_NAMES.size()));
            Faculty faculty = new Faculty();
            faculty.setName(facultyName);
            logger.info("Created faculty: {}", faculty.getName());
            facultyService.save(faculty);
        }
    }
}