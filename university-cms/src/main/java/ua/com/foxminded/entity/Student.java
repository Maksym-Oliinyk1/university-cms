package ua.com.foxminded.entity;

import jakarta.persistence.*;
import ua.com.foxminded.enums.Gender;

import java.util.Objects;

@Entity
@Table(name = "students")
public class Student extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student(Long id, String firstName, String lastName, Gender gender, Group group, int age, String email, String imageName) {
        super(firstName, lastName, gender, age, email, imageName);
        this.group = group;
        this.id = id;
    }

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id.equals(student.id) && firstName.equals(student.firstName) && lastName.equals(student.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
