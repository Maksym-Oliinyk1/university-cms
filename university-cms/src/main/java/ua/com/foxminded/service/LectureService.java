package ua.com.foxminded.service;

import ua.com.foxminded.entity.Lecture;

public interface LectureService extends BaseService<Lecture>{
    void attachGroupToLecture(Long groupId, Long lectureId);
    void detachGroupFromLecture(Long groupId, Long lectureId);
}
