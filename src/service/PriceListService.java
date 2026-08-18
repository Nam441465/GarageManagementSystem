package service;

import dao.PriceListDAO;
import dao.impl.PriceListDAOImpl;
import model.PriceList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PriceListService {

    private final PriceListDAO priceListDAO;

    public PriceListService() { this(new PriceListDAOImpl()); }
    public PriceListService(PriceListDAO priceListDAO) { this.priceListDAO = java.util.Objects.requireNonNull(priceListDAO, "priceListDAO is required"); }

    public boolean addPrice(int serviceId, String vehicleType, BigDecimal price, LocalDate effectiveFrom, LocalDate effectiveTo, String note) {
        PriceList priceList = new PriceList(serviceId, vehicleType, price, effectiveFrom, effectiveTo, note);
        return priceListDAO.addPriceList(priceList);
    }

    public PriceList getPrice(int priceId) {
        return priceListDAO.findById(priceId);
    }

    public List<PriceList> getAllPrices() {
        return priceListDAO.findAll();
    }

    public PriceList getPriceByServiceAndVehicle(int serviceId, String vehicleType) {
        return priceListDAO.findByServiceAndVehicleType(serviceId, vehicleType);
    }

    public List<PriceList> getPricesByService(int serviceId) {
        return priceListDAO.findByServiceId(serviceId);
    }

    public boolean updatePrice(PriceList priceList) {
        return priceListDAO.updatePriceList(priceList);
    }

    public boolean deletePrice(int priceId) {
        return priceListDAO.deletePriceList(priceId);
    }
}
