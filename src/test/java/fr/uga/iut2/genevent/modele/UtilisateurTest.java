package fr.uga.iut2.genevent.modele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilisateurTest {

    @Test
    void ajouteEvenementAdministre() {
        Copro copro = new Copro("Moi", "MonMDP", "MaSuperCopro", new Lieu("MaSuperCopro", "5 rue de la galaxie", "54415"));

    }
}