package Packages.Nasri.tests;

import Packages.Nasri.enums.CivilStatus;
import Packages.Nasri.enums.HebergementStatus;
import Packages.Nasri.models.HebergementComment;
import Packages.Nasri.models.HebergementOffer;
import Packages.Nasri.models.HebergementRequest;
import Packages.Nasri.services.ServiceHebergementComment;
import Packages.Nasri.services.ServiceHebergementOffer;
import Packages.Nasri.services.ServiceHebergementRequest;

import java.time.LocalDateTime;

public class RefugeeMain {
    public static void main(String[] args) {
        HebergementComment hebergementComment =
                new HebergementComment(2, 73, 10, "blahx", LocalDateTime.now());

        ServiceHebergementComment sHebCom = new ServiceHebergementComment();
        sHebCom.add(hebergementComment);
        sHebCom.add(hebergementComment);

        for (HebergementComment heberComment : sHebCom.get()) {
            System.out.println(heberComment);
        }
    }
}
