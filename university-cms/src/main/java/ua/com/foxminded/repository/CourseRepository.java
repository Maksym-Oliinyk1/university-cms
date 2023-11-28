package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Course;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {

}
