package ua.com.foxminded.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;

import java.time.LocalDateTime;
import java.util.List;

public interface TeacherRepository extends CrudRepository<Teacher, Long> {

    @Query("SELECT l FROM Lecture l JOIN Teacher t WHERE t.id = :teacherId AND l.date BETWEEN :startDateTime AND :endDateTime")
    List<Lecture> findLecturesByDateBetween(@Param("teacherId") Long teacherId,
                                            @Param("startDateTime") LocalDateTime startDateTime,
                                            @Param("endDateTime") LocalDateTime endDateTime);
    @Query("SELECT COUNT(l) FROM Lecture l JOIN l.teacher t WHERE t.id = :teacherId")
    Long countLecturesByTeacher(@Param("teacherId") Long teacherId);
}
