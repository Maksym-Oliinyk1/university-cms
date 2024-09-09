package ua.com.foxminded.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ua.com.foxminded.enums.Authorities;
import ua.com.foxminded.enums.Gender;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "administrators")
public class Administrator extends User {

  public Administrator(
          Long id,
          String firstName,
          String lastName,
          Gender gender,
          LocalDate birthDate,
          String email,
          String imageName,
          String password,
          Authorities authority) {
    super(firstName, lastName, gender, birthDate, email, imageName, password, authority);
    this.id = id;
  }

  public Administrator() {
  }

  @Override
  public String toString() {
    return "Administrator{"
            + "firstName='"
            + firstName
            + '\''
            + ", lastName='"
            + lastName
            + '\''
            + ", email='"
            + email
            + '\''
            + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Administrator administrator = (Administrator) o;
    return id.equals(administrator.id)
            && firstName.equals(administrator.firstName)
            && lastName.equals(administrator.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName);
  }
}
