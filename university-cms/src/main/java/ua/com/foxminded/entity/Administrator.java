package ua.com.foxminded.entity;

import jakarta.persistence.*;
import ua.com.foxminded.enums.Gender;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "administrators")
public class Administrator extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "administrator_id")
    private Long id;

    public Administrator(Long id, String firstName, String lastName, Gender gender, LocalDate birthDate, String email, String imageName) {
        super(firstName, lastName, gender, birthDate, email, imageName);
        this.id = id;
    }

    public Administrator() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Administrator that = (Administrator) o;
        return id.equals(that.id) && firstName.equals(that.firstName) && lastName.equals(that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }


    @Override
    public String toString() {
        return "Administrator{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
