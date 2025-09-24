package fr.uga.iut2.genevent.modele;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GenEventTest {
    GenEvent GE;

    @BeforeEach
    void setUp() {
        GE = new GenEvent();
    }

    @Test
    void ajouteUtilisateur() {
        assertTrue(GE.ajouteUtilisateur("Moi", "oZGER¨G", "Ma copro", new Lieu("Ma copro", "5 rue de la Galaxie", "65498")));
        assertFalse(GE.ajouteUtilisateur("Moi", "oZGER¨G", "Ma copro", new Lieu("Ma copro", "5 rue de la Galaxie", "65498")));
    }

    @Test
    void existeUtilisateur() {
        GE.ajouteUtilisateur("Moi", "oZGER¨G", "Ma copro", new Lieu("Ma copro", "5 rue de la Galaxie", "65498"));
        assertTrue(GE.existeUtilisateur("Moi"));
        assertFalse(GE.existeUtilisateur("Toi"));
    }

    @Test
    void validationMotDePasse() {
//        GE.ajouteUtilisateur("Moi", "oZGER¨G", "Ma copro", new Lieu("Ma copro", "5 rue de la Galaxie", "65498"));
////        Map<Pair<String, String>, Copro> utilisateurs = new HashMap<>();
////        utilisateurs.put(new Pair<>("Moi", "oZGER¨G"), new Copro("Copro", "mqgerùo", "Ma copro", new Lieu("La copro", "5 rue de la Galaxie", "54415")));
//        assertEquals();

    }

    @Test
    void seConnecter() {
        Copro copro = new Copro("Moi", "MonMDP", "MaSuperCopro", new Lieu("MaSuperCopro", "5 rue de la galaxie", "54415"));
        GE.seConnecter(copro);
        assertEquals(copro, GE.consulterCoproCourante());
    }

    @Test
    void seDeconnecter() {
        Copro copro = new Copro("Moi", "MonMDP", "MaSuperCopro", new Lieu("MaSuperCopro", "5 rue de la galaxie", "54415"));
        GE.seConnecter(copro);
        GE.seDeconnecter();
        assertEquals(null, GE.consulterCoproCourante());
    }
}