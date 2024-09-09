package ua.com.foxminded.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.entity.Lecture;

public interface LectureService extends EntityService<Lecture> {
    void attachGroupToLecture(Long groupId, Long lectureId);

    void detachGroupFromLecture(Long groupId, Long lectureId);

    Page<Lecture> findAllByCourse(Long courseId, Pageable pageable);

    Page<Lecture> findAllByGroup(Long groupId, Pageable pageable);
}
