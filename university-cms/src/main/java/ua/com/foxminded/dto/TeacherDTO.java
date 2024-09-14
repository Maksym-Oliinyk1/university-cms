package ua.com.foxminded.dto;

import ua.com.foxminded.entity.Lecture;

import java.util.List;

public class TeacherDTO extends UserDTO {
  private List<Lecture> lectures;
  private String academicDegree;

  public List<Lecture> getLectures() {
    return lectures;
  }

  public void setLectures(List<Lecture> lectures) {
    this.lectures = lectures;
  }

  public String getAcademicDegree() {
    return academicDegree;
  }

  public void setAcademicDegree(String academicDegree) {
    this.academicDegree = academicDegree;
  }
}
