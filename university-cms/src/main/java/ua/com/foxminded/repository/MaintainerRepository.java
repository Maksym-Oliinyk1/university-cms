package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Maintainer;

import java.util.Optional;

@Repository
public interface MaintainerRepository
        extends PagingAndSortingRepository<Maintainer, Long>, CrudRepository<Maintainer, Long> {
  Optional<Maintainer> findByEmail(String email);
}
