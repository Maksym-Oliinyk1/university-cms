package ua.com.foxminded.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Student;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository
        extends PagingAndSortingRepository<Student, UUID>, CrudRepository<Student, UUID> {
  Page<Student> findByGroupId(UUID groupId, Pageable pageable);

  Optional<Student> findByEmail(String email);
}
