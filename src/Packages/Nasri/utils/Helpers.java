package Packages.Nasri.utils;

import Packages.Nasri.enums.CivilStatus;
import Packages.Nasri.enums.HebergementStatus;

import java.sql.Date;
import java.time.LocalDateTime;

public class Helpers {
    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        return java.sql.Date.valueOf(localDateTime.toLocalDate());
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return date.toLocalDate().atStartOfDay();
    }

    public static String convertHebergementStateToFrench(String hebergementState) {
        return hebergementState.equals(HebergementStatus.inProcess.name()) ? "En cours" : "Terminé";
    }

    public static String convertCivilStateToFrench(String civilStage) {
        return civilStage.equals(CivilStatus.Married.name()) ? "Marié(e)" : "Célibataire";
    }
}
