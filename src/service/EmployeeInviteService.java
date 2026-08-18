package service;

import dao.EmployeeInviteDAO;
import dao.impl.EmployeeInviteDAOImpl;
import enums.InviteStatus;
import model.EmployeeInvite;

import java.util.List;

public class EmployeeInviteService {

    private final EmployeeInviteDAO dao;

    public EmployeeInviteService() {
        this(new EmployeeInviteDAOImpl());
    }

    public EmployeeInviteService(EmployeeInviteDAO dao) {
        this.dao = java.util.Objects.requireNonNull(dao, "employeeInviteDAO is required");
    }

    public boolean createInvite(EmployeeInvite invite) {
        if (invite == null
                || invite.getInviteCode() == null
                || invite.getInviteCode().trim().length() < 6) {
            return false;
        }

        String inviteCode = invite.getInviteCode().trim();

        if (dao.findByCode(inviteCode) != null) {
            return false;
        }

        invite.setInviteCode(inviteCode);
        invite.setStatus(InviteStatus.UNUSED.name());

        return dao.addInvite(invite);
    }

    public EmployeeInvite findByCode(String inviteCode) {
        if (inviteCode == null || inviteCode.trim().isEmpty()) {
            return null;
        }

        return dao.findByCode(inviteCode.trim());
    }

    public boolean useInvite(String inviteCode) {
        if (inviteCode == null || inviteCode.trim().isEmpty()) {
            return false;
        }

        inviteCode = inviteCode.trim();

        EmployeeInvite existing = dao.findByCode(inviteCode);

        if (existing == null) {
            return false;
        }

        if (!InviteStatus.UNUSED.name().equalsIgnoreCase(existing.getStatus())) {
            return false;
        }

        return dao.updateStatus(inviteCode, InviteStatus.USED.name());
    }

    public List<EmployeeInvite> findAll() {
        return dao.findAll();
    }

    public boolean deleteInvite(int id) {
        return id > 0 && dao.delete(id);
    }
}
