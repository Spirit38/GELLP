package fr.uga.iut2.genevent.modele;

import javafx.util.Pair;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La classe Evenement représente un événement dans le système GenEvent.
 * Elle contient les informations de base d'un événement, telles que son titre, sa date, son budget, son lieu, et les participants.
 */
public class Evenement implements Serializable, Comparable<Evenement> {

    /** Numéro de version pour la sérialisation. */
    private static final long serialVersionUID = 1L;

    /** Date de création de l'événement. */
    private final LocalDateTime dateCreation = LocalDateTime.now();

    /** Titre de l'événement. */
    private String titre;

    /** Date de l'événement. */
    private LocalDateTime dateEvenement;

    /** Indique si l'événement est terminé. */
    private boolean termine;

    /** Budget de l'événement. */
    private String budget;

    /** Lieu de l'événement. */
    private Lieu lieu;

    /** Liste des participants à l'événement. */
    private List<Membre> participants;

    /** Map des administrateurs de l'événement, qualifiés par le login et le mot de passe. */
    private final Map<Pair<String, String>, Copro> administrateurs;

    /**
     * Initialise un nouvel événement avec les informations fournies.
     *
     * @param titre Le titre de l'événement.
     * @param dateEvenement La date de l'événement.
     * @param termine Indique si l'événement est terminé.
     * @param budget Le budget de l'événement.
     * @param lieu Le lieu de l'événement.
     * @param admin L'administrateur de l'événement.
     * @return Une nouvelle instance de Evenement.
     */
    public static Evenement initialiseEvenement(String titre, LocalDateTime dateEvenement, boolean termine, String budget, Lieu lieu, Copro admin) {
        Evenement evt = new Evenement(titre, dateEvenement, budget, lieu);
        if (LocalDateTime.now().isBefore(evt.dateEvenement)) evt.setTermine(true);
        evt.ajouteAdministrateur(admin);
        return evt;
    }

    /**
     * Constructeur pour initialiser un nouvel événement avec les informations fournies.
     *
     * @param titre         Le titre de l'événement.
     * @param dateEvenement La date de l'événement.
     * @param budget        Le budget de l'événement.
     * @param lieu          Le lieu de l'événement.
     */
    public Evenement(String titre, LocalDateTime dateEvenement, String budget, Lieu lieu) {
        this.titre = titre;
        this.dateEvenement = dateEvenement;
        this.termine = termine;
        this.budget = budget;
        this.lieu = lieu;
        this.administrateurs = new HashMap<>();
        this.participants = new ArrayList<>();
    }

    /**
     * Ajoute un administrateur à l'événement.
     *
     * @param admin L'administrateur à ajouter.
     */
    public void ajouteAdministrateur(Copro admin) {
        Pair<String, String> adminPair = new Pair<>(admin.getLogin(), admin.getMotDePasse());
        assert !this.administrateurs.containsKey(adminPair);
        this.administrateurs.put(adminPair, admin);
        admin.ajouteEvenementAdministre(this);
    }

    /**
     * Retourne le titre de l'événement.
     *
     * @return Le titre de l'événement.
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Définit le titre de l'événement.
     *
     * @param titre Le nouveau titre de l'événement.
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Retourne la date de l'événement.
     *
     * @return La date de l'événement.
     */
    public LocalDateTime getDateEvenement() {
        return dateEvenement;
    }

    /**
     * Définit la date de l'événement.
     *
     * @param dateEvenement La nouvelle date de l'événement.
     */
    public void setDateEvenement(LocalDateTime dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    /**
     * Indique si l'événement est terminé.
     *
     * @return true si l'événement est terminé, false sinon.
     */
    public boolean isTermine() {
        return termine;
    }

    /**
     * Définit si l'événement est terminé.
     *
     * @param termine Le nouvel état de terminaison de l'événement.
     */
    public void setTermine(boolean termine) {
        this.termine = termine;
    }

    /**
     * Retourne le budget de l'événement.
     *
     * @return Le budget de l'événement.
     */
    public String getBudget() {
        return budget;
    }

    /**
     * Définit le budget de l'événement.
     *
     * @param budget Le nouveau budget de l'événement.
     */
    public void setBudget(String budget) {
        this.budget = budget;
    }

    /**
     * Retourne le lieu de l'événement.
     *
     * @return Le lieu de l'événement.
     */
    public Lieu getLieu() {
        return lieu;
    }

    /**
     * Définit le lieu de l'événement.
     *
     * @param lieu Le nouveau lieu de l'événement.
     */
    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    /**
     * Retourne la date de création de l'événement.
     *
     * @return La date de création de l'événement.
     */
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    /**
     * Ajoute un participant à l'événement.
     *
     * @param participant Le participant à ajouter.
     */
    public void ajouterParticipant(Membre participant) {
        participants.add(participant);
    }

    /**
     * Retire un participant de l'événement.
     *
     * @param participant Le participant à retirer.
     */
    public void retirerParticipant(Membre participant) {
        participants.remove(participant);
    }

    /**
     * Change le lieu de l'événement.
     *
     * @param nouveauLieu Le nouveau lieu de l'événement.
     */
    public void changerLieu(Lieu nouveauLieu) {
        this.lieu = nouveauLieu;
    }

    public int compareTo(Evenement e){
        return dateEvenement.compareTo(e.dateEvenement);
    }
}
