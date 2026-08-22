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
}