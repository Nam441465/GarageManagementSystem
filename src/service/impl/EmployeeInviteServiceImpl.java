package service.impl;

import dao.EmployeeInviteDAO;
import dao.impl.EmployeeInviteDAOImpl;
import model.EmployeeInvite;
import service.EmployeeInviteService;

import java.util.List;

public class EmployeeInviteServiceImpl implements EmployeeInviteService {

    private final EmployeeInviteDAO dao = new EmployeeInviteDAOImpl();

    @Override
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
        invite.setStatus("UNUSED");

        return dao.addInvite(invite);
    }

    @Override
    public EmployeeInvite findByCode(String inviteCode) {
        if (inviteCode == null || inviteCode.trim().isEmpty()) {
            return null;
        }

        return dao.findByCode(inviteCode.trim());
    }

    @Override
    public boolean useInvite(String inviteCode) {
        if (inviteCode == null || inviteCode.trim().isEmpty()) {
            return false;
        }

        inviteCode = inviteCode.trim();

        EmployeeInvite existing = dao.findByCode(inviteCode);

        if (existing == null) {
            return false;
        }

        if (!"UNUSED".equalsIgnoreCase(existing.getStatus())) {
            return false;
        }

        return dao.updateStatus(inviteCode, "USED");
    }

    @Override
    public List<EmployeeInvite> findAll() {
        return dao.findAll();
    }

    @Override
    public boolean deleteInvite(int id) {
        return id > 0 && dao.delete(id);
    }
}
