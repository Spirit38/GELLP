package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * La classe abstraite Utilisateur représente un utilisateur dans le système GenEvent.
 * Elle fournit les fonctionnalités de base pour gérer les informations d'un utilisateur,
 * y compris la gestion des événements qu'il administre.
 */

public abstract class Utilisateur implements Serializable {

    /** Numéro de version pour la sérialisation. */
    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation

    /** Identifiant de connexion de l'utilisateur. */
    private String login;

    /** Mot de passe de l'utilisateur. */
    private String motDePasse;

    /**
     * Map des événements administrés par l'utilisateur, qualifiés par le nom de l'événement.
     */
    private final Map<String, Evenement> evenementsAdministres;


    /**
     * Constructeur pour initialiser un nouvel utilisateur avec un login et un mot de passe.
     *
     * @param login L'identifiant de connexion de l'utilisateur.
     * @param motDePasse Le mot de passe de l'utilisateur.
     */
    public Utilisateur(String login, String motDePasse) {
        this.login = login;
        this.motDePasse = motDePasse;
        this.evenementsAdministres = new HashMap<>();
    }

    /**
     * Retourne l'identifiant de connexion de l'utilisateur.
     *
     * @return L'identifiant de connexion.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Définit l'identifiant de connexion de l'utilisateur.
     *
     * @param login Le nouvel identifiant de connexion.
     */
    public void setLogin(String login){
        this.login = login;
    }

    /**
     * Retourne le mot de passe de l'utilisateur.
     *
     * @return Le mot de passe.
     */
    public String getMotDePasse() {
        return motDePasse;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     *
     * @param motDePasse Le nouveau mot de passe.
     */
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    /**
     * Ajoute un événement à la liste des événements administrés par l'utilisateur.
     *
     * @param evt L'événement à ajouter.
     * @throws AssertionError Si un événement avec le même titre existe déjà.
     */
    public void ajouteEvenementAdministre(Evenement evt) {
        assert !this.evenementsAdministres.containsKey(evt.getTitre()) : "Un événement avec ce titre existe déjà.";
        this.evenementsAdministres.put(evt.getTitre(), evt);
    }
}
