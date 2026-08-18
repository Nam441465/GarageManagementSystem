package dao;

import model.AuditLog;
import java.util.List;

public interface AuditLogDAO {
    
    boolean addAuditLog(AuditLog obj);
    
    AuditLog findById(int id);
    
    List<AuditLog> findAll();
    
    List<AuditLog> findByUserId(int userId);
    
    List<AuditLog> findByEntityName(String entityName);
    
    boolean deleteAuditLog(int id);
}