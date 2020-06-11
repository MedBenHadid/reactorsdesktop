package Main.Entities;

import Main.Services.UserService;
import Packages.Chihab.Models.Entities.Association;
import Packages.Chihab.Models.enums.RoleEnum;
import Packages.Chihab.Services.AssociationService;
import Packages.Chihab.Services.MembershipService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public final class UserSession {
    private static UserSession instance;
    private static final ArrayList<Association> affiliateAssociations = new ArrayList<>();
    private static final Association managedAss = new Association();
    private static User user;
    public boolean isLoggedIn(){
        return user!=null;
    }
    public ArrayList<Association> getAffiliateAssociations() {
        return affiliateAssociations;
    }


    public static boolean login(String credential, String password) throws SQLException {
        Optional<User> possibleUser = UserService.getInstace().validateLogin(credential, password);
        if (possibleUser.isPresent()) {
            user = possibleUser.get();
            if (user.isAssociationAdmin()) {
                Optional<Association> first = AssociationService.getInstance().getRecords().values().stream().filter(association -> association.getManager().getUsername().equals(user.getUsername())).findFirst();
                if (first.isPresent())
                    managedAss.set(first.get());
                else
                    user.removeRole(RoleEnum.ROLE_ADMIN_ASSOCIATION);
            }
            if (user.isMember())
                for (int i : MembershipService.getInstace().searchAffiliateIdByMemberId(user.getId()))
                    AssociationService.getInstance().getRecords().values().forEach(association -> {
                        if (association.getId() == i)
                            affiliateAssociations.add(association);
                    });
            return true;
        } else {
            System.out.println("Couldnt login");
            return false;
        }
    }

    public Optional<Association> getManagedAss() {
        if (managedAss.getId() == -1) return Optional.empty();
        return Optional.of(managedAss);
    }

    public void setManagedAss(Association managedAss) {
        UserSession.managedAss.set(managedAss);
    }

    public User getUser() {
        return user;
    }

    public void cleanUserSession() {
        instance = null;
    }

    public static UserSession getInstace() {
        if (instance == null)
            return instance = new UserSession();
        return instance;
    }

}