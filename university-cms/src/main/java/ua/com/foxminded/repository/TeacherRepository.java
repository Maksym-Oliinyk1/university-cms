package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.foxminded.entity.Teacher;

public interface TeacherRepository extends CrudRepository<Teacher, Long> {
}
