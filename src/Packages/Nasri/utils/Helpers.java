package Packages.Nasri.utils;

import Packages.Nasri.enums.CivilStatus;
import Packages.Nasri.enums.HebergementStatus;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

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

    public static boolean phoneNumberIsValid(String phoneNumber) {
        //source: https://stackoverflow.com/questions/42104546/java-regular-expressions-to-validate-phone-numbers

        //\d{8} matches 12345678

        String regex = "\\d{8}";

        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(phoneNumber).matches();
    }
}
