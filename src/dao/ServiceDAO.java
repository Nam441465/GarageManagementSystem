package dao;

import java.util.List;
import model.Service;
public interface ServiceDAO {

    void addService(Service service);

    void updateService(Service service);

    void deleteService(int id);

    Service findById(int id);

    List<Service> findAll();

    List<Service> findByPriceRange(double minPrice, double maxPrice);

    boolean existsById(int id);

    boolean existsByName(String name);

    int countServices();
}
