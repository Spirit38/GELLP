package fr.uga.iut2.genevent.util;

import org.junit.jupiter.api.Test;
import javafx.scene.control.TextField;


import static org.junit.jupiter.api.Assertions.*;

class UtilitaireGenEventTest {

    @Test
    void hachageMdp() {
        //test mdp diff qu'avant
    }

    @Test
    void nombreOnly() {
        TextField mesNumero = new TextField("1234");
//        assertThrows(ExceptionInInitializerError, UtilitaireGenEvent.nombreOnly(mesNumero));

        mesNumero.setText("abc");
        UtilitaireGenEvent.nombreOnly(mesNumero);
    }

//    @Test
//    void nombreOnly2() {
//        TextField mesNumero = new TextField("1234");
//        assertDoesNotThrow(UtilitaireGenEvent.nombreOnly(mesNumero));
//        mesNumero.setText("abc");
//        assertThrows(ExceptionInInitializerError, UtilitaireGenEvent.nombreOnly(mesNumero));
//        // Ajoutez des assertions pour vérifier le comportement
//    }
//
//
//    @Test
//    void formatTel() {
//        TextField monTel = new TextField("0123456789");
//        System.out.println(UtilitaireGenEvent.formatTel(monTel));
//    }
}