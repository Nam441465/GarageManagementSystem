package dao;

import java.util.List;

public interface GenericDAO<T> {
    void add(T entity);
    void update(T entity);
    void delete(int id);
    T findById(int id);
    List<T> findAll();
    boolean existsById(int id);
}
