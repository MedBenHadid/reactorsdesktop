package Packages.Chihab.Services;

public class AssociationSQLException extends Exception {
    public AssociationSQLException() {
    }

    public AssociationSQLException(String message) {
        super(message);
    }

    public AssociationSQLException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssociationSQLException(Throwable cause) {
        super(cause);
    }

    public AssociationSQLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
