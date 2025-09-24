package fr.uga.iut2.genevent.modele;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

class VoteTest {
    Vote vote;

    @BeforeEach
    void setUp() {
        vote = new Vote(20);
        vote.ajouterProposition("Lavande");
        vote.ajouterProposition("Coquelicot");
    }
    @Test
    void ajouterProposition() {
        vote.ajouterProposition("Rose");
        System.out.println(vote.consulterPropositions().size());
        assertTrue(vote.consulterPropositions().size() == 3, "la liste de proposition s'agrandit" +
                " si on ajoute une proposition");
    }

    @Test
    void supprimerProposition() {
        assertTrue(vote.consulterPropositions().size() == 2);
        assertEquals(0, vote.consulterPropositions().get("Lavande"), "nombre de vote de la proposition");
        vote.supprimerProposition("Lavande");
        assertTrue(vote.consulterPropositions().size() == 1, "mauvaise suppresion");
        assertEquals(null, vote.consulterPropositions().get("Lavande"), "nombre de vote de la proposition");

    }

    @Test
    void modifierNombreVote() {
        vote.modifierNombreVote("Lavande", 10);
        assertEquals(10, vote.consulterPropositions().get("Lavande"), "la valeur associé à la clas se modifie quand on le demande");
    }

    @Test
    void gagnant() {
        vote.modifierNombreVote("Lavande", 10);
        vote.ajouterProposition("Rose");
        vote.modifierNombreVote("Rose", 15);
        System.out.println(vote.gagnant());
    }
}