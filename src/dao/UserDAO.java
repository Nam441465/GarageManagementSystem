package dao;

import java.util.List;
import model.User;

public interface UserDAO extends BaseDAO<User> {

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(int id);

    User findById(int id);

    List<User> findAll();

    User findByUsername(String username);

    User login(String username, String password);

    boolean changePassword(int userId, String newPassword);

    boolean existsById(int id);

    boolean existsByUsername(String username);

    int countUsers();

    @Override default boolean create(User value) { addUser(value); return true; }
    @Override default User read(int id) { return findById(id); }
    @Override default List<User> readAll() { return findAll(); }
    @Override default boolean update(User value) { updateUser(value); return true; }
    @Override default boolean delete(int id) { deleteUser(id); return true; }
}
