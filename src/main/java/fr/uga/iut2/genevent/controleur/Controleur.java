package fr.uga.iut2.genevent.controleur;

import fr.uga.iut2.genevent.exceptions.MauvaisMotDePasse;
import fr.uga.iut2.genevent.modele.*;
import fr.uga.iut2.genevent.util.UtilitaireGenEvent;
import fr.uga.iut2.genevent.vue.IHM;
// import fr.uga.iut2.genevent.vue.CLI;
import fr.uga.iut2.genevent.vue.JavaFXGUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class Controleur {

    private final GenEvent genevent;
    private final IHM ihm;

    public Controleur(GenEvent genevent) {
        this.genevent = genevent;
        // choisir la classe CLI ou JavaFXGUI
//        this.ihm = new CLI(this);
        this.ihm = new JavaFXGUI(this);
    }

    public void demarrer() {
        this.ihm.demarrerInteraction();
    }

    // -----------------------------------------------------------------------------------------
    // page Authentification
    // -----------------------------------------------------------------------------------------

    /**
     *
     * @erreurs:
     *      0 : Login vide
     *      1 : Mdp vide
     *      2 : Login invalide
     *      3 : Mdp invalide
     * @param login login rentré par l'utilisateur
     * @param mdp mot de passe (non haché) rentré par l'utilisateur. il sera haché avant d'être stocké
     * @throws IOException si le changement de fenêtre ne fonctionne pas
     */
    public void seConnecter(String login, String mdp) throws IOException {
        // Gestion d'erreurs
        if (login.isEmpty()){
            ((JavaFXGUI) ihm).authentificationErreurSaisie(0);
        }
        else if (mdp.isEmpty()){
            ((JavaFXGUI) ihm).authentificationErreurSaisie(1);
        }
        else if (!genevent.existeUtilisateur(login)){
            ihm.informerUtilisateur("Cet utilisateur n'existe pas.", false);
            ((JavaFXGUI) ihm).authentificationErreurSaisie(2);
        }
        else {
            try {
                mdp = UtilitaireGenEvent.hachageMdp(mdp);
                Copro coproCourante = genevent.validationMotDePasse(login, mdp); // Peut causer MauvaisMotDePasse

                // Ajout de l'utilisateur saisi en tant qu'utilisateur courant
                genevent.seConnecter(coproCourante);
                ((JavaFXGUI) ihm).switchScene("Accueil.fxml");
                List<Evenement> evenements = genevent.consulterCoproCourante().consulterEvenement();
                ((JavaFXGUI) ihm).afficherProchainsEvenements(evenements);

                // Gestion d'erreurs
            } catch (MauvaisMotDePasse e) {
                ihm.informerUtilisateur(e.getMessage(), false);
                ((JavaFXGUI) ihm).authentificationErreurSaisie(3);
            } catch (NoSuchAlgorithmException e){
                ihm.informerUtilisateur(e.getMessage(), false);
            }
        }
    }

    // -----------------------------------------------------------------------------------------
    // page Inscription
    // -----------------------------------------------------------------------------------------

    public void creerUtilisateur(IHM.InfosUtilisateur infos) {

        boolean nouvelUtilisateur = this.genevent .ajouteUtilisateur(
                infos.login,
                infos.motDePasse,
                infos.nomCopro,
                infos.lieu
        );
        if (nouvelUtilisateur) {
            this.ihm.informerUtilisateur(
                    "Nouvel utilisateur : " + infos.login,
                    true
            );
        } else {
            this.ihm.informerUtilisateur(
                    "L'utilisateur " + infos.login + " est déjà connu de GenEvent.",
                    false
            );
        }
    }

    // -----------------------------------------------------------------------------------------
    // page CreationEvenement
    // -----------------------------------------------------------------------------------------

    public void saisirEvenement() {
        this.ihm.saisirNouvelEvenement(this.genevent.getEvenements());
    }

    public void creerEvenement(IHM.InfosNouvelEvenementOffi infos) {
        Copro copro = genevent.consulterCoproCourante();

        boolean nouveauEventOff = copro.creerEvenement(infos.evenement);
        if (nouveauEventOff) {
            this.ihm.informerUtilisateur(
                    "Nouvel événement : " + infos.evenement.getTitre(),
                    true
            );
            List<Evenement> evenements = copro.consulterEvenement();
            ((JavaFXGUI) ihm).afficherProchainsEvenements(evenements);
        } else {
            this.ihm.informerUtilisateur(
                    "L'événement : " + infos.evenement.getTitre() + " existe déjà.",
                    false
            );
        }

    }

    public void creerEvenementSoc(IHM.InfosNouvelEvenementSoc infos) {
        Copro copro = genevent.consulterCoproCourante();

        boolean nouveauEventSoc = copro.creerEvenement(infos.evenementSocial);
        if (nouveauEventSoc) {
            this.ihm.informerUtilisateur(
                    "Nouvel événement : " + infos.evenementSocial.getTitre(),
                    true
            );
            List<Evenement> evenements = copro.consulterEvenement();
            ((JavaFXGUI) ihm).afficherProchainsEvenements(evenements);
        } else {
            this.ihm.informerUtilisateur(
                    "L'événement : " + infos.evenementSocial.getTitre() + " existe déjà.",
                    false
            );
        }

    }

    // -----------------------------------------------------------------------------------------
    // page ConsulterEvenements
    // -----------------------------------------------------------------------------------------

    public void consulterEvenements() {
        Copro copro = genevent.consulterCoproCourante();
        List<Evenement> evenements = copro.consulterEvenement();

        ((JavaFXGUI) ihm).initialiserListeEvenements(evenements);
    }

    public void modifierEvenement(IHM.ModifierEvenement modifierEvenement){
        Copro copro = genevent.consulterCoproCourante();

        boolean modif = copro.modifierEvenementOfficiel(
                modifierEvenement.evenement,
                modifierEvenement.nouvelEvenement
        );
        if (modif) {
            this.ihm.informerUtilisateur(
                    "Événement modifié : " + modifierEvenement.evenement.getTitre(),
                    true
            );
        } else {
            this.ihm.informerUtilisateur(
                    "L'événement : " + modifierEvenement.nouvelEvenement.getTitre() + " existe déjà.",
                    false
            );
        }
    }
    public void supprimerEvenement(IHM.InfosNouvelEvenement infos) {
        Copro copro = genevent.consulterCoproCourante();
        boolean supprime = copro.supprimerEvenement(infos.evenement);

        if (supprime) {
            this.ihm.informerUtilisateur(
                    "Événement supprimé : " + infos.evenement.getTitre(),
                    true
            );
        } else {
            this.ihm.informerUtilisateur(
                    "L'événement : " + infos.evenement.getTitre() + " n'existe pas.",
                    false
            );
        }
    }

    // -----------------------------------------------------------------------------------------
    // page ConsulterMembres
    // -----------------------------------------------------------------------------------------

    public void consulterMembres() {
        Copro copro = genevent.consulterCoproCourante();
        List<Membre> membres = copro.consulterMembres();

        ((JavaFXGUI) ihm).initialiserListeMembres(membres);
    }

    public void modifierMembre(IHM.ModifierMembre modifierMembre){
        Copro copro = genevent.consulterCoproCourante();

        boolean modif = copro.modifierMembre(
                modifierMembre.membre,
                modifierMembre.nouveauMembre
        );
        if (modif) {
            this.ihm.informerUtilisateur(
                    "Membre modifier : " + modifierMembre.nouveauMembre.getNom(),
                    true
            );
        } else {
            this.ihm.informerUtilisateur(
                    "Le membre : " + modifierMembre.nouveauMembre.getNom() + " existe déjà.",
                    false
            );
        }
    }
    public void supprimerMembre(IHM.InfosMembre infos) {
        Copro copro = genevent.consulterCoproCourante();
        boolean supprime = copro.supprimerMembre(infos.membre);

        if (supprime) {
            this.ihm.informerUtilisateur(
                    "Membre supprimé : " + infos.membre.getNom(),
                    true
            );
        } else {
            this.ihm.informerUtilisateur(
                    "Le membre : " + infos.membre.getNom() + " n'existe pas.",
                    false
            );
        }
    }

    // -----------------------------------------------------------------------------------------
    // page CreerMembre
    // -----------------------------------------------------------------------------------------

    public void creerMembre(IHM.InfosMembre infosMembre){
        Copro copro = genevent.consulterCoproCourante();
        boolean nouvelUtilisateur = copro.creerMembre(
                infosMembre.membre
        );
        if (nouvelUtilisateur) {
            this.ihm.informerUtilisateur(
                    "Nouveaux membre : " + infosMembre.membre.getNom(),
                    true
            );
        } else {
            this.ihm.informerUtilisateur(
                    "Le membre : " + infosMembre.membre.getNom() + " existe déjà.",
                    false
            );
        }
    }

    // -----------------------------------------------------------------------------------------
    // page Vote
    // -----------------------------------------------------------------------------------------

    private String voteModificationCompteur(int increment, String compteur){
        int nouveauCompteur = Integer.parseInt(compteur) + increment;
        if (nouveauCompteur < 0) nouveauCompteur = 0;
        return Integer.toString(nouveauCompteur);
    }

    private void voteSetupEventsCompteur(Button buttonMoins, Button buttonPlus, TextField tfCompteur){
        // Events buttonMoins
        buttonMoins.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tfCompteur.setText(voteModificationCompteur(-1, tfCompteur.getText()));
            }
        });

        // Events buttonPlus
        buttonPlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tfCompteur.setText(voteModificationCompteur(1, tfCompteur.getText()));
            }
        });

        // Events tfCompteur
        tfCompteur.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                UtilitaireGenEvent.nombreOnly(tfCompteur);
            }
        });
    }


    private HBox voteCreationCompteur(){
        // Création des éléments FXML
        Button buttonMoins = new Button("-");
        buttonMoins.setCursor(Cursor.HAND);
        buttonMoins.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20; -fx-min-width: 35; -fx-min-height: 35; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1);");
        Button buttonPlus = new Button("+");
        buttonPlus.setCursor(Cursor.HAND);
        buttonPlus.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 20; -fx-min-width: 35; -fx-min-height: 35; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1);");
        TextField tfCompteur = new TextField("0");
        tfCompteur.setAlignment(Pos.CENTER);
        tfCompteur.setStyle("-fx-background-radius: 8; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-border-width: 1; -fx-font-size: 14; -fx-font-weight: bold;");

        // Ajout des événements associés
        voteSetupEventsCompteur(buttonMoins, buttonPlus, tfCompteur);

        // Ajout dans une HBox
        HBox compteur = new HBox(10);
        compteur.setAlignment(Pos.CENTER);
        compteur.getChildren().add(buttonMoins);
        compteur.getChildren().add(tfCompteur);
        compteur.getChildren().add(buttonPlus);
        return compteur;
    }

    private Button voteCreationDelete(GridPane propositions) {
        Button voteButtonDelete = new Button();
        voteButtonDelete.setCursor(Cursor.HAND);
        voteButtonDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20; -fx-min-width: 35; -fx-min-height: 35; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1);");
        voteButtonDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int ligneASupprimer = GridPane.getRowIndex(voteButtonDelete);

                propositions.getChildren().removeIf(node -> {
                    Integer rowIndex = GridPane.getRowIndex(node);
                    return rowIndex != null && rowIndex == ligneASupprimer;
                });

                propositions.getChildren().forEach(node -> {
                    Integer rowIndex = GridPane.getRowIndex(node);
                    if (rowIndex != null && rowIndex > ligneASupprimer) {
                        GridPane.setRowIndex(node, rowIndex - 1);
                    }
                });
            }
        });
        return voteButtonDelete;
    }

    private TextField voteCreationTfProposition(){
        TextField tfProposition = new TextField();
        tfProposition.setPrefWidth(50.0);
        tfProposition.setAlignment(Pos.CENTER_LEFT);
        tfProposition.setStyle("-fx-background-radius: 8; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-border-width: 1; -fx-font-size: 14;");
        tfProposition.setPromptText("Entrez votre proposition...");
        return tfProposition;
    }

    public void voteAjouterProposition(GridPane propositions){
        if (propositions.getRowCount() >= 6){
            return;
        }
        else{
            Button buttonDelete = voteCreationDelete(propositions);
            propositions.addRow(propositions.getRowCount(),(voteCreationDelete(propositions)), voteCreationTfProposition(), voteCreationCompteur());
        }

    }

    public GenEvent getGenEvent() {
        return this.genevent;
    }
}
