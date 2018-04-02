package pl.skript.ipsauth.exceptions;

public class NotAssignedAccountException extends Exception {

    private int assignedAccountId;

    public NotAssignedAccountException(int assignedAccountId) {
        this.assignedAccountId = assignedAccountId;
    }

    public NotAssignedAccountException(String message, int assignedAccountId) {
        super(message);
        this.assignedAccountId = assignedAccountId;
    }

}
