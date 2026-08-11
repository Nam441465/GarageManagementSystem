package model;

import java.time.LocalDateTime;

public class EmployeeInvite {

    private int id;
    private String inviteCode;
    private String status;
    private LocalDateTime createdDate;

    public EmployeeInvite() {
    }

    public EmployeeInvite(int id, String inviteCode, String status) {
        this.id = id;
        this.inviteCode = inviteCode;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "EmployeeInvite{" +
                "id=" + id +
                ", inviteCode='" + inviteCode + '\'' +
                ", status='" + status + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}