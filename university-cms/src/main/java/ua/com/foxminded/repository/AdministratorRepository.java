package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Administrator;

import java.util.Optional;

@Repository
public interface AdministratorRepository
        extends PagingAndSortingRepository<Administrator, Long>, CrudRepository<Administrator, Long> {
    Optional<Administrator> findByEmail(String email);
}
