package ua.com.foxminded.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Course;

@Repository
public interface CourseRepository
        extends PagingAndSortingRepository<Course, Long>, CrudRepository<Course, Long> {
  Page<Course> findByFacultyId(Long facultyId, Pageable pageable);
}
