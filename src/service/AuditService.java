package service;

import dao.AuditLogDAO;
import dao.impl.AuditLogDAOImpl;
import model.AuditLog;

import java.util.List;

public class AuditService {

    private final AuditLogDAO auditLogDAO;

    public AuditService() { this(new AuditLogDAOImpl()); }
    public AuditService(AuditLogDAO auditLogDAO) { this.auditLogDAO = java.util.Objects.requireNonNull(auditLogDAO, "auditLogDAO is required"); }

    public boolean logAction(int userId, String action, String entityName, int entityId, String oldValue,
            String newValue, String ipAddress, String device) {
        AuditLog log = new AuditLog(userId, action, entityName, entityId, oldValue, newValue, ipAddress, device);
        return auditLogDAO.addAuditLog(log);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogDAO.findAll();
    }

    public List<AuditLog> getLogsByUser(int userId) {
        return auditLogDAO.findByUserId(userId);
    }

    public List<AuditLog> getLogsByEntity(String entityName) {
        return auditLogDAO.findByEntityName(entityName);
    }

    public AuditLog getLog(int logId) {
        return auditLogDAO.findById(logId);
    }

    public boolean deleteLog(int logId) {
        return auditLogDAO.deleteAuditLog(logId);
    }
}
