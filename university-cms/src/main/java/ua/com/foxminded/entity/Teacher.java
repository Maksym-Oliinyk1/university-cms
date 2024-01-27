package ua.com.foxminded.entity;

import jakarta.persistence.*;
import ua.com.foxminded.enums.Gender;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teachers")
public class Teacher extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    protected Long id;
    @Column(name = "academic_degree")
    private String academicDegree;
    @OneToMany(mappedBy = "teacher")
    private List<Lecture> lectures;

    public Teacher(Long id, String firstName, String lastName, Gender gender, String academicDegree, int age, String email, String imageName) {
        super(firstName, lastName, gender, age, email, imageName);
        this.id = id;
        this.academicDegree = academicDegree;
        this.lectures = new ArrayList<>();
    }

    public Teacher() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return id.equals(teacher.id) && firstName
                .equals(teacher.firstName) && lastName
                .equals(teacher.lastName) && academicDegree
                .equals(teacher.academicDegree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, academicDegree);
    }


    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", academicDegree='" + academicDegree + '\'' +
                '}';
    }
}
