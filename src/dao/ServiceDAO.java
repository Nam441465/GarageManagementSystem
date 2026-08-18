package dao;

import java.util.List;
import model.Service;

public interface ServiceDAO extends BaseDAO<Service> {

    void addService(Service service);

    void updateService(Service service);

    void deleteService(int id);

    Service findById(int id);

    List<Service> findAll();

    boolean existsById(int id);

    boolean existsByName(String name);

    int countServices();

    @Override
    default boolean create(Service value) {
        addService(value);
        return true;
    }

    @Override
    default Service read(int id) {
        return findById(id);
    }

    @Override
    default List<Service> readAll() {
        return findAll();
    }

    @Override
    default boolean update(Service value) {
        updateService(value);
        return true;
    }

    @Override
    default boolean delete(int id) {
        deleteService(id);
        return true;
    }
}