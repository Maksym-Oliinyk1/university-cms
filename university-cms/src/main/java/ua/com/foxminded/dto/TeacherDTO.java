package ua.com.foxminded.dto;

import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.enums.Gender;

import java.util.ArrayList;
import java.util.List;

public class TeacherDTO extends UserDTO {

    private List<Lecture> lectures;
    private String academicDegree;

    public TeacherDTO(Long id, String firstName, String lastName, Gender gender, String academicDegree, int age, String email, MultipartFile multipartFile) {
        super(id, firstName, lastName, gender, age, email, multipartFile);
        this.academicDegree = academicDegree;
        this.lectures = new ArrayList<>();
    }

    public TeacherDTO() {

    }

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
