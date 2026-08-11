package service.impl;

import java.util.List;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import model.User;
import service.UserService;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public void addUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }

        if (user.getUsername() == null
                || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username can not be empty");
        }

        if (user.getPassword() == null
                || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password can not be empty");
        }

        if (user.getRole() == null
                || user.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Role can not be empty");
        }

        if ("Owner".equalsIgnoreCase(user.getRole())) {
            throw new IllegalArgumentException("The fixed Owner account cannot be created manually");
        }

        if (userDAO.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        userDAO.addUser(user);
    }

    @Override
    public void updateUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }

        if (user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }

        if (user.getUsername() == null
                || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username can not be empty");
        }

        if (user.getPassword() == null
                || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password can not be empty");
        }

        if (user.getRole() == null
                || user.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Role can not be empty");
        }

        if (!userDAO.existsById(user.getId())) {
            throw new IllegalArgumentException("User not found");
        }

        if (isOwner(user.getId())) {
            throw new IllegalArgumentException("The fixed Owner account cannot be modified");
        }

        userDAO.updateUser(user);
    }

    @Override
    public void deleteUser(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }

        if (!userDAO.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }

        if (isOwner(id)) {
            throw new IllegalArgumentException("The fixed Owner account cannot be deleted");
        }

        userDAO.deleteUser(id);
    }

    @Override
    public User findById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }

        return userDAO.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User findByUsername(String username) {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username can not be empty");
        }

        return userDAO.findByUsername(username);
    }

    @Override
    public User login(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username can not be empty");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password can not be empty");
        }

        return userDAO.login(username, password);
    }

    @Override
    public boolean changePassword(int userId, String newPassword) {

        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }

        if (newPassword == null
                || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password can not be empty");
        }

        if (!userDAO.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        return userDAO.changePassword(userId, newPassword);
    }

    @Override
    public boolean existsById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }

        return userDAO.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username can not be empty");
        }

        return userDAO.existsByUsername(username);
    }

    @Override
    public int countUsers() {
        return userDAO.countUsers();
    }

    private boolean isOwner(int id) {
        User user = userDAO.findById(id);
        return user != null && "Owner".equalsIgnoreCase(user.getRole());
    }
}
