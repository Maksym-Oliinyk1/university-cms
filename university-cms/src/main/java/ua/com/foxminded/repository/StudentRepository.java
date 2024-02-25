package ua.com.foxminded.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Student;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, Long>, CrudRepository<Student, Long> {
    Page<Student> findByGroupId(Long groupId, Pageable pageable);
}
