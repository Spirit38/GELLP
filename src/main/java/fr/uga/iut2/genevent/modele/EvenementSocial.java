package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.time.LocalDateTime;

public class EvenementSocial extends Evenement implements Serializable {

    /** Numéro de version pour la sérialisation. */
    private static final long serialVersionUID = 1L;

    /** Précise si l'activité convient aux mineurs */
    private boolean convientAuxMineur;

    /** Nom du prestataire */
    private String prestataire;

    /** Catégorie de l'évènement */
    private String categorie;

    /**
     * Constructeur pour créer un nouvel évènement social
     *
     * @param mineurs true si l'actvité est adaptée aux mineurs, false sinon.
     * @param prestataire le nom du prestataire.
     * @param categorie la catégorie de l'évènement
     * @param nom Le titre de l'événement.
     * @param dateDebut La date de l'événement.
     * @param termine Indique si l'événement est terminé.
     * @param budget Le budget de l'événement.
     * @param lieu Le lieu de l'événement.
     * */
    public EvenementSocial(boolean mineurs, String prestataire, String categorie, String nom, LocalDateTime dateDebut, String budget, Lieu lieu, boolean termine){
        super(nom, dateDebut, budget, lieu);
        setCategorie(categorie);
        setPrestataire(prestataire);
        setConvientAuxMineur(mineurs);
    }

    /**
     * Retourne le nom du prestataire.
     *
     * @return Le nom du prestataire.
     */
    public String getPrestataire() {
        return prestataire;
    }

    /**
     * Définit le nouveau nom du prestataire.
     *
     * @param prestataire Le nouveau nom du prestataire.
     */
    public void setPrestataire(String prestataire) {
        this.prestataire = prestataire;
    }

    /**
     * Retourne le nom de la catégorie.
     *
     * @return Le nom de la catégorie.
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * Définit la nouvelle catégorie.
     *
     * @param categorie La nouvelle catégorie.
     */
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    /**
     * Redéfinit si l'évèment est accessible aux mineurs.
     *
     * @param convientAuxMineur Boolean qui redéfinit l'accès des mineurs à l'activitée.
     */
    public void setConvientAuxMineur(boolean convientAuxMineur) {
        this.convientAuxMineur = convientAuxMineur;
    }

//    public void choisirCategorie(){
//
//    }

    /**
     * Retourne si l'activité convient aux mineurs.
     *
     * @return si l'activité convient aux mineurs.
     */
    public boolean interdireAuxMineurs(){
        return convientAuxMineur;
    }
}
