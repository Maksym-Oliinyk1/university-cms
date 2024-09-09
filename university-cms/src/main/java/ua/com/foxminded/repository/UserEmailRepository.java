package ua.com.foxminded.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.User;

import java.util.Optional;

@Repository
public interface UserEmailRepository extends JpaRepository<User, Long> {

    @Query(
            "SELECT DISTINCT "
                    + "CASE "
                    + "WHEN s.email = :searchEmail THEN s "
                    + "WHEN t.email = :searchEmail THEN t "
                    + "WHEN a.email = :searchEmail THEN a "
                    + "WHEN m.email = :searchEmail THEN m "
                    + "END "
                    + "FROM "
                    + "Student s "
                    + "LEFT JOIN Teacher t ON s.email = t.email "
                    + "LEFT JOIN Administrator a ON s.email = a.email "
                    + "LEFT JOIN Maintainer m ON s.email = m.email "
                    + "WHERE "
                    + "s.email = :searchEmail OR t.email = :searchEmail OR a.email = :searchEmail OR m.email = :searchEmail")
    Optional<User> findByEmail(String searchEmail);
}
