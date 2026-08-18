package dao;

import java.util.List;

public interface BaseDAO<T> {
    
    boolean create(T obj);
    
    T read(int id);
    
    List<T> readAll();
    
    boolean update(T obj);
    
    boolean delete(int id);
}
