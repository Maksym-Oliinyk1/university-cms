package ua.com.foxminded.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseService<E> {

    void delete(Long id);

    E findById(Long id);

    Page<E> findAll(Pageable pageable);

}
