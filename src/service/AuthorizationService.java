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
                    "Owner permission required.");
        }
    }

    public boolean isEmployee() {
        return Session.getCurrentUser() != null
                && Session.getCurrentUser().getRole() == UserRole.EMPLOYEE;
    }

    public void requireEmployee() {
        if (!isEmployee()) {
            throw new PermissionDeniedException(
                    "Employee permission required.");
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
                    "Owner or Employee permission required.");
        }
    }
}