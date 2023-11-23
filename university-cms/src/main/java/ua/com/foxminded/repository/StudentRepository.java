package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.foxminded.entity.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {
}
