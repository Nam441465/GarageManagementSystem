package service;

import dao.PriceListDAO;
import dao.ServiceDAO;
import dao.impl.PriceListDAOImpl;
import dao.impl.ServiceDAOImpl;
import model.PriceList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class PriceListService {

    private final PriceListDAO priceListDAO;
    private final ServiceDAO serviceDAO;

    public PriceListService() {
        this(
                new PriceListDAOImpl(),
                new ServiceDAOImpl());
    }

    public PriceListService(
            PriceListDAO priceListDAO,
            ServiceDAO serviceDAO) {

        this.priceListDAO = Objects.requireNonNull(
                priceListDAO,
                "priceListDAO is required");

        this.serviceDAO = Objects.requireNonNull(
                serviceDAO,
                "serviceDAO is required");
    }

    public boolean addPrice(
            int serviceId,
            String vehicleType,
            String vehicleBrand,
            BigDecimal price,
            LocalDate effectiveFrom,
            LocalDate effectiveTo,
            String note) {

        validatePriceData(
                serviceId,
                vehicleType,
                vehicleBrand,
                price,
                effectiveFrom,
                effectiveTo);

        if (!serviceDAO.existsById(serviceId)) {
            throw new IllegalArgumentException(
                    "Service not found");
        }

        PriceList priceList = new PriceList(
                serviceId,
                vehicleType,
                vehicleBrand,
                price,
                effectiveFrom,
                effectiveTo,
                note);

        boolean created = priceListDAO.addPriceList(priceList);

        if (!created) {
            throw new IllegalStateException(
                    "Failed to create price list");
        }

        return true;
    }

    public PriceList getPrice(int priceId) {

        if (priceId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid price list id");
        }

        PriceList priceList = priceListDAO.findById(priceId);

        if (priceList == null) {
            throw new IllegalArgumentException(
                    "Price list not found");
        }

        return priceList;
    }

    public List<PriceList> getAllPrices() {
        return priceListDAO.findAll();
    }

    public List<PriceList> getPricesByService(int serviceId) {

        if (serviceId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid service id");
        }

        if (!serviceDAO.existsById(serviceId)) {
            throw new IllegalArgumentException(
                    "Service not found");
        }

        return priceListDAO.findByServiceId(serviceId);
    }

    public boolean updatePrice(PriceList priceList) {

        if (priceList == null) {
            throw new IllegalArgumentException(
                    "Price list is null");
        }

        if (priceList.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid price list id");
        }

        validatePriceData(
                priceList.getServiceId(),
                priceList.getVehicleType(),
                priceList.getVehicleBrand(),
                priceList.getPrice(),
                priceList.getEffectiveFrom(),
                priceList.getEffectiveTo());

        if (!serviceDAO.existsById(
                priceList.getServiceId())) {

            throw new IllegalArgumentException(
                    "Service not found");
        }

        if (priceListDAO.findById(
                priceList.getId()) == null) {

            throw new IllegalArgumentException(
                    "Price list not found");
        }

        boolean updated = priceListDAO.updatePriceList(
                priceList);

        if (!updated) {
            throw new IllegalStateException(
                    "Failed to update price list");
        }

        return true;
    }

    public boolean deletePrice(int priceId) {

        if (priceId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid price list id");
        }

        if (priceListDAO.findById(priceId) == null) {
            throw new IllegalArgumentException(
                    "Price list not found");
        }

        boolean deleted = priceListDAO.deletePriceList(
                priceId);

        if (!deleted) {
            throw new IllegalStateException(
                    "Failed to delete price list");
        }

        return true;
    }

    public PriceList getPriceByServiceVehicleAndBrand(
            int serviceId,
            String vehicleType,
            String vehicleBrand) {

        if (serviceId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid service id");
        }

        if (vehicleType == null
                || vehicleType.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Vehicle type cannot be empty");
        }

        if (vehicleBrand == null
                || vehicleBrand.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Vehicle brand cannot be empty");
        }

        PriceList price = priceListDAO.findByServiceVehicleTypeAndBrand(
                serviceId,
                vehicleType,
                vehicleBrand);

        return price;
    }

    private void validatePriceData(
            int serviceId,
            String vehicleType,
            String vehicleBrand,
            BigDecimal price,
            LocalDate effectiveFrom,
            LocalDate effectiveTo) {

        if (serviceId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid service id");
        }

        if (vehicleType == null
                || vehicleType.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Vehicle type cannot be empty");
        }

        if (vehicleBrand == null
                || vehicleBrand.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Vehicle brand cannot be empty");
        }

        if (price == null) {
            throw new IllegalArgumentException(
                    "Price cannot be null");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be negative");
        }

        if (effectiveFrom == null) {
            throw new IllegalArgumentException(
                    "Effective from date cannot be null");
        }

        if (effectiveTo != null
                && effectiveTo.isBefore(effectiveFrom)) {

            throw new IllegalArgumentException(
                    "Effective to date cannot be before effective from date");
        }
    }
}