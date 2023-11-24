package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.foxminded.entity.Administrator;

public interface AdministratorRepository extends CrudRepository<Administrator, Long> {

}
