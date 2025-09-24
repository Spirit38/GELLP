package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.exceptions.MauvaisMotDePasse;
import fr.uga.iut2.genevent.util.UtilitaireGenEvent;
import javafx.util.Pair;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La classe GenEvent représente le système principal de gestion des événements et des utilisateurs.
 * Elle gère les utilisateurs et les événements au sein de l'application.
 */
public class GenEvent implements Serializable {

    /** Numéro de version pour la sérialisation. */
    private static final long serialVersionUID = 1L;

    /** Map des utilisateurs, qualifiés par leur login et mot de passe. */
    private final Map<Pair<String, String>, Copro> utilisateurs;


    /** Login de l'utilisateur courant courant. */
    private Copro coproCourante;

    /** Evenement courant (celui en train d'être modifié) */
    private Evenement evenementCourant;

    /**
     * Constructeur pour initialiser une nouvelle instance de GenEvent.
     */
    public GenEvent() {
        this.utilisateurs = new HashMap<>();
    }

    /**
     * @return la copro courante du gestionnaire d'événements
     */
    private Copro getCoproCourante() {
        return coproCourante;
    }

    /**
     * @param coproCourante nouvelle copro courante. peut être mise à null lors de la déconnexion
     */
    private void setCoproCourante(Copro coproCourante) {
        this.coproCourante = coproCourante;
    }

    public Copro consulterCoproCourante(){
        return getCoproCourante();
    }

    /**
     * @return l'événement courant
     */
    private Evenement getEvenementCourant(){
        return evenementCourant;
    }

    private void setEvenementCourant(Evenement evenementCourant){
        this.evenementCourant = evenementCourant;
    }

    public Evenement consulterEvenementCourant(){
        return getEvenementCourant();
    }

    public void modifierEvenementCourant(Evenement evenementCourant){
        setEvenementCourant(evenementCourant);
    }

    /**
     * Ajoute un nouvel utilisateur au système.
     *
     * @param login L'identifiant de connexion de l'utilisateur.
     * @param motDePasse Le mot de passe de l'utilisateur.
     * @param nomCopro Le nom de la copropriété associée à l'utilisateur.
     * @param lieu Le lieu associé à la copropriété.
     * @return true si l'utilisateur a été ajouté avec succès, false sinon.
     */
    public boolean ajouteUtilisateur(String login, String motDePasse, String nomCopro, Lieu lieu) {
        if (existeUtilisateur(login)) return false;
        Pair<String, String> userKey = new Pair<>(login, motDePasse);
        this.utilisateurs.put(userKey, new Copro(login, motDePasse, nomCopro, lieu));
        return true;
    }

    /**
     * @param login login de l'utilisateur à rechercher
     * @return true si l'utilisateur existe, false sinon
     */
    public boolean existeUtilisateur(String login){
        for (Pair<String, String> utilisateur  : utilisateurs.keySet()){
            if (utilisateur.getKey().equals(login)) return true;
        }
        return false;
    }

    /**
     * @précondition: l'utilisateur existe dans utilisateurs (existeUtilisateur(login) == true)
     * @param login login de l'utilisateur
     * @param mdp mot de passe rentré par l'utilisateur
     * @throws MauvaisMotDePasse si le mot de passe est erroné
     */
    public Copro validationMotDePasse(String login, String mdp) throws MauvaisMotDePasse, NoSuchAlgorithmException {
        for (Pair<String, String> utilisateur : utilisateurs.keySet()){
            if (login.equals(utilisateur.getKey()) && utilisateur.getValue().equals(mdp)) return utilisateurs.get(utilisateur);
        }
        throw new MauvaisMotDePasse("Mot de passe erroné.");
    }

    /**
     * Récupère un utilisateur à partir de son login et mot de passe.
     *
     * @param login L'identifiant de connexion de l'utilisateur.
     * @param motDePasse Le mot de passe de l'utilisateur.
     * @return L'utilisateur correspondant, ou null s'il n'existe pas.
     */
    public Copro getUtilisateur(String login, String motDePasse) {
        return utilisateurs.get(new Pair<>(login, motDePasse));
    }

    /**
     * Récupère la map des événements.
     *
     * @return La map des événements.
     */
    public List<Evenement> getEvenements() {
        return coproCourante.consulterEvenement();
    }

    public void seConnecter(Copro utilisateur){
        setCoproCourante(utilisateur);
    }

    public void seDeconnecter(){
        setCoproCourante(null);
    }
}
