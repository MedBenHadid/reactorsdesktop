package Packages.Chihab.Models.enums;

public enum AccessType {
    WRITE,
    READ,
    DELIVER;

    public static int getRole(AccessType accessType) {
        switch (accessType) {
            case WRITE:
                return 1;
            case READ:
                return 2;
            case DELIVER:
                return 3;
            default:
                return 2;
        }
    }
}
