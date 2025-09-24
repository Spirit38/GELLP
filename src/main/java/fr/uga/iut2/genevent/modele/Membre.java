package fr.uga.iut2.genevent.modele;

import java.io.Serializable;

/**
 * La classe Membre représente un membre dans le système GenEvent.
 * Elle contient les informations de base d'un membre, telles que son nom, prénom, email et numéro de téléphone.
 */
public class Membre implements Serializable {

    /** Numéro de version pour la sérialisation. */
    private static final long serialVersionUID = 1L;

    /** Nom du membre. */
    private String nom;

    /** Prénom du membre. */
    private String prenom;

    /** Adresse email du membre. */
    private String email;

    /** Numéro de téléphone du membre. */
    private String numTel;

    /**
     * Constructeur pour initialiser un nouveau membre avec un nom, un prénom, un email et un numéro de téléphone.
     *
     * @param nom Le nom du membre.
     * @param prenom Le prénom du membre.
     * @param email L'adresse email du membre.
     * @param numTel Le numéro de téléphone du membre.
     */
    public Membre(String nom, String prenom, String email, String numTel) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numTel = numTel;
    }

    /**
     * Retourne le nom du membre.
     *
     * @return Le nom du membre.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom du membre.
     *
     * @param nom Le nouveau nom du membre.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le prénom du membre.
     *
     * @return Le prénom du membre.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom du membre.
     *
     * @param prenom Le nouveau prénom du membre.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Retourne l'adresse email du membre.
     *
     * @return L'adresse email du membre.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse email du membre.
     *
     * @param email La nouvelle adresse email du membre.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne le numéro de téléphone du membre.
     *
     * @return Le numéro de téléphone du membre.
     */
    public String getNumTel() {
        return numTel;
    }

    /**
     * Définit le numéro de téléphone du membre.
     *
     * @param numTel Le nouveau numéro de téléphone du membre.
     */
    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }
}
