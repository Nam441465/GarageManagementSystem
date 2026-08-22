package service;

import dao.PriceListDAO;
import dao.ServiceDAO;
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
                                new PriceListDAO(),
                                new ServiceDAO());
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

        // ==================== UI Input Parsing & Validation ====================

        public int parseServiceId(String text) {
                if (text == null || text.trim().isEmpty()) {
                        throw new IllegalArgumentException("Mã dịch vụ là bắt buộc.");
                }

                try {
                        int id = Integer.parseInt(text.trim());
                        if (id <= 0) {
                                throw new IllegalArgumentException("Mã dịch vụ phải lớn hơn 0.");
                        }
                        return id;
                } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Mã dịch vụ phải là số nguyên.");
                }
        }

        public String parseVehicleType(String vehicleType) {
                if (vehicleType == null) {
                        throw new IllegalArgumentException("Hãy chọn loại xe.");
                }
                return vehicleType;
        }

        public String parseVehicleBrand(String vehicleBrand) {
                if (vehicleBrand == null || vehicleBrand.trim().isEmpty()) {
                        throw new IllegalArgumentException("Hãy chọn hãng xe.");
                }
                return vehicleBrand;
        }

        public BigDecimal parseAmount(String text) {
                if (text == null || text.trim().isEmpty()) {
                        throw new IllegalArgumentException("Giá là bắt buộc.");
                }

                try {
                        BigDecimal amount = new BigDecimal(text.trim());
                        if (amount.signum() < 0) {
                                throw new IllegalArgumentException("Giá không được âm.");
                        }
                        return amount;
                } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Giá phải là số hợp lệ.");
                }
        }

        public void validateDateRange(LocalDate from, LocalDate to) {
                if (from == null) {
                        throw new IllegalArgumentException("Ngày hiệu lực là bắt buộc.");
                }

                if (to != null && to.isBefore(from)) {
                        throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu.");
                }
        }

        // ==================== Business Logic ====================

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
                                        "Không tìm thấy dịch vụ tương ứng.");
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
                                        "Không thể tạo bảng giá mới.");
                }

                return true;
        }

        public PriceList getPrice(int priceId) {

                if (priceId <= 0) {
                        throw new IllegalArgumentException(
                                        "Mã bảng giá không hợp lệ.");
                }

                PriceList priceList = priceListDAO.findById(priceId);

                if (priceList == null) {
                        throw new IllegalArgumentException(
                                        "Không tìm thấy bảng giá.");
                }

                return priceList;
        }

        public List<PriceList> getAllPrices() {
                return priceListDAO.findAll();
        }

        public List<PriceList> getPricesByService(int serviceId) {

                if (serviceId <= 0) {
                        throw new IllegalArgumentException(
                                        "Mã dịch vụ không hợp lệ.");
                }

                if (!serviceDAO.existsById(serviceId)) {
                        throw new IllegalArgumentException(
                                        "Không tìm thấy dịch vụ tương ứng.");
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
                                        "Mã bảng giá không hợp lệ.");
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
                                        "Không tìm thấy dịch vụ tương ứng.");
                }

                if (priceListDAO.findById(
                                priceList.getId()) == null) {

                        throw new IllegalArgumentException(
                                        "Không tìm thấy bảng giá.");
                }

                boolean updated = priceListDAO.updatePriceList(
                                priceList);

                if (!updated) {
                        throw new IllegalStateException(
                                        "Không thể cập nhật bảng giá.");
                }

                return true;
        }

        public boolean deletePrice(int priceId) {

                if (priceId <= 0) {
                        throw new IllegalArgumentException(
                                        "Mã bảng giá không hợp lệ.");
                }

                if (priceListDAO.findById(priceId) == null) {
                        throw new IllegalArgumentException(
                                        "Không tìm thấy bảng giá.");
                }

                boolean deleted = priceListDAO.deletePriceList(
                                priceId);

                if (!deleted) {
                        throw new IllegalStateException(
                                        "Không thể xóa bảng giá.");
                }

                return true;
        }

        public PriceList getPriceByServiceVehicleAndBrand(
                        int serviceId,
                        String vehicleType,
                        String vehicleBrand) {

                if (serviceId <= 0) {
                        throw new IllegalArgumentException(
                                        "Mã dịch vụ không hợp lệ.");
                }

                if (vehicleType == null
                                || vehicleType.trim().isEmpty()) {
                        throw new IllegalArgumentException(
                                        "Loại xe không được để trống.");
                }

                if (vehicleBrand == null
                                || vehicleBrand.trim().isEmpty()) {
                        throw new IllegalArgumentException(
                                        "Hãng xe không được để trống.");
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
                                        "Mã dịch vụ không hợp lệ.");
                }

                if (vehicleType == null
                                || vehicleType.trim().isEmpty()) {

                        throw new IllegalArgumentException(
                                        "Loại xe không được để trống.");
                }

                if (vehicleBrand == null
                                || vehicleBrand.trim().isEmpty()) {

                        throw new IllegalArgumentException(
                                        "Hãng xe không được để trống.");
                }

                if (price == null) {
                        throw new IllegalArgumentException(
                                        "Giá dịch vụ không được để trống.");
                }

                if (price.compareTo(BigDecimal.ZERO) < 0) {
                        throw new IllegalArgumentException(
                                        "Giá dịch vụ không được âm.");
                }

                if (effectiveFrom == null) {
                        throw new IllegalArgumentException(
                                        "Ngày bắt đầu hiệu lực không được để trống.");
                }

                if (effectiveTo != null
                                && effectiveTo.isBefore(effectiveFrom)) {

                        throw new IllegalArgumentException(
                                        "Ngày kết thúc hiệu lực không được trước ngày bắt đầu.");
                }
        }
}