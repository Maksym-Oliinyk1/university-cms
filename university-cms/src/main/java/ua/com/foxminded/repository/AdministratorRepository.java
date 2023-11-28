package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Administrator;

@Repository
public interface AdministratorRepository extends CrudRepository<Administrator, Long> {

}
