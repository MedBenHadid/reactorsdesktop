import Packages.Chihab.Services.MembershipService;

import java.sql.SQLException;

public class CRUDTester{
    public static void main(String[] args) {
        try {
            System.out.println(MembershipService.getInstace().searchByAssociationId(20));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
