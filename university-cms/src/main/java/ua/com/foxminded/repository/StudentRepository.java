package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Student;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

}
