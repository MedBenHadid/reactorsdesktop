package associationPackage.Entities.Enums;

import sharedAppPackage.models.enums.RoleEnum;

public enum AccessType {
    WRITE,
    READ,
    DELIVER;

    public static AccessType getRole(int accessType) {
        switch (accessType) {
            case 1:return WRITE;
            case 2: return READ;
            case 3:return DELIVER;
            default:return READ;
        }
    }
}
