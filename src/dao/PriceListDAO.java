package dao;

import model.PriceList;
import java.util.List;

public interface PriceListDAO extends BaseDAO<PriceList> {

    boolean addPriceList(PriceList obj);

    PriceList findById(int id);

    List<PriceList> findAll();

    PriceList findByServiceAndVehicleType(int serviceId, String vehicleType);

    List<PriceList> findByServiceId(int serviceId);

    boolean updatePriceList(PriceList obj);

    boolean deletePriceList(int id);

    @Override default boolean create(PriceList value) { return addPriceList(value); }
    @Override default PriceList read(int id) { return findById(id); }
    @Override default List<PriceList> readAll() { return findAll(); }
    @Override default boolean update(PriceList value) { return updatePriceList(value); }
    @Override default boolean delete(int id) { return deletePriceList(id); }
}
