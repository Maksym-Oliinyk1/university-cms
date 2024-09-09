package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Administrator;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdministratorRepository
        extends PagingAndSortingRepository<Administrator, UUID>, CrudRepository<Administrator, UUID> {
  Optional<Administrator> findByEmail(String email);
}
