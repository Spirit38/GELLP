package fr.uga.iut2.genevent.modele;

import java.io.Serializable;

import static java.lang.Integer.parseInt;

/**
 * La classe Lieu représente un lieu dans le système GenEvent.
 * Elle contient les informations de base d'un lieu, telles que son nom et son adresse.
 */
public class Lieu implements Serializable {

    /** Numéro de version pour la sérialisation. */
    private static final long serialVersionUID = 1L;

    /** Nom du lieu. */
    private String nom;

    /** Adresse du lieu. */
    private String adresse;

    private String codePostal;

    /**
     * Constructeur pour initialiser un nouveau lieu avec un nom et une adresse.
     *
     * @param nom        Le nom du lieu.
     * @param adresse    L'adresse du lieu.
     * @param codePostal Le code Postal du lieu.
     */
    public Lieu(String nom, String adresse, String codePostal) {
        this.nom = nom;
        this.adresse = adresse;
        this.codePostal = codePostal;
    }



    /**
     * Retourne le nom du lieu.
     *
     * @return Le nom du lieu.
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Définit le nom du lieu.
     *
     * @param nom Le nouveau nom du lieu.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

}
