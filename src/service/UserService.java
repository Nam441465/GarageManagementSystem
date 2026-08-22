package service;

import java.util.List;

import dao.UserDAO;
import enums.UserRole;
import model.User;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this(new UserDAO());
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = java.util.Objects.requireNonNull(userDAO, "userDAO is required");
    }

    public void addUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }

        if (user.getUsername() == null
                || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");
        }

        if (user.getPassword() == null
                || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống.");
        }

        if (user.getRole() == null) {
            throw new IllegalArgumentException("Quyền hạn không được để trống.");
        }

        if (user.getRole() == UserRole.OWNER) {
            throw new IllegalArgumentException("Không thể tạo thủ công tài khoản Chủ gara cố định.");
        }

        if (userDAO.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập này đã được sử dụng.");
        }

        userDAO.addUser(user);
    }

    public void updateUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }

        if (user.getId() <= 0) {
            throw new IllegalArgumentException("Mã người dùng không hợp lệ.");
        }

        if (user.getUsername() == null
                || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");
        }

        if (user.getPassword() == null
                || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống.");
        }

        if (user.getRole() == null) {
            throw new IllegalArgumentException("Quyền hạn không được để trống.");
        }

        if (!userDAO.existsById(user.getId())) {
            throw new IllegalArgumentException("Không tìm thấy người dùng.");
        }

        if (isOwner(user.getId())) {
            throw new IllegalArgumentException("Không thể sửa tài khoản Chủ gara cố định.");
        }

        userDAO.updateUser(user);
    }

    public void deleteUser(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Mã người dùng không hợp lệ.");
        }

        if (!userDAO.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy người dùng.");
        }

        if (isOwner(id)) {
            throw new IllegalArgumentException("Không thể xóa tài khoản Chủ gara cố định.");
        }

        userDAO.deleteUser(id);
    }

    public User findById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Mã người dùng không hợp lệ.");
        }

        return userDAO.findById(id);
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public User findByUsername(String username) {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");
        }

        return userDAO.findByUsername(username);
    }

    public User login(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống.");
        }

        return userDAO.login(username, password);
    }

    public boolean changePassword(int userId, String newPassword) {

        if (userId <= 0) {
            throw new IllegalArgumentException("Mã người dùng không hợp lệ.");
        }

        if (newPassword == null
                || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu mới không được để trống.");
        }

        if (!userDAO.existsById(userId)) {
            throw new IllegalArgumentException("Không tìm thấy người dùng.");
        }

        return userDAO.changePassword(userId, newPassword);
    }

    public boolean existsById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Mã người dùng không hợp lệ.");
        }

        return userDAO.existsById(id);
    }

    public boolean existsByUsername(String username) {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");
        }

        return userDAO.existsByUsername(username);
    }

    public int countUsers() {
        return userDAO.countUsers();
    }

    private boolean isOwner(int id) {
        User user = userDAO.findById(id);
        return user != null && user.getRole() == UserRole.OWNER;
    }
}
