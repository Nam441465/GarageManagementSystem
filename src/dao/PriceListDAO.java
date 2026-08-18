package dao;

import java.util.List;

import model.PriceList;

public interface PriceListDAO extends BaseDAO<PriceList> {

    boolean addPriceList(PriceList obj);

    PriceList findById(int id);

    List<PriceList> findAll();

    List<PriceList> findByServiceId(int serviceId);

    boolean updatePriceList(PriceList obj);

    PriceList findByServiceVehicleTypeAndBrand(
            int serviceId,
            String vehicleType,
            String vehicleBrand);

    boolean deletePriceList(int id);

    @Override
    default boolean create(PriceList value) {
        return addPriceList(value);
    }

    @Override
    default PriceList read(int id) {
        return findById(id);
    }

    @Override
    default List<PriceList> readAll() {
        return findAll();
    }

    @Override
    default boolean update(PriceList value) {
        return updatePriceList(value);
    }

    @Override
    default boolean delete(int id) {
        return deletePriceList(id);
    }
}