package fr.uga.iut2.genevent.util;

import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;

class UtilitaireEvenementTest {

    @Test
    void majusculeDebutMot() {
        String motTest = "bATimEnt";
        assertEquals("Batiment", UtilitaireEvenement.majusculeDebutMot(motTest));
    }

    @Test
    void deLocalDateVersString() {
        LocalDate date = LocalDate.of(2025,06,21);
        assertEquals("21/6/2025", UtilitaireEvenement.deLocalDateVersString(date));
    }

    @Test
    void deStringversLocalDate() {
        String date = "10/12/16/08/2016";
        //assertEquals(new LocalDateTimeStringConverter(DateTimeFormatter.ofPattern("mm/HH/dd/MM/yyyy"),"10/12/16/08/2016"))//2016-08-16T12:10), UtilitaireEvenement.deStringversLocalDate(date));
    }

}