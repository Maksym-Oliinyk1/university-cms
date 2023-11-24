package ua.com.foxminded.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.repository.CourseRepository;
import ua.com.foxminded.repository.FacultyRepository;
import ua.com.foxminded.service.FacultyService;

import java.util.List;
import java.util.Optional;

import static ua.com.foxminded.utill.NameValidator.isValidName;

@Service
public class FacultyServiceImpl implements FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository, CourseRepository courseRepository) {
        this.facultyRepository = facultyRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void create(Faculty faculty) {
        if (isValidName(faculty.getName())) {
            facultyRepository.save(faculty);
            logger.info("Created faculty {}", faculty.getName());
        } else {
            throw new RuntimeException("Invalid name for faculty");
        }
    }

    @Override
    public void update(Faculty faculty) {
        if (isValidName(faculty.getName())) {
            facultyRepository.save(faculty);
            logger.info("Updated faculty {}", faculty.getName());
        } else {
            throw new RuntimeException("Invalid name for faculty");
        }
    }

    @Override
    public void delete(Long id) {
        if (facultyRepository.existsById(id)) {
            facultyRepository.deleteById(id);
            logger.info("The faculty was deleted by id {}", id);
        } else {
            throw new RuntimeException("There is no such faculty");
        }
    }

    @Override
    public Faculty findById(Long id) {
        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);
        if (optionalFaculty.isPresent()) {
            logger.info("The faculty was fount by id {}", id);
            return optionalFaculty.get();
        } else {
            throw new RuntimeException("The is no such faculty");
        }
    }

    @Override
    public List<Faculty> findAll() {
        return (List<Faculty>) facultyRepository.findAll();
    }

    @Override
    @Transactional
    public void attachCourseToFaculty(Long courseId, Long facultyId) {
        Faculty faculty = facultyRepository
                .findById(facultyId)
                .orElseThrow(EntityNotFoundException::new);
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(EntityNotFoundException::new);

        course.setFaculty(faculty);
        faculty.getCourses().add(course);
        courseRepository.save(course);
        facultyRepository.save(faculty);
        logger.info("Course {} was added to the faculty {}", course.getName(), faculty.getName());
    }

    @Override
    @Transactional
    public void detachCourseFromFaculty(Long courseId, Long facultyId) {
        Faculty faculty = facultyRepository
                .findById(facultyId)
                .orElseThrow(EntityNotFoundException::new);
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(EntityNotFoundException::new);

        course.setFaculty(null);
        faculty.getCourses().remove(course);
        courseRepository.save(course);
        facultyRepository.save(faculty);
        logger.info("Course {} was removed from the faculty {}", course.getName(), faculty.getName());
    }
}
