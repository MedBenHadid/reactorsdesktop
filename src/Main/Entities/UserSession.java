package Main.Entities;

import Main.Services.UserService;
import Packages.Chihab.Models.Entities.Association;
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


    public Association getManagedAss() {
        return managedAss;
    }

    public void setManagedAss(Association managedAss) {
        UserSession.managedAss.set(managedAss);
    }


    public static boolean login(String credential, String password) throws SQLException {
        Optional<User> possibleUser = UserService.getInstace().validateLogin(credential, password);
        if(possibleUser.isPresent()){
            user=possibleUser.get();
            if(user.isAssociationAdmin()){
                managedAss.set(AssociationService.getInstance().getRecords().values().stream().filter(association -> association.getManager().getUsername().equals(user.getUsername())).findFirst().get());
                System.out.println("is ass admin of :"+managedAss.get().getNom());
            }
            if(user.isMember()){
                for(int i:MembershipService.getInstace().searchAffiliateIdByMemberId(user.getId())){
                    AssociationService.getInstance().getRecords().values().forEach(association -> {
                        if(association.getId()==i)
                            affiliateAssociations.add(association);
                        System.out.println("is member of :" + association.getNom());
                    });
                    //affiliateAssociations.get().add(ObservableAssociationListValue.getInstance().stream().filter(association -> association.get().getId()==i).findFirst().get().get());
                }
            }
            return true;
        }
        else{
            System.out.println("Couldnt login");
            return false;
        }
    }

    public User getUser() {
        return user;
    }

    public void cleanUserSession() {
        instance=null;
    }
    public static UserSession getInstace() {
        if (instance == null)
            return instance = new UserSession();
        return instance;
    }

}