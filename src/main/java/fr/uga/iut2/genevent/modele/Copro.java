package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe Copro représente une copropriété dans le système GenEvent.
 * Elle étend la classe Utilisateur et gère les membres et les événements associés à la copropriété.
 */
public class Copro extends Utilisateur implements Serializable {

    /**
     * Numéro de version pour la sérialisation.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Nom de la copropriété.
     */
    private String nom;

    /**
     * Liste des membres de la copropriété.
     */
    private List<Membre> membres;

    /**
     * Liste des événements associés à la copropriété.
     */
    private List<Evenement> evenements;

    /**
     * Lieu associé à la copropriété.
     */
    private Lieu lieu;

    /**
     * Constructeur pour initialiser une nouvelle copropriété avec un login, un mot de passe, un nom et un lieu.
     *
     * @param login      L'identifiant de connexion de l'utilisateur.
     * @param motDePasse Le mot de passe de l'utilisateur.
     * @param nomCopro   Le nom de la copropriété.
     * @param lieu       Le lieu associé à la copropriété.
     */
    public Copro(String login, String motDePasse, String nomCopro, Lieu lieu) {
        super(login, motDePasse);
        this.nom = nomCopro;
        this.membres = new ArrayList<>();
        this.evenements = new ArrayList<>();
        this.lieu = lieu;
    }

    /**
     * Ajoute un membre à la liste des membres de la copropriété.
     *
     * @param membre Le membre à ajouter.
     * @return true si le membre a été ajouté avec succès, false sinon.
     */
    public boolean creerMembre(Membre membre) {
        for (int i = 0; i < membres.size(); i++) {
            if (membres.get(i).getPrenom().equals(membre.getPrenom()) &&
                    membres.get(i).getNom().equals(membre.getNom()) &&
                    membres.get(i).getNumTel().equals(membre.getNumTel()) &&
                    membres.get(i).getEmail().equals(membre.getEmail())) {
                return false; // Le membre existe déjà
            }
        }

        // Ajouter le membre à la liste des membres
        membres.add(membre);
        return true; // Le membre a été ajouté avec succès
    }


    /**
     * Supprime un membre de la liste des membres de la copropriété.
     *
     * @param membre Le membre à supprimer.
     */
    public boolean supprimerMembre(Membre membre) {
        return membres.remove(membre);
    }


    /**
     * Retourne une copie de la liste des membres de la copropriété.
     *
     * @return Une liste des membres.
     */
    public List<Membre> consulterMembres() {
        return new ArrayList<>(membres);
    }

    /**
     * Modifie les informations d'un membre de la copropriété.
     *
     * @param membre        Le membre à supprimer.
     * @param nouveauMembre Le nouveau membre
     */
    public boolean modifierMembre(Membre membre, Membre nouveauMembre) {
        if (membres.contains(membre)) {
            membres.remove(membre);
            membres.add(nouveauMembre);
            return true;
        }
        return false;
    }

    /**
     *
     *
     * @param evenement L'événement à ajouter.
     */
    /**
     * Ajoute un événement à la liste des événements de la copropriété.
     *
     * @param evenement L'événement à ajouter.
     * @return true si l'événement a été ajouté avec succès, false sinon.
     */
    public boolean creerEvenement(Evenement evenement) {
        if (evenements.contains(evenement)) {
            return false; // Le membre existe déjà
        }
        evenements.add(evenement);
        return true;
    }

    /**
     * Supprime un événement de la liste des événements de la copropriété.
     *
     * @param evenement L'événement à supprimer.
     */
    public boolean supprimerEvenement(Evenement evenement) {
        return evenements.remove(evenement);
    }

    /**
     * Retourne une copie de la liste des événements de la copropriété.
     *
     * @return Une liste des événements.
     */
    public List<Evenement> consulterEvenement() {
        return new ArrayList<>(evenements);
    }

    /**
     * Modifie les informations d'un événement de la copropriété.
     *
     * @param evenement L'événement à supprimer.
     * @param nouvelEvenement Le nouveau événement.
     */
    public boolean modifierEvenementOfficiel(Evenement evenement, Evenement nouvelEvenement) {
        if (evenements.contains(evenement)) {
            evenements.remove(evenement);
            evenements.add(nouvelEvenement);
            return true;
        }
        return false;
    }

    public Lieu getLieu() {
        return lieu;
    }

    /**
     * Retourne le nom de la copropriété.
     *
     * @return Le nom de la copropriété.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de la copropriété.
     *
     * @param nom Le nouveau nom de la copropriété.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

 }
