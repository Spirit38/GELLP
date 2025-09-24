package fr.uga.iut2.genevent.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilitaireEvenement {
    /**
     * @param chaine
     * cette fonction prend une chaine de carctères et retourne cette même chaine avec le premier charactère en majuscule et le reste en minuscule
     * @return String sous le format la dit plus haut
     */
    public static String majusculeDebutMot(String chaine) {
        if (chaine.length()<2)
            return chaine.toUpperCase();
        return chaine.toUpperCase().charAt(0) + chaine.toLowerCase().substring(1);
    }

    /**
     * @param date
     * change le format LocalDate vers un string pour aider la lisibilité de l'utilisateur
     * @return
     */
    public static String deLocalDateVersString(LocalDate date){
        //selon comment Iliesse fait pour l'heure je pourrais créer une deuxieme fonction
        System.out.println(date);
        return ""+date.getDayOfMonth()+"/"+date.getMonth().getValue()+"/"+date.getYear();
    }

    public static LocalDateTime deStringversLocalDate(String date){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
    System.out.println(localDateTime);
    System.out.println(formatter.format(localDateTime));
    return localDateTime;
    }

    public static String limiterNombre(String contenu, int nbCara){
        if (contenu.length()>nbCara){
            contenu.substring(0, nbCara);
        }
        return contenu;
    }
}
