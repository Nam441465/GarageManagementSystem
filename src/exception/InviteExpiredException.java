package exception;

public class InviteExpiredException extends GarageException {
    public InviteExpiredException(String inviteCode) { super("Invite has expired: " + inviteCode); }
}
