package ua.com.foxminded.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.repository.CourseRepository;
import ua.com.foxminded.repository.LectureRepository;
import ua.com.foxminded.service.CourseService;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    private static final String FILTER_ON_THE_MINIMUM_NUMBER_OF_LETTERS = ".*[a-zA-Z].*[a-zA-Z].*[a-zA-Z].*";

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, LectureRepository lectureRepository) {
        this.courseRepository = courseRepository;
        this.lectureRepository = lectureRepository;
    }

    @Override
    public void create(Course course) {
        if (isValidName(course.getName())) {
            courseRepository.save(course);
            logger.info("Created course: {}", course.getName());
        } else {
            logger.error("Invalid name for course: {}", course.getName());
            throw new RuntimeException("Invalid name for course");
        }
    }

    @Override
    public void update(Course course) {
        if (isValidName(course.getName())) {
            courseRepository.save(course);
            logger.info("Updated course: {}", course.getName());
        }
    }

    @Override
    public void delete(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            logger.info("The course was deleted by id {}", id);
        } else {
            logger.error("The course was not found by id {}", id);
            throw new RuntimeException("There is no such course");
        }
    }

    @Override
    public Course findById(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            logger.info("The course was found by id {}", id);
            return optionalCourse.get();
        } else {
            logger.error("The course was not found by id {}", id);
            throw new RuntimeException("There is no such course");
        }
    }

    @Override
    public List<Course> findAll() {
        logger.info("Find all courses");
        return (List<Course>) courseRepository.findAll();
    }

    @Override
    @Transactional
    public void attachLectureToCourse(Long lectureId, Long courseId) {
        Lecture lecture = lectureRepository
                .findById(lectureId)
                .orElseThrow(EntityNotFoundException::new);
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(EntityNotFoundException::new);

        lecture.setCourse(course);
        course.getLectures().add(lecture);
        lectureRepository.save(lecture);
        courseRepository.save(course);
        logger.info("Lecture {} was added to the course {}", lecture.getName(), course.getName());
    }

    @Override
    @Transactional
    public void detachLectureFromCourse(Long lectureId, Long courseId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(EntityNotFoundException::new);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(EntityNotFoundException::new);

        lecture.setCourse(null);
        course.getLectures().remove(lecture);
        lectureRepository.save(lecture);
        courseRepository.save(course);
        logger.info("Lecture {}, has been removed from {} course", lecture.getName(), course.getName());
    }

    private boolean isValidName(String name) {
        return name.matches(FILTER_ON_THE_MINIMUM_NUMBER_OF_LETTERS);
    }
}
