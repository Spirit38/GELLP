package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.modele.*;

import java.util.List;


public abstract class IHM {
    /**
     * Classe conteneur pour les informations saisies à propos d'un
     * {@link fr.uga.iut2.genevent.modele.Utilisateur}.
     *
     * <ul>
     * <li>Tous les attributs sont `public` par commodité d'accès.</li>
     * <li>Tous les attributs sont `final` pour ne pas être modifiables.</li>
     * </ul>
     */
    public static class InfosUtilisateur {
        public final String login;
        public final String motDePasse;
        public final String nomCopro;
        public final Lieu lieu;

        public InfosUtilisateur(final String login, final String motDePasse, String nomCopro, Lieu lieu) {
            this.login = login;
            this.motDePasse = motDePasse;
            this.nomCopro = nomCopro;
            this.lieu = lieu;
        }
    }

    public static class InfosMembre {
        public Membre membre;


        public InfosMembre(Membre membre) {
            this.membre = membre;
        }

    }

    public static class ModifierMembre {
        public Membre membre;
        public Membre nouveauMembre;

        public ModifierMembre(Membre membre, Membre nouveauMembre){
            this.membre = membre;
            this.nouveauMembre = nouveauMembre;
        }
    }

    public static class InfosNouvelEvenementOffi {
        public Evenement evenement;

        public InfosNouvelEvenementOffi(Evenement evenement) {
            this.evenement = evenement;
        }
    }

    public static class ModifierEvenement {
        public Evenement evenement;
        public Evenement nouvelEvenement;


        public ModifierEvenement(EvenementOfficiel evenementOfficiel, EvenementOfficiel nouvelEvenementOfficiel){
            this.evenement = evenementOfficiel;
            this.nouvelEvenement = nouvelEvenementOfficiel;
        }

        public ModifierEvenement(EvenementSocial evenementSocial, EvenementSocial nouvelEvenementSocial){
            this.evenement = evenementSocial;
            this.nouvelEvenement = nouvelEvenementSocial;
        }
    }

    public static class InfosNouvelEvenementSoc {
        public EvenementSocial evenementSocial;

        public InfosNouvelEvenementSoc(EvenementSocial evenementSocial) {
            this.evenementSocial = evenementSocial;
        }
    }

    public static class InfosNouvelEvenement {
        public Evenement evenement;

        public InfosNouvelEvenement(Evenement evenement) {
            this.evenement = evenement;
        }
    }

    public static class ConsulterEvent {
        public Evenement evenement;

        public ConsulterEvent(Evenement evenement) {
            this.evenement = evenement;
        }
    }
    /**
     * Rend actif l'interface Humain-machine.
     *
     * L'appel est bloquant : le contrôle est rendu à l'appelant une fois que
     * l'IHM est fermée.
     *
     */
    public abstract void demarrerInteraction();

    /**
     * Affiche un message d'information à l'attention de l'utilisa·teur/trice.
     *
     * @param msg Le message à afficher.
     *
     * @param succes true si le message informe d'une opération réussie, false
     *     sinon.
     */
    public abstract void informerUtilisateur(final String msg, final boolean succes);

    /**
     * Récupère les informations nécessaires à la création d'un nouvel
     * {@link fr.uga.iut2.genevent.modele.Evenement}.
     *
     * @param nomsExistants L'ensemble des noms d'évenements qui ne sont plus
     *     disponibles.
     *
     */
    public abstract void saisirNouvelEvenement(final List<Evenement> nomsExistants);
}
