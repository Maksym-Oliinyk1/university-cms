package ua.com.foxminded.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.repository.CourseRepository;
import ua.com.foxminded.repository.FacultyRepository;
import ua.com.foxminded.service.FacultyService;

import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public FacultyServiceImpl(
            FacultyRepository facultyRepository, CourseRepository courseRepository) {
        this.facultyRepository = facultyRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void save(Faculty faculty) {
        facultyRepository.save(faculty);
        logger.info("Saved faculty {}", faculty.getName());
    }

    @Override
    public void update(Long id, Faculty updatedFaculty) {
        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);
        if (optionalFaculty.isPresent()) {
            Faculty existingFaculty = optionalFaculty.get();
            existingFaculty.setName(updatedFaculty.getName());
            existingFaculty.setCourses(updatedFaculty.getCourses());
            facultyRepository.save(existingFaculty);
            logger.info("Faculty updated by id: {}", id);
        } else {
            throw new RuntimeException("There is no such faculty");
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
    public Page<Faculty> findAll(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int from = pageNumber * pageSize;
        int to = from + pageSize;
        logger.info("Find faculties from {} to {}", from, to);
        return facultyRepository.findAll(pageable);
    }

    @Override
    public Long count() {
        return facultyRepository.count();
    }

    @Override
    @Transactional
    public void attachCourseToFaculty(Long courseId, Long facultyId) {
        Faculty faculty =
                facultyRepository.findById(facultyId).orElseThrow(EntityNotFoundException::new);
        Course course = courseRepository.findById(courseId).orElseThrow(EntityNotFoundException::new);

        course.setFaculty(faculty);
        faculty.getCourses().add(course);
        courseRepository.save(course);
        facultyRepository.save(faculty);
        logger.info("Course {} was added to the faculty {}", course.getName(), faculty.getName());
    }

    @Override
    @Transactional
    public void detachCourseFromFaculty(Long courseId, Long facultyId) {
        Faculty faculty =
                facultyRepository.findById(facultyId).orElseThrow(EntityNotFoundException::new);
        Course course = courseRepository.findById(courseId).orElseThrow(EntityNotFoundException::new);

        course.setFaculty(null);
        faculty.getCourses().remove(course);
        courseRepository.save(course);
        facultyRepository.save(faculty);
        logger.info("Course {} was removed from the faculty {}", course.getName(), faculty.getName());
    }
}
