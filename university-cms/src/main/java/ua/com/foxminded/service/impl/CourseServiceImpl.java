package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.repository.CourseRepository;
import ua.com.foxminded.service.CourseService;

import java.util.Optional;

import static ua.com.foxminded.utill.NameValidator.isValidNameForUniversityEntity;

@Service
public class CourseServiceImpl implements CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void save(Course course) {
        if (isValidNameForUniversityEntity(course.getName())) {
            courseRepository.save(course);
            logger.info("Saved course: {}", course.getName());
        } else {
            throw new RuntimeException("Invalid name for course");
        }
    }

    @Override
    public void update(Long id, Course updatedCourse) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();
            existingCourse.setName(updatedCourse.getName());
            existingCourse.setLectures(updatedCourse.getLectures());
            existingCourse.setFaculty(updatedCourse.getFaculty());
            courseRepository.save(existingCourse);
            logger.info("Course updated by id: {}", id);
        } else {
            throw new RuntimeException("There is no such course");
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
    public Page<Course> findAll(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int from = pageNumber * pageSize;
        int to = from + pageSize;
        logger.info("Find courses from {} to {}", from, to);
        return courseRepository.findAll(pageable);
    }
}
