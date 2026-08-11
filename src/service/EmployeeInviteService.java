package service;

import model.EmployeeInvite;
import java.util.List;

public interface EmployeeInviteService {

    boolean createInvite(EmployeeInvite invite);

    EmployeeInvite findByCode(String inviteCode);

    boolean useInvite(String inviteCode);

    List<EmployeeInvite> findAll();

    boolean deleteInvite(int id);

}
