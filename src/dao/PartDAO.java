package dao;

import model.Part;
import java.util.List;

public interface PartDAO extends BaseDAO<Part> {
    
    boolean addPart(Part obj);
    
    Part findById(int id);
    
    List<Part> findAll();
    
    boolean updatePart(Part obj);
    
    boolean deletePart(int id);

    @Override default boolean create(Part value) { return addPart(value); }
    @Override default Part read(int id) { return findById(id); }
    @Override default List<Part> readAll() { return findAll(); }
    @Override default boolean update(Part value) { return updatePart(value); }
    @Override default boolean delete(int id) { return deletePart(id); }
}
