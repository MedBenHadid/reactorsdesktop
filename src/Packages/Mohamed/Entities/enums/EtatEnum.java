package Packages.Mohamed.Entities.enums;

public enum EtatEnum {
    inviter,
    accepter,
    réfuser;

    public static String getEtat(String etat) {
        switch (etat) {
            case "accepter":
                return "accepter";
            case "réfuser":
                return "réfuser";
            case "inviter":
                return "inviter";
            default:
                return "inviter";
        }
    }
}
