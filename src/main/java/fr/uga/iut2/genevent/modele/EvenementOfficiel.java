package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EvenementOfficiel extends Evenement implements Serializable {

    /** Numéro de version pour la sérialisation. */
    private static final long serialVersionUID = 1L;

    private String sujet;
    private String procesVerbal;
    ArrayList<Vote> votes = new ArrayList<>();

     /**
     * Constructeur pour créer un nouvel évènement officiel
     *
     * @param titre Le titre de l'événement.
     * @param dateEvenement La date de l'événement.
     * @param termine Indique si l'événement est terminé.
     * @param budget Le budget de l'événement.
     * @param lieu Le lieu de l'événement.
     * */
    public EvenementOfficiel(String titre, LocalDateTime dateEvenement, boolean termine, String budget, Lieu lieu) {
        super(titre, dateEvenement, budget, lieu);
        sujet = "";
        procesVerbal = "";
    }

//    public EvenementOfficiel(GenEvent genevent, String nom, LocalDate dateDebut, LocalDate dateFin) {
//        super(genevent, nom, dateDebut, dateFin);
//        sujet = "";
//        procesVerbal = "";
//    }

    /**
     * Modifie le contenu du sujet
     * à verifier avant que quand il le modifie il recoit ce qui était écrit avant
     */
    public void modifierSujet(String contenu){
        sujet = contenu;
    }

    /**
     * Affiche le contenu écrit dans le sujet
     * @return sujet
     */
    public String consulterSujet(){
        return sujet;
    }

    /**
     * @param vote
     * Ajoute un vote dans la liste de votes
     */
    public void creerVote(Vote vote){
        votes.add(vote);
    }

    /**
     * @param vote
     * supprime un vote dans la liste de votes
     */
    public void supprimerVote(Vote vote){
        votes.remove(vote);
    }

    /**
     * retourne la liste de tous les vote de l'Événement Officiel
     * @return votes
     */
    public ArrayList<Vote> consulterVotes(){
        return votes;
    }

    public String consulterProcesVerbal(){
        return procesVerbal;
    }

    public void modifierProcesVerbal(String contenu){
        procesVerbal = contenu;
    }

}
