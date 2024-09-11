package ua.com.foxminded.service.impl.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.FacultyService;

import java.util.List;

@Service
public class CourseGenerator extends DataGenerator {

  private static final Logger logger = LoggerFactory.getLogger(CourseGenerator.class);
  private static final String COURSE_NAMES_DIRECTORY = "/populate/course_names";
  private static final List<String> COURSE_NAMES = readFilePerOneLine(COURSE_NAMES_DIRECTORY);
  private static final int AMOUNT_OF_COURSES = 30;
  private static final int AMOUNT_OF_FACULTIES = 20;
  private final CourseService courseService;
  private final FacultyService facultyService;

  public CourseGenerator(CourseService courseService, FacultyService facultyService) {
    this.courseService = courseService;
    this.facultyService = facultyService;
  }

  public void generateIfEmpty() {
    if (courseService.count() == 0) {
      generateCourses();
    }
  }

  @Override
  public int getOrder() {
    return 5;
  }

  private void generateCourses() {
    for (int i = 0; i < AMOUNT_OF_COURSES; i++) {
      String courseName = COURSE_NAMES.get(random.nextInt(COURSE_NAMES.size()));
      Faculty randomFaculty =
              facultyService.findById(Math.abs(random.nextLong() % AMOUNT_OF_FACULTIES) + 1);

      Course course = new Course(null, courseName, randomFaculty);
      logger.info("Created course: {}", course.getName());
      courseService.save(course);
    }
  }
}
