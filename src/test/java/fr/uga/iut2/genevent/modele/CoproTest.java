package fr.uga.iut2.genevent.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CoproTest {
    Copro copro;

    @BeforeEach
    void setup(){
        copro = new Copro("Moi", "MonMDP", "MaSuperCopro", new Lieu("MaSuperCopro", "5 rue de la galaxie", "54415"));
    }

    @Test
    void creerMembre() {
        assertTrue(copro.creerMembre(new Membre("John", "Doe", "JohnDoe@mail.com", "08.08.53.32.84")));
        assertFalse(copro.creerMembre(new Membre("John", "Doe", "JohnDoe@mail.com", "08.08.53.32.84")));
    }

    @Test
    void supprimerMembre() {
        Membre John = new Membre("John", "Doe", "JohnDoe@mail.com", "08.08.53.32.84");
        copro.creerMembre(John);
        Membre Anne = new Membre("Anne", "Onyme", "AnneOnyme@mail.com", "08.52.54.58.63");
        copro.creerMembre(Anne);
        copro.supprimerMembre(John);
        assertEquals( 1, copro.consulterMembres().size());
        copro.supprimerMembre(Anne);
        assertEquals(0, copro.consulterMembres().size());
    }

    @Test
    void consulterMembres() {
        Membre John = new Membre("John", "Doe", "JohnDoe@mail.com", "08.08.53.32.84");
        copro.creerMembre(John);
        Membre Anne = new Membre("Anne", "Onyme", "AnneOnyme@mail.com", "08.52.54.58.63");
        copro.creerMembre(Anne);
        assertEquals(new ArrayList<>(Arrays.asList(John, Anne)), copro.consulterMembres());
    }

//    @Test
//    void modifierMembre() {
//        Membre Anne = new Membre("Anne", "Onyme", "AnneOnyme@mail.com", "08.52.54.58.54");
//        copro.creerMembre(new Membre("Anne", "Onyme", "AnneOnyme@mail.com", "08.52.54.58.63"));
//        copro.modifierMembre(copro.consulterMembres().get(0), "", "", "", "08.52.54.58.54");
//        assertEquals(Anne.getNumTel(), copro.consulterMembres().get(0).getNumTel());
//        assertEquals(Anne.getPrenom(), copro.consulterMembres().get(0).getPrenom());
//        assertEquals(Anne.getNom(), copro.consulterMembres().get(0).getNom());
//        assertEquals(Anne.getEmail(), copro.consulterMembres().get(0).getEmail());
//
//    }

    @Test
    void creerEvenement() {
        EvenementOfficiel EvOff = new EvenementOfficiel("réunion novembre", LocalDateTime.of(2025, 11, 20, 10, 30), false, "0.0", new Lieu("salle réunion", "5 rue de la galaxie", "54415"));
        copro.creerEvenement(EvOff);
        assertEquals(1, copro.consulterEvenement().size());
    }

    @Test
    void supprimerEvenement() {
        EvenementOfficiel EvOff = new EvenementOfficiel("réunion novembre", LocalDateTime.of(2025, 11, 20, 10, 30), false, "0.0", new Lieu("salle réunion", "5 rue de la galaxie", "54415"));
        copro.creerEvenement(EvOff);
        assertEquals(1, copro.consulterEvenement().size());
        EvenementSocial EvSoc = new EvenementSocial(true, "", "Pétanque", "Pétanque du samedi soir", LocalDateTime.of(2025, 8, 23, 19, 00), "0.0", new Lieu("Cour commune", "5 rue de la galaxie", "54415"), false);
        copro.creerEvenement(EvSoc);
        copro.supprimerEvenement(EvSoc);
        assertEquals(1, copro.consulterEvenement().size());
    }

    @Test
    void consulterEvenement() {
        EvenementOfficiel EvOff = new EvenementOfficiel("réunion novembre", LocalDateTime.of(2025, 11, 20, 10, 30), false, "0.0", new Lieu("salle réunion", "5 rue de la galaxie", "54415"));
        copro.creerEvenement(EvOff);
        assertEquals(1, copro.consulterEvenement().size());
        EvenementSocial EvSoc = new EvenementSocial(true, "", "Pétanque", "Pétanque du samedi soir", LocalDateTime.of(2025, 8, 23, 19, 00), "0.0", new Lieu("Cour commune", "5 rue de la galaxie", "54415"), false);
        copro.creerEvenement(EvSoc);
        assertEquals(new ArrayList<>(Arrays.asList(EvOff, EvSoc)), copro.consulterEvenement());
    }

    @Test
    void modifierEvenement() {
        EvenementOfficiel EvOff = new EvenementOfficiel("réunion novembre", LocalDateTime.of(2025, 11, 20, 10, 30), false, "0.0", new Lieu("salle réunion", "5 rue de la galaxie", "54415"));
        copro.creerEvenement(EvOff);
        assertEquals(1, copro.consulterEvenement().size());
    }

}