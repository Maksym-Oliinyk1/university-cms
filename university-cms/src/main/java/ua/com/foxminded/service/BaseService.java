package ua.com.foxminded.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseService<E> {
    void save(E e);

    void delete(Long id);

    E findById(Long id);

    List<E> findAll();

    Page<E> findAll(Pageable pageable);
}
