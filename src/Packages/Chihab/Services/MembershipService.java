package Packages.Chihab.Services;

import Packages.Chihab.Models.Membership;
import SharedResources.Utils.Connector.ConnectionUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MembershipService {
    //TODO : Search by associatio ID, Search by user ID
    private final String a = "adherance";
    public void create(Membership m , int userId , int associationId){ }
    public List<Membership> read(){
        List<Membership> ms = new ArrayList<>();
        return ms;
    }
    public void update(Membership m){ }
    public void delete(Membership m){ }
    public Membership searchByAssociation(){
        Membership ms = new Membership();
        return ms;
    }
    public Membership searchByUser(){
        Membership m = new Membership();
        return m;
    }

    public Membership rsToEntity(ResultSet rs){
        Membership m = new Membership();

        return m;
    }

    private static MembershipService instance;
    private Connection connection;

    private MembershipService() {
        connection = ConnectionUtil.conDB().conn;
    }
    public static MembershipService getInstace() {
        if(instance == null) { instance = new MembershipService(); }
        return instance;
    }
}
