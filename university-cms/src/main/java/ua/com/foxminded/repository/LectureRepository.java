package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.foxminded.entity.Lecture;

public interface LectureRepository extends CrudRepository<Lecture, Long> {

}
