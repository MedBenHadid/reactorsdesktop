package Packages.Chihab.Models.enums;

public enum RoleEnum {
    ROLE_SUPER_ADMIN,
    ROLE_CLIENT,
    ROLE_MEMBER,
    ROLE_ADMIN_ASSOCIATION;

    public static RoleEnum getRole(String role) {
        switch (role) {
            case "ROLE_CUSTOMER":
                return ROLE_CLIENT;
            case "ROLE_MEMBER":
                return ROLE_MEMBER;
            case "ROLE_SUPER_ADMIN":
                return ROLE_SUPER_ADMIN;
            case "ROLE_ASSOCIATION":
                return ROLE_ADMIN_ASSOCIATION;
            default:
                return ROLE_CLIENT;
        }
    }
}
