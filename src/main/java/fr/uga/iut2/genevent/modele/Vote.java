package fr.uga.iut2.genevent.modele;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Vote{
    private HashMap<String,Integer> propositions;
    private int nbVotantMax;//récupéré par du Membre.get().size

    /**
     * Constructeur de Vote
     */
    public Vote(){
        propositions = new HashMap<>();
    }

    /**
     * @param nbVotantMax
     * Constructeur de Vote permetant d'avoir un nombre de votant maximal
     */
    public Vote(int nbVotantMax){
        propositions = new HashMap<>();
        this.nbVotantMax=nbVotantMax;
    }

    /**
     * @param laPropo
     * ajoute laPropo de la liste si elle n'existe pas
     */
    public void ajouterProposition(String laPropo){
        if (!propositions.containsKey(laPropo))
            propositions.put(laPropo, 0);
    }

    /**
     * @param laPropo
     * enleve laPropo de la liste si elle existe
     */
    public void supprimerProposition(String laPropo){
        if (propositions.containsKey(laPropo))
            propositions.remove(laPropo);
    }


    /**
     * affiche la liste des propositions
     * @return propositions
     */
    public HashMap<String, Integer> consulterPropositions() {
        return propositions;
    }

    /**
     * @param nbVotantMax
     * change le nombre votan maximum (fonction utile que si on a déja créer un vote puis ajouter unmembre puis fait le vote)
     */
    public void modifierNombre(int nbVotantMax){
        this.nbVotantMax = nbVotantMax;
    }

    /**
     * @param propos
     * @param nbVote
     * prend la proposition et lui attribue une nouvelle valeur
     * ATTENTION risque de planter si la proposition est mal écrite
     */
    //TO DO un exeption
    //si temps: si 1 personne peut faire qu'un vote: nbVote<= nbVoteMax- autre choix voté
    public void modifierNombreVote(String propos, int nbVote){
        if (nbVote>nbVotantMax)
            nbVote=nbVotantMax;
        if(propositions.containsKey(propos))
            propositions.replace(propos, nbVote);
        else
            System.out.println("erreur");
    }


    //fonction en cours: A FINIR
    //mon probleme c'est plusVoteInt qui veut étre en final pour fonctioner (super pour trouver un maximum)

    /**
     * envoie une HashMap avec toutes les propositions (et leurs scores) ayant eu le plus de vote
     * prend en compte les égalités
     * @return HashMap<String, Integer>
     */
    public HashMap<String, Integer> gagnant(){
        String plusVoteString = "";
        HashMap<String, Integer> potenGagnant = new HashMap<>();

        AtomicInteger plusVoteInt= new AtomicInteger(6);
        propositions.forEach((k, v) -> {
//            System.out.println(potenGagnant.values());
//            System.out.println(k + " -> " + v);
            if (v > plusVoteInt.get()) {
                potenGagnant.clear();
                plusVoteInt.set(v);
            }
            if (v >= plusVoteInt.get())
                potenGagnant.putIfAbsent(k, v);
            System.out.println(potenGagnant);
        } );

        HashMap<String,Integer> proposit = new HashMap<>();
        return proposit;
    }

}
