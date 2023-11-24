package ua.com.foxminded.service;


import java.util.List;

public interface BaseService<E> {
    void create(E e);

    void update(E e);

    void delete(Long id);

    E findById(Long id);

    List<E> findAll();
}
