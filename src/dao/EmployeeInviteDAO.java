package dao;
import java.util.List;
import model.EmployeeInvite;
public interface EmployeeInviteDAO {

    boolean addInvite(EmployeeInvite invite);

    EmployeeInvite findByCode(String inviteCode);

    boolean updateStatus(String inviteCode, String status);

    List<EmployeeInvite> findAll();

    boolean delete(int id);

}
