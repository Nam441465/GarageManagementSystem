package service;

import java.util.List;
import model.User;

public interface UserService {

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
}