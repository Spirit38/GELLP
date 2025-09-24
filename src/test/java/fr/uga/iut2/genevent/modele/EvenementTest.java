package fr.uga.iut2.genevent.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EvenementTest {
    Evenement event;

    @BeforeEach
    void setup(){
        event = new Evenement("Event de test", LocalDateTime.of(2025, 10, 25, 15, 15), "0.0", new Lieu("MaCopro", "5 rue de la galaxie", "54415"));
    }

    @Test
    void initialiseEvenement() {
        Evenement ev1 = Evenement.initialiseEvenement("Event de test", LocalDateTime.of(2025, 10, 25, 15, 15), false, "0.0", new Lieu("MaCopro", "5 rue de la galaxie", "54415"), new Copro("Macopro", "erùghuaI533O", "Notre copro", new Lieu("ma copro", "5 rue de la galaxie", "54415")));
        assertEquals(event.getTitre(), ev1.getTitre());
        assertEquals(event.getDateEvenement(), ev1.getDateEvenement());
        assertEquals(event.getLieu(), ev1.getLieu());
    }

}