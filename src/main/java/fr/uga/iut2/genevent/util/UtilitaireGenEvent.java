package fr.uga.iut2.genevent.util;
import fr.uga.iut2.genevent.modele.Evenement;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;

public class UtilitaireGenEvent {

    // page Authentification
    public static String hachageMdp(String mdp) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();

        MessageDigest md = MessageDigest.getInstance("SHA-512");

        byte[] hashedPassword = md.digest(mdp.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    /**
     *
     * @param textField
     * @return
     * cette fonction verifie que le textField ne peut contenire que des nombres >0
     */
    public static TextField nombreOnly(TextField textField) {
        UnaryOperator<TextFormatter.Change> filtre = change -> {
            String noutext = change.getControlNewText();
            if (noutext.matches("\\d*")) {
                return change;
            }
            return null;
        };
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null, filtre);
        textField.setTextFormatter(textFormatter);
        return textField;
    }

    /**
     * @param textField
     * transformation du text field pour avoir un bon format de numero de téléphone
     * À utiliser après nombreOnly
     * @return textFiel
     */
    public static TextField formatTel(TextField textField) {
        textField.textProperty().addListener((observable, ancienTF, nouvTF) -> {
            String queNum = nouvTF.replaceAll("[^0-9]", "");
            StringBuilder formatTel = new StringBuilder();
            int finBcle = Math.min(queNum.length(), 10);
            for (int i = 0; i < finBcle; i++) {
                formatTel.append(queNum.charAt(i));
                if ((i + 1) % 2 == 0 && i + 1 != finBcle) {
                    formatTel.append("-");
                }
            }
            textField.setText(formatTel.toString());
            textField.positionCaret(formatTel.length());
        });
        return textField;
    }


    /**
     * @param textField
     * transformation du text field pour avoir un bon format pour l'horaire
     * @return textFiel
     */
    public static TextField formatHeureMin(TextField textField) {
        textField.textProperty().addListener((observable, ancienTF, nouvTF) -> {
            String queNum = nouvTF.replaceAll("[^0-9]", "");
            StringBuilder formatHM = new StringBuilder();
            int finBcle = Math.min(queNum.length(), 4);
            for (int i = 0; i < finBcle; i++) {
                formatHM.append(queNum.charAt(i));
                if ((i + 1) % 2 == 0 && i + 1 != finBcle) {
                    formatHM.append(":");
                }
            }
//            String bonFormat = validerHM(formatHM.toString());
//            textField.setText(bonFormat);
            textField.setText(formatHM.toString());
            textField.positionCaret(formatHM.length());
        });
        return textField;
    }

//    private static String validerHM(String horaire) {
//        String ancienneHeure = horaire.substring(0, 2);
//
//        int heures = Integer.parseInt(ancienneHeure.substring(0,2));
//        if (heures > 23)
//            heures = 23;
//        if (horaire.length() > 3){
//            String ancienneMin = horaire.substring(3, 5);
//            int minutes = Integer.parseInt(ancienneMin);
//            if (minutes==60){
//                minutes=00;
//            }
//            else if (minutes > 59 )
//                minutes = 59;
//            horaire = beauDuoInt(heures) + ":" + beauDuoInt(minutes);
//        }
//        else{
//            horaire = beauDuoInt(heures);
//        }
//        return horaire;
//    }

//    private static String beauDuoInt(int val){
//        String valString = ((Integer)val).toString();
//        if (valString.length()==1)
//            valString = '0'+ valString;
//        return valString;
//    }



    private static void triEvenements(List<Evenement> evenements){
        evenements.sort(new Comparator<Evenement>() {
            @Override
            public int compare(Evenement evenement, Evenement t1) {
                return evenement.compareTo(t1);
            }
        });
    }

    public static int indicePremierEvenementFutur(List<Evenement> evenements){
        triEvenements(evenements);
        if (evenements.isEmpty() || evenements.get(evenements.size()-1).isTermine()) return -1;
        int i = 0;
        while (i < evenements.size() && evenements.get(i).isTermine()){
            i++;
        }
        return i;
    }

}
