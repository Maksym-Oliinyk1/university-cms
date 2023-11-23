package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.foxminded.entity.Course;

public interface CourseRepository extends CrudRepository<Course, Long> {
}
