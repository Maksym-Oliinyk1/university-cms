package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.repository.CourseRepository;
import ua.com.foxminded.repository.LectureRepository;
import ua.com.foxminded.service.CourseService;

import java.util.List;
import java.util.Optional;

import static ua.com.foxminded.utill.NameValidator.isValidName;

@Service
public class CourseServiceImpl implements CourseService {
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
            throw new RuntimeException("There is no such course");
        }
    }

    @Override
    public List<Course> findAll() {
        logger.info("Find all courses");
        return (List<Course>) courseRepository.findAll();
    }
}
