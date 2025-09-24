package fr.uga.iut2.genevent.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EvenementOfficielTest {
    EvenementOfficiel EvOff;

    @BeforeEach
    void setup(){
        EvenementOfficiel EvOff = new EvenementOfficiel("Réunion", LocalDateTime.of(2025, 12, 25, 16, 25), false, "0.0", new Lieu("ma copro", "5 rue de la galaxie", "54415"));
    }

    @Test
    void creerVote() {
        EvOff.creerVote(new Vote());
        assertEquals(1, EvOff.consulterVotes().size());
    }

    @Test
    void supprimerVote() {
        Vote v1 = new Vote();
        EvOff.creerVote(v1);
        EvOff.creerVote(new Vote(52));
        EvOff.supprimerVote(v1);
        assertEquals(1, EvOff.consulterVotes().size());
    }

}