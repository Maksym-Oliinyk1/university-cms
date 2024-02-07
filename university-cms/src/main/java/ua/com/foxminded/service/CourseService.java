package ua.com.foxminded.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.entity.Course;

public interface CourseService extends EntityService<Course> {
    Page<Course> findAllOfFaculty(Long facultyId, Pageable pageable);
}
