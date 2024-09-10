package ua.com.foxminded.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeacherRepository
        extends PagingAndSortingRepository<Teacher, UUID>, CrudRepository<Teacher, UUID> {

    @Query(
            "SELECT l FROM Lecture l JOIN l.teacher t WHERE t.id = :teacherId AND l.date BETWEEN :startDateTime AND :endDateTime")
    List<Lecture> findLecturesByDateBetween(
            @Param("teacherId") UUID teacherId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT COUNT(l) FROM Lecture l JOIN l.teacher t WHERE t.id = :teacherId")
    Long countLecturesByTeacher(@Param("teacherId") UUID teacherId);

    Optional<Teacher> findByEmail(String email);
}
