package ua.com.foxminded.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BaseService<D> {

  void delete(UUID id);

  D findById(UUID id);

  Page<D> findAll(Pageable pageable);

  Long count();
}
