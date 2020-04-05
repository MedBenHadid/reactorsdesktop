package Packages.Ramy;

import Packages.Ramy.Models.Requete;
import Packages.Ramy.Services.RequeteService;

import java.sql.Date;

public class SupportMain {
    public static void main(String[] args){
        RequeteService ros = new RequeteService();
        Requete r1 = new Requete(72,1,"problem","descri",new Date(2020,1,1),1,2);
        ros.add(r1);
        System.out.println("hello");
    }
}
