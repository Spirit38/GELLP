package fr.uga.iut2.genevent.modele;

public enum CategorieEvenementSocial {

    VOISINS("Fête des voisins"),
    PETANQUE("Pétanque"),
    ANNIV("Anniversaire"),
    REPAS("Repas"),
    AUTRE("Autre");

    private String libelle;

    private CategorieEvenementSocial(String libelle){
        this.libelle = libelle;
    }

    @Override
    public String toString(){
        return libelle;
    }

}
