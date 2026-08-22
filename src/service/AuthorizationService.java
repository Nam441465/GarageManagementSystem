package service;

import enums.UserRole;
import exception.PermissionDeniedException;
import model.Session;

public class AuthorizationService {

    public boolean isOwner() {
        return Session.getCurrentUser() != null
                && Session.getCurrentUser().getRole() == UserRole.OWNER;
    }

    public void requireOwner() {
        if (!isOwner()) {
            throw new PermissionDeniedException(
                    "Yêu cầu quyền hạn Chủ gara.");
        }
    }

    public boolean isEmployee() {
        return Session.getCurrentUser() != null
                && Session.getCurrentUser().getRole() == UserRole.EMPLOYEE;
    }

    public void requireEmployee() {
        if (!isEmployee()) {
            throw new PermissionDeniedException(
                    "Yêu cầu quyền hạn Nhân viên.");
        }
    }

    public boolean isOwnerOrEmployee() {
        return Session.getCurrentUser() != null &&
                (Session.getCurrentUser().getRole() == UserRole.OWNER ||
                 Session.getCurrentUser().getRole() == UserRole.EMPLOYEE);
    }

    public void requireOwnerOrEmployee() {
        if (!isOwnerOrEmployee()) {
            throw new PermissionDeniedException(
                    "Yêu cầu quyền hạn Chủ gara hoặc Nhân viên.");
        }
    }
}