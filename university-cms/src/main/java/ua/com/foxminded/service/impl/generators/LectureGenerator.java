package ua.com.foxminded.service.impl.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.LectureService;
import ua.com.foxminded.service.TeacherService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LectureGenerator extends DataGenerator {

  private static final Logger logger = LoggerFactory.getLogger(LectureGenerator.class);
  private static final String LECTURE_NAMES_DIRECTORY = "/populate/lectures_names";
  private static final String LECTURE_DESCRIPTION_DIRECTORY = "/populate/lectures_descriptions";
  private static final List<String> LECTURE_NAMES = readFilePerOneLine(LECTURE_NAMES_DIRECTORY);
  private static final List<String> LECTURE_DESCRIPTIONS =
          readFilePerOneLine(LECTURE_DESCRIPTION_DIRECTORY);
  private static final int AMOUNT_OF_LECTURES = 40;
  private static final int AMOUNT_OF_RANDOM_GROUPS = 5;
  private static final int AMOUNT_OF_TEACHERS = 30;
  private static final int AMOUNT_OF_COURSES = 30;
  private static final int AMOUNT_OF_GROUPS = 20;

  private final LectureService lectureService;
  private final CourseService courseService;
  private final TeacherService teacherService;
  private final GroupService groupService;

  public LectureGenerator(
          LectureService lectureService,
          CourseService courseService,
          TeacherService teacherService,
          GroupService groupService) {
    this.lectureService = lectureService;
    this.courseService = courseService;
    this.teacherService = teacherService;
    this.groupService = groupService;
  }

  public void generateIfEmpty() {
    if (lectureService.count() == 0) {
      generateLectures();
    }
  }

  @Override
  public int getOrder() {
    return 8;
  }

  private void generateLectures() {
    for (int i = 0; i < AMOUNT_OF_LECTURES; i++) {
      String name = LECTURE_NAMES.get(i % LECTURE_NAMES.size());
      String description = LECTURE_DESCRIPTIONS.get(i % LECTURE_DESCRIPTIONS.size());
      LocalDateTime randomDate = generateRandomDate();
      Course randomCourse =
              courseService.findById(Math.abs(random.nextLong() % AMOUNT_OF_COURSES) + 1);
      Teacher randomTeacher =
              teacherService.findById(Math.abs(random.nextLong() % AMOUNT_OF_TEACHERS) + 1);
      List<Group> groups = getRandomGroups();

      Lecture lecture =
              new Lecture(null, randomCourse, randomTeacher, name, description, randomDate);
      lecture.setGroups(groups);
      logger.info("Created lecture: {}", lecture.getName());
      lectureService.save(lecture);
    }
  }

  private LocalDateTime generateRandomDate() {
    LocalDateTime startDate = LocalDateTime.now();
    LocalDateTime randomDate = startDate.plusDays(random.nextInt(365));
    int[] fixedHours = {8, 10, 12, 14, 16, 18};
    randomDate = randomDate.withHour(fixedHours[random.nextInt(fixedHours.length)]);
    return randomDate;
  }

  private List<Group> getRandomGroups() {
    List<Group> groups = new ArrayList<>();

    for (int i = 0; i < AMOUNT_OF_RANDOM_GROUPS; i++) {
      Group randomGroup = groupService.findById(Math.abs(random.nextLong() % AMOUNT_OF_GROUPS) + 1);
      groups.add(randomGroup);
    }

    return groups;
  }
}
