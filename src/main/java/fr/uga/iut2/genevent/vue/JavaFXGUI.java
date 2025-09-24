package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.controleur.Controleur;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import fr.uga.iut2.genevent.modele.*;
import fr.uga.iut2.genevent.util.Persisteur;
import fr.uga.iut2.genevent.util.UtilitaireGenEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * La classe JavaFXGUI est responsable des interactions avec
 * l'utilisa·teur/trice en mode graphique.
 * <p>
 * Attention, pour pouvoir faire le lien avec le
 * {@link Controleur}, JavaFXGUI n'est pas une
 * sous-classe de {@link Application} !
 * <p>
 * Le démarrage de l'application diffère des exemples classiques trouvés dans
 * la documentation de JavaFX : l'interface est démarrée à l'initiative du
 * {@link Controleur} via l'appel de la méthode
 * {@link #demarrerInteraction()}.
 */
public class JavaFXGUI extends IHM {

    private final Controleur controleur;
    private final CountDownLatch eolBarrier;  // /!\ ne pas supprimer /!\ : suivi de la durée de vie de l'interface
    private Stage primaryStage;

    // éléments vue nouvel·le utilisa·teur/trice


    public JavaFXGUI(Controleur controleur) {
        this.controleur = controleur;
        this.eolBarrier = new CountDownLatch(1);  // /!\ ne pas supprimer /!\
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Point d'entrée principal pour le code de l'interface JavaFX.
     *
     * @param primaryStage stage principale de l'interface JavaFX, sur laquelle
     *     définir des scenes.
     *
     * @throws IOException si le chargement de la vue FXML échoue.
     *
     * @see Application#start(Stage)
     */
    private void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
//        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("Authentification.fxml"));
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("Authentification.fxml"));

        mainViewLoader.setController(this);
        Scene mainScene = new Scene(mainViewLoader.load());

        primaryStage.setTitle("GELPP");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * permet de changer la vue de la fenêtre principale
     * @param pageFxml vue à consulter
     * @throws IOException si le chargement de la vue échoue
     */
    @FXML
    public void switchScene(String pageFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pageFxml));
        loader.setController(this);
        Scene mainScene = new Scene(loader.load());
        primaryStage.setScene(mainScene);
    }

    /**
     * permet de changer la vue de la fenêtre de l'élément control
     * @param pageFxml vue à consulter
     * @param control un élément dans le stage à modifier
     * @throws IOException si le chargement de la vue échoue
     */
    @FXML
    public void switchScene(String pageFxml, Control control) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pageFxml));
        loader.setController(this);
        Scene mainScene = new Scene(loader.load());
        Stage secondaryStage = (Stage) control.getScene().getWindow();
        secondaryStage.setScene(mainScene);
    }

//-----  Éléments du dialogue  -------------------------------------------------

    private void exitAction() {
        // fermeture de l'interface JavaFX : on notifie sa fin de vie
        this.eolBarrier.countDown();
    }

    // -----------------------------------------------------------------------------------------
    // page Authentification
    // -----------------------------------------------------------------------------------------

    @FXML private Label authentificationLabelLogin, authentificationLabelMdp;
    @FXML private TextField authentificationTfLogin;
    @FXML private PasswordField authentificationPfMdp;
    @FXML private Button authentificationButtonSeConnecter, authentificationButtonSInscrire, authentificationbuttonRetour;

    /**
     * tentative de connexion.
     * si les identifiants sont valides, affiche la page d'accueil
     * sinon affiche une erreur
     * @throws IOException si le chargement de la vue échoue
     */
    @FXML
    private void authentificationSeConnecterClick() throws IOException {
        authentificationResetLabel();
        controleur.seConnecter(authentificationTfLogin.getText(), authentificationPfMdp.getText());
    }

    /**
     * réinitialise les label (pour retirer les potentiels message d'erreur qui ont été résolues)
     */
    private void authentificationResetLabel(){
        authentificationLabelLogin.setText("Login :");
        authentificationLabelLogin.setTextFill(Color.BLACK);
        authentificationLabelMdp.setText("Mot de passe :");
        authentificationLabelMdp.setTextFill(Color.BLACK);
    }

    /**
     * affiche la page d'inscription dans une nouvelle fenêtre
     * @throws IOException si le chargement de la vue échoue
     */
    @FXML
    private void authentificationSInscrireClick() throws IOException {
        Stage secondaryStage = new Stage();
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("Inscription.fxml"));
        mainViewLoader.setController(this);
        Scene mainScene = new Scene(mainViewLoader.load());

        secondaryStage.setTitle("GenEvent");
        secondaryStage.setScene(mainScene);
        secondaryStage.show();
    }

    /**
     * selon l'erreur reçue, modifie le label des champs de saisie
     * @param erreur
     */
    public void authentificationErreurSaisie(int erreur) {
        switch (erreur){
            // Login non renseigné
            case 0 :
                authentificationLabelLogin.setText("Login :  *doit être renseigné");
                authentificationLabelLogin.setTextFill(Color.RED);
                return;
            // Mot de passe non renseigné
            case 1 :
                authentificationLabelMdp.setText("Mot de passe :  *doit être renseigné");
                authentificationLabelMdp.setTextFill(Color.RED);
                return;
            // Login non existant
            case 2 :
                authentificationLabelLogin.setText("Login : *cet utilisateur n'existe pas");
                authentificationLabelLogin.setTextFill(Color.RED);
                return;
            // Mot de pass erroné
            case 3 :
                authentificationLabelMdp.setText("Mot de passe : *mot de passe erroné");
                authentificationLabelMdp.setTextFill(Color.RED);
                return;
        }
    }

    /**
     * lors d'un clic sur le bouton Quitter, ferme l'application
     */
    @FXML
    private void cancelAuthAction() {
        exitAction();
        this.authentificationbuttonRetour.getScene().getWindow().hide();
    }

    // -----------------------------------------------------------------------------------------
    // page Inscription  -----
    // -----------------------------------------------------------------------------------------

    @FXML private TextField inscriptionTfLogin, inscriptionTfNomCopro, inscriptionTfNumRue, inscriptionTfCodePostal;
    @FXML private PasswordField inscriptionPfMdp, inscriptionPfConfirmationMdp;
    @FXML private Button inscriptionButtonSInscrire, inscriptionButtonRetour;

    /**
     * lors d'un clic sur le bouton "S'inscrire"
     * vérifie la validité de tous les champs puis créé une Copro
     * affiche une erreur dans le cas d'une confirmation de mot de passe ratée
     * affiche une erreur dans le cas d'un echec de hachage du mot de passe
     *
     */
    @FXML
    private void inscriptionButtonSInscrireClick() {
        if (inscriptionPfMdp.getText().equals(inscriptionPfConfirmationMdp.getText())){
            try{
                InfosUtilisateur data = new InfosUtilisateur(
                        inscriptionTfLogin.getText().strip().toLowerCase(),
                        UtilitaireGenEvent.hachageMdp(inscriptionPfMdp.getText()),
                        inscriptionTfNomCopro.getText(),
                        new Lieu(inscriptionTfNomCopro.getText(), inscriptionTfNumRue.getText(), inscriptionTfNumRue.getText()));
                this.controleur.creerUtilisateur(data);
                this.inscriptionButtonSInscrire.getScene().getWindow().hide();
            } catch (Exception e) {
                informerUtilisateur("Erreur grave: problème d'encodage du mot de passe.", false);
            }
        }
        else{
            informerUtilisateur("Erreur: le mot de passe et la confirmation de correspondent pas.", false);
        }
    }

    /**
     * lors du clic sur le bouton "Retour" ferme la fenêtre
     */
    @FXML
    private void cancelNewUserAction() {
        this.inscriptionButtonRetour.getScene().getWindow().hide();
    }

    // -----------------------------------------------------------------------------------------
    // Page Accueil
    // -----------------------------------------------------------------------------------------

    @FXML Button accueilbuttonAjouterEvenements, accueilbuttonConsulterEvenements, accueilbuttonAjouterMembre,
            accueilbuttonConsulterMembre, accueilbuttonRetour;

    @FXML GridPane accueilgridProchainsEvenements;


    /**
     * affiche jusqu'à 3 prochains événements.
     * @param evenements tous les événements de la copro
     */
    @FXML
    public void afficherProchainsEvenements(List<Evenement> evenements) {
        accueilgridProchainsEvenements.getChildren().clear();

        int indiceProchainEvenement = UtilitaireGenEvent.indicePremierEvenementFutur(evenements);

        if (indiceProchainEvenement == -1) {
            Label aucun = new Label("Aucun événement à venir.");
            aucun.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13; -fx-font-style: italic;");
            accueilgridProchainsEvenements.add(aucun, 0, 0, 2, 1);
        }
        else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            int indiceDernierEvenementAAfficher;

            if (evenements.size() - indiceProchainEvenement < 3){
                indiceDernierEvenementAAfficher = evenements.size()-1;
            }
            else{
                indiceDernierEvenementAAfficher = indiceProchainEvenement + 2;
            }

            int i = indiceProchainEvenement;
            int ligne = 0;

            while (i <= indiceDernierEvenementAAfficher){

                Evenement event = evenements.get(i);

                Label nom = new Label(event.getTitre());
                Label date = new Label(event.getDateEvenement().format(formatter));

                nom.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 14;");
                date.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 14;");

                accueilgridProchainsEvenements.add(nom, 0, ligne);
                accueilgridProchainsEvenements.add(date, 1, ligne);
                i++;
                ligne++;
            }
        }
    }


    /**
     * lors du clic sur "Ajouter un membre", affiche la page de création d'un membre dans une nouvelle fenêtre
     * @throws IOException si le chargement de la vue échoue
     */
    @FXML
    private void accueilAjoutMembreClick() throws IOException {
        Stage secondaryStage = new Stage();
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("AjoutCoproprietaire.fxml"));
        mainViewLoader.setController(this);
        Scene mainScene = new Scene(mainViewLoader.load());

        secondaryStage.setTitle("GELPP");
        secondaryStage.setScene(mainScene);
        secondaryStage.show();
    }

    /**
     * lors du clic sur "Consulter les membres", affiche la page de consultation des membre dans une nouvelle fenêtre
     * @throws IOException
     */
    @FXML
    private void accueilConsulterMembreClick() throws IOException {
        Stage secondaryStage = new Stage();
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("ConsulterMembres.fxml"));
        mainViewLoader.setController(this);
        Scene mainScene = new Scene(mainViewLoader.load());

        secondaryStage.setTitle("GELPP");
        secondaryStage.setScene(mainScene);
        secondaryStage.show();
        controleur.consulterMembres();
    }

    /**
     * lors du clic sur "Ajouter un événement", affiche la page de choix du type d'événement dans une nouvelle fenêtre
     * @throws IOException si le chargement de la vue échoue
     */
    @FXML
    private void accueilAjoutEvenementCLick() throws IOException {
        Stage secondaryStage = new Stage();
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("PopUpChoixEvenement.fxml"));
        mainViewLoader.setController(this);
        Scene mainScene = new Scene(mainViewLoader.load());

        secondaryStage.setTitle("GELPP");
        secondaryStage.setScene(mainScene);
        secondaryStage.show();
    }

    /**
     * lors du clic sur "Consulter les événements", affiche la page de consultation des événements dans une nouvelle fenêtre
     * @throws IOException si le chargement de la vue échoue
     */
    @FXML
    private void accueilConsulterEvenementClick() throws IOException {
        Stage secondaryStage = new Stage();
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("ConsulterEvenements.fxml"));
        mainViewLoader.setController(this);
        Scene mainScene = new Scene(mainViewLoader.load());

        secondaryStage.setTitle("GELPP");
        secondaryStage.setScene(mainScene);
        secondaryStage.show();
        controleur.consulterEvenements();
    }

    /**
     * lors du clic sur "Se déconnecter", affiche un pop up de confirmation
     * si "oui" est choisi, déconnecte l'utilisateur et retourne sur la page d'authentification
     * si "non" est choisi, la fenêtre se ferme
     * @throws IOException si le chargement de la vue échoue
     */
    @FXML
    private void cancelAccueilAction() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de déconnexion");
        alert.setHeaderText("Voulez-vous vraiment vous déconnecter ?");
        alert.setContentText("Vous allez être redirigé vers la page d'authentification.");

        ButtonType buttonTypeOui = new ButtonType("Se déconnecter");
        ButtonType buttonTypeNon = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOui, buttonTypeNon);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeOui) {
            try {
                Persisteur.sauverEtat(controleur.getGenEvent());
                System.out.println("État de l'application sauvegardé avec succès.");
                switchScene("Authentification.fxml");

            } catch (FileNotFoundException e) {
                System.err.println("Erreur lors de la sauvegarde de l'état : " + e.getMessage());
                showErrorAlert("Erreur de sauvegarde", "Impossible de sauvegarder l'état de l'application.");

            } catch (IOException e) {
                System.err.println("Erreur lors de la sauvegarde de l'état : " + e.getMessage());
                showErrorAlert("Erreur de sauvegarde", "Erreur d'entrée/sortie lors de la sauvegarde.");
            }
        }
    }

    /**
     * permet d'afficher une petite fenêtre d'alerte
     * @param title titre de la fenêtre
     * @param message message d'alerte
     */
    private void showErrorAlert(String title, String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    // -----------------------------------------------------------------------------------------
    // Page AjoutCoproprietaire
    // -----------------------------------------------------------------------------------------

    @FXML Button ajoutCoprobuttonRetour, ajoutCoprobuttonEnregistrer;
    @FXML TextField ajoutCoprotfNomCopro, ajoutCoprotfPrenomCopro, ajoutCoprotfEmail, ajoutCoprotfNumeroTelephone;

    /**
     * lors du clic sur le bouton "Enregistrer", valide la saisie puis créé un nouveau Membre
     */
    @FXML
    private void ajoutCoproEnregistrerClick() {
        String nom = ajoutCoprotfNomCopro.getText().trim();
        String prenom = ajoutCoprotfPrenomCopro.getText().trim();
        String email = ajoutCoprotfEmail.getText().trim();
        String numerotel = ajoutCoprotfNumeroTelephone.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || numerotel.isEmpty()) {
            informerUtilisateur("Erreur: Tous les champs doivent être remplis.", false);
            return;
        }

        Membre nouveauMembre = new Membre(nom, prenom, email, numerotel);
        IHM.InfosMembre data = new IHM.InfosMembre(nouveauMembre);
        this.controleur.creerMembre(data);

        cancelAjoutCoproprietaireAction();
    }

    /**
     * validation du format du numéro de téléphone
     */
    @FXML private void ajoutCoprotfNumeroTelephoneKeyPressed(){
        UtilitaireGenEvent.formatTel(ajoutCoprotfNumeroTelephone);
    }

    /**
     * lors du clic sur le bouton "Retour", ferme la fenêtre
     */
    @FXML
    private void cancelAjoutCoproprietaireAction(){
        this.ajoutCoprobuttonRetour.getScene().getWindow().hide();
    }

    // -----------------------------------------------------------------------------------------
    // Page ConsulterEvenements
    // -----------------------------------------------------------------------------------------

    @FXML Button consulterEvenementsbuttonAjouterEvenement, consulterEvenementsbuttonRetour;
    @FXML private TextField consulterEvenementsrechercheTextField;
    @FXML private VBox listeEvenementsContainer;
    private List<Evenement> tousLesEvenement = new ArrayList<>(); // Stocker tous les membres

    public void initialiserListeEvenements(List<Evenement> evenements) {
        this.tousLesEvenement = evenements;  // Stocker pour le filtrage
        afficherEvenements(evenements);      // Affichage initial

        // Activer le filtre dynamique
        consulterEvenementsrechercheTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            filtrerEvenements(newVal);
        });
    }

    private void filtrerEvenements(String filtre) {
        if (filtre == null || filtre.isEmpty()) {
            afficherEvenements(tousLesEvenement); // Réaffiche tout
            return;
        }

        String filtreMinuscule = filtre.toLowerCase();

        List<Evenement> filtres = tousLesEvenement.stream()
                .filter(e ->
                        e.getTitre().toLowerCase().contains(filtreMinuscule)
                                || e.getLieu().getNom().toLowerCase().contains(filtreMinuscule)
                                || e.getDateEvenement().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")).contains(filtreMinuscule)
                )
                .toList();

        afficherEvenements(filtres);
    }

    /**
     * affiche tous les événements, démarqués selon leur type, ainsi que des boutons d'action
     * @param evenements événements à afficher
     */
    @FXML
    private void afficherEvenements(List<Evenement> evenements) {
        if (listeEvenementsContainer == null){
            Label vide = new Label("Aucun évènement trouvé.");
            vide.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");
            listeEvenementsContainer.getChildren().add(vide);
            return;
        }
        listeEvenementsContainer.getChildren().clear();

        if (evenements.isEmpty()) {
            Label vide = new Label("Aucun évènement trouvé.");
            vide.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");
            listeEvenementsContainer.getChildren().add(vide);
            return;
        }

        for (Evenement evenement : evenements) {
            VBox evenementBox = new VBox();
            evenementBox.setSpacing(5);
            if (evenement.getClass() == EvenementOfficiel.class){
                evenementBox.setStyle("-fx-background-color: orange; -fx-padding: 10; -fx-background-radius: 8; -fx-border-color: #e0e0e0; -fx-border-radius: 8;");

            } else {
                evenementBox.setStyle("-fx-background-color: #32a852; -fx-padding: 10; -fx-background-radius: 8; -fx-border-color: #e0e0e0; -fx-border-radius: 8;");

            }

            Label titre = new Label("Titre : " + evenement.getTitre());
            Label date = new Label("Date : " + evenement.getDateEvenement().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            Label lieu = new Label("Lieu : " + evenement.getLieu().getNom());

            titre.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
            date.setStyle("-fx-text-fill: #34495e;");
            lieu.setStyle("-fx-text-fill: #34495e;");

            // Container pour les boutons (alignement à droite)
            HBox buttonContainer = new HBox(10); // Espacement entre les boutons
            buttonContainer.setAlignment(Pos.CENTER_RIGHT);
            buttonContainer.setStyle("-fx-padding: 10 0 0 0;");

            Button evenementConsulter = new Button("Consulter");
            evenementConsulter.setCursor(Cursor.HAND);
            evenementConsulter.setStyle(
                    "-fx-background-color: linear-gradient(to right, #e91e63, #ff5722); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 14; " +
                            "-fx-background-radius: 20; " +
                            "-fx-padding: 8 20; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1); " +
                            "-fx-cursor: hand;"
            );  evenementConsulter.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (evenement.getClass() == EvenementOfficiel.class){
                        try{
                            switchScene("ConsulterEvenementOfficiel.fxml", evenementConsulter);
                            afficherInformationsEvenementOfficiel(evenement);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else{
                        try{
                            switchScene("ConsulterEvenementSocial.fxml", evenementConsulter);
                            afficherInformationsEvenementSocial(evenement);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            evenementConsulter.setOnMouseEntered(e -> {
                evenementConsulter.setStyle(
                        "-fx-background-color: linear-gradient(to right, #d81b60, #f4511e); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 14; " +
                                "-fx-background-radius: 20; " +
                                "-fx-padding: 8 20; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2); " +
                                "-fx-cursor: hand; " +
                                "-fx-scale-x: 1.05; " +
                                "-fx-scale-y: 1.05;"
                );
            });
            evenementConsulter.setOnMouseExited(e -> {
                evenementConsulter.setStyle(
                        "-fx-background-color: linear-gradient(to right, #e91e63, #ff5722); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 14; " +
                                "-fx-background-radius: 20; " +
                                "-fx-padding: 8 20; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1); " +
                                "-fx-cursor: hand; " +
                                "-fx-scale-x: 1.0; " +
                                "-fx-scale-y: 1.0;"
                );
            });

            Button evenementModifier = new Button("Modifier");
            evenementModifier.setCursor(Cursor.HAND);
            evenementModifier.setStyle(
                    "-fx-background-color: linear-gradient(to right, #e91e63, #ff5722); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 14; " +
                            "-fx-background-radius: 20; " +
                            "-fx-padding: 8 20; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1); " +
                            "-fx-cursor: hand;"
            );  evenementModifier.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    controleur.getGenEvent().modifierEvenementCourant(evenement);
                    if (evenement.getClass() == EvenementOfficiel.class){
                        try {
                            switchScene("ModifierEvenementOfficiel.fxml", evenementModifier);
                            modifierevenementofficieltfTitreEvenement.setText(evenement.getTitre());

                            modifierevenementofficieltfNomLieu.setText(evenement.getLieu().getNom());
                            modifierevenementofficieltfAdresse.setText(evenement.getLieu().getAdresse());
                            modifierevenementofficieltfCodePostal.setText(evenement.getLieu().getCodePostal());
                            modifierevenementofficieltaProcesVerbal.setText(((EvenementOfficiel) evenement).consulterProcesVerbal());

                            LocalDateTime date = evenement.getDateEvenement();
                            modifierevenementofficieldpDateEvenement.setValue(date.toLocalDate());
                            modifierevenementofficieltfHoraireEvenement.setText(date.getHour() + ":" + date.getMinute());

                            modifierevenementofficieltfBudget.setText(evenement.getBudget());

                            if (evenement.isTermine()){
                                modifierevenementofficielcbEvenementTermine.isSelected();
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    else {
                        try {
                            switchScene("ModifierEvenementSocial.fxml", evenementModifier);
                            modifierevenementSocialtfTitreEvenement.setText(evenement.getTitre());

                            modifiertfNomLieu.setText(evenement.getLieu().getNom());
                            modifierevenementSocialtfAdresse.setText(evenement.getLieu().getAdresse());
                            modifiertfCodePostal.setText(evenement.getLieu().getCodePostal());

                            LocalDateTime date = evenement.getDateEvenement();
                            modifierevenementSocialdpDateEvenement.setValue(date.toLocalDate());
                            modifierevenementSocialtfHoraireEvenement.setText(date.getHour() + ":" + date.getMinute());

                            modifierevenementSocialtfBudget.setText(evenement.getBudget());

                            modifiertfPrestataire.setText(((EvenementSocial) evenement).getPrestataire());
                            modifiercheckboxCategorie.setValue(((EvenementSocial) evenement).getCategorie());

                            if (evenement.isTermine()){
                                modifierevenementSocialcbEvenementTermine.isSelected();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            evenementModifier.setOnMouseEntered(e -> {
                evenementModifier.setStyle(
                        "-fx-background-color: linear-gradient(to right, #d81b60, #f4511e); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 14; " +
                                "-fx-background-radius: 20; " +
                                "-fx-padding: 8 20; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2); " +
                                "-fx-cursor: hand; " +
                                "-fx-scale-x: 1.05; " +
                                "-fx-scale-y: 1.05;"
                );
            });
            evenementModifier.setOnMouseExited(e -> {
                evenementModifier.setStyle(
                        "-fx-background-color: linear-gradient(to right, #e91e63, #ff5722); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 14; " +
                                "-fx-background-radius: 20; " +
                                "-fx-padding: 8 20; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1); " +
                                "-fx-cursor: hand; " +
                                "-fx-scale-x: 1.0; " +
                                "-fx-scale-y: 1.0;"
                );
            });


            Button evenementSupprimer = new Button("Supprimer");
            evenementSupprimer.setStyle(
                    "-fx-background-color: linear-gradient(to right, #e74c3c, #c0392b); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 14; " +
                            "-fx-background-radius: 20; " +
                            "-fx-padding: 8 20; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1); " +
                            "-fx-cursor: hand;"
            );

            evenementSupprimer.setOnMouseEntered(e -> {
                evenementSupprimer.setStyle(
                        "-fx-background-color: linear-gradient(to right, #c0392b, #a93226); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 14; " +
                                "-fx-background-radius: 20; " +
                                "-fx-padding: 8 20; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2); " +
                                "-fx-cursor: hand; " +
                                "-fx-scale-x: 1.05; " +
                                "-fx-scale-y: 1.05;"
                );
            });

            evenementSupprimer.setOnMouseExited(e -> {
                evenementSupprimer.setStyle(
                        "-fx-background-color: linear-gradient(to right, #e74c3c, #c0392b); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 14; " +
                                "-fx-background-radius: 20; " +
                                "-fx-padding: 8 20; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1); " +
                                "-fx-cursor: hand; " +
                                "-fx-scale-x: 1.0; " +
                                "-fx-scale-y: 1.0;"
                );
            });

            evenementSupprimer.setOnAction(e -> {
                // Confirmation avant suppression
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirmation de suppression");
                confirmation.setHeaderText("Supprimer l'événement");
                confirmation.setContentText("Êtes-vous sûr de vouloir supprimer " + evenement.getTitre()+ " ?");

                Optional<ButtonType> result = confirmation.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        supprimerEvenement(evenement);
                    } catch (Exception ex) {
                        Alert erreur = new Alert(Alert.AlertType.ERROR);
                        erreur.setTitle("Erreur");
                        erreur.setHeaderText("Erreur lors de la suppression");
                        erreur.setContentText("Impossible de supprimer l'événement : " + ex.getMessage());
                        erreur.showAndWait();
                    }
                }
            });

            buttonContainer.getChildren().addAll(evenementModifier, evenementSupprimer,evenementConsulter);
            evenementBox.getChildren().addAll(titre, date, lieu, buttonContainer);
            listeEvenementsContainer.getChildren().add(evenementBox);
        }
    }

    /**
     * lors du clic sur le bouton "Ajouter", ouvre une nouvelle fenêtre pour choisir un type d'événement
     * @throws IOException
     */
    @FXML
    private void consulterEvenementsAjouterCLick() throws IOException {
        Stage secondaryStage = new Stage();
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("PopUpChoixEvenement.fxml"));
        mainViewLoader.setController(this);
        Scene mainScene = new Scene(mainViewLoader.load());

        secondaryStage.setTitle("GELPP");
        secondaryStage.setScene(mainScene);
        secondaryStage.show();
    }

    /**
     * ferme la fenêtre
     * @throws IOException si le chargement de la vue échoue
     */
    @FXML
    private void cancelConsulterEvenementAction() throws IOException {
        this.consulterEvenementsbuttonRetour.getScene().getWindow().hide();
    }

    /**
     * supprime un événement
     * @param evenement événement à supprimer
     */
    private void supprimerEvenement(Evenement evenement) {
        InfosNouvelEvenement infos = new InfosNouvelEvenement(evenement);
        controleur.supprimerEvenement(infos);
        controleur.consulterEvenements();
    }

    // -----------------------------------------------------------------------------------------
    // Page ConsulterMembres
    // -----------------------------------------------------------------------------------------

    @FXML Button consulterMembresbuttonAjouterMembre, consulterMembresbuttonRetour;

    @FXML private TextField consulterMembrestfrecherche;
    @FXML private VBox listeMembresContainer;
    private List<Membre> tousLesMembres = new ArrayList<>(); // Stocker tous les membres

    public void initialiserListeMembres(List<Membre> membres) {
        this.tousLesMembres = membres;
        afficherMembres(membres);

        consulterMembrestfrecherche.textProperty().addListener((obs, oldVal, newVal) -> {
            filtrerMembres(newVal);
        });
    }

    private void filtrerMembres(String filtre) {
        if (filtre == null || filtre.isEmpty()) {
            afficherMembres(tousLesMembres);
            return;
        }

        String filtreMinuscule = filtre.toLowerCase();

        List<Membre> membresFiltres = tousLesMembres.stream()
                .filter(m -> m.getNom().toLowerCase().contains(filtreMinuscule)
                        || m.getPrenom().toLowerCase().contains(filtreMinuscule)
                        || m.getEmail().toLowerCase().contains(filtreMinuscule))
                .toList();

        afficherMembres(membresFiltres);
    }


    private void afficherMembres(List<Membre> membres) {
        listeMembresContainer.getChildren().clear(); // Vider les anciens

        if (membres.isEmpty()) {
            Label vide = new Label("Aucun membre trouvé.");
            vide.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic; -fx-font-size: 16;");
            listeMembresContainer.getChildren().add(vide);
            return;
        }

        for (Membre membre : membres) {
            VBox membreBox = new VBox(10);
            membreBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 20; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

            // Informations du membre
            Label nomPrenom = new Label(membre.getNom() + " " + membre.getPrenom());
            nomPrenom.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

            Label email = new Label("Email : " + membre.getEmail());
            email.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14;");

            Label telephone = new Label("Téléphone : " + membre.getNumTel());
            telephone.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14;");

            // Container pour les boutons (alignement à droite)
            HBox buttonContainer = new HBox(10); // Espacement entre les boutons
            buttonContainer.setAlignment(Pos.CENTER_RIGHT);
            buttonContainer.setStyle("-fx-padding: 10 0 0 0;");

            // Bouton Modifier
            Button modifierBtn = new Button("Modifier");
            modifierBtn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #e91e63, #ff5722); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 14; " +
                            "-fx-background-radius: 20; " +
                            "-fx-padding: 8 20; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1); " +
                            "-fx-cursor: hand;"
            );

            // Effet hover pour le bouton Modifier
            modifierBtn.setOnMouseEntered(e -> {
                modifierBtn.setStyle(
                        "-fx-background-color: linear-gradient(to right, #d81b60, #f4511e); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 14; " +
                                "-fx-background-radius: 20; " +
                                "-fx-padding: 8 20; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2); " +
                                "-fx-cursor: hand; " +
                                "-fx-scale-x: 1.05; " +
                                "-fx-scale-y: 1.05;"
                );
            });

            modifierBtn.setOnMouseExited(e -> {
                modifierBtn.setStyle(
                        "-fx-background-color: linear-gradient(to right, #e91e63, #ff5722); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 14; " +
                                "-fx-background-radius: 20; " +
                                "-fx-padding: 8 20; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1); " +
                                "-fx-cursor: hand; " +
                                "-fx-scale-x: 1.0; " +
                                "-fx-scale-y: 1.0;"
                );
            });

            modifierBtn.setOnAction(e -> {
                try {
                    ouvrirFenetreModificationMembre(membre);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            // Bouton Supprimer
            Button supprimerBtn = new Button("Supprimer");
            supprimerBtn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #e74c3c, #c0392b); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 14; " +
                            "-fx-background-radius: 20; " +
                            "-fx-padding: 8 20; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1); " +
                            "-fx-cursor: hand;"
            );

            // Effet hover pour le bouton Supprimer
            supprimerBtn.setOnMouseEntered(e -> {
                supprimerBtn.setStyle(
                        "-fx-background-color: linear-gradient(to right, #c0392b, #a93226); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 14; " +
                                "-fx-background-radius: 20; " +
                                "-fx-padding: 8 20; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2); " +
                                "-fx-cursor: hand; " +
                                "-fx-scale-x: 1.05; " +
                                "-fx-scale-y: 1.05;"
                );
            });

            supprimerBtn.setOnMouseExited(e -> {
                supprimerBtn.setStyle(
                        "-fx-background-color: linear-gradient(to right, #e74c3c, #c0392b); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 14; " +
                                "-fx-background-radius: 20; " +
                                "-fx-padding: 8 20; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1); " +
                                "-fx-cursor: hand; " +
                                "-fx-scale-x: 1.0; " +
                                "-fx-scale-y: 1.0;"
                );
            });

            supprimerBtn.setOnAction(e -> {
                // Confirmation avant suppression
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirmation de suppression");
                confirmation.setHeaderText("Supprimer le membre");
                confirmation.setContentText("Êtes-vous sûr de vouloir supprimer " + membre.getNom() + " " + membre.getPrenom() + " ?");

                Optional<ButtonType> result = confirmation.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        supprimerMembre(membre); // Méthode à implémenter
                    } catch (Exception ex) {
                        Alert erreur = new Alert(Alert.AlertType.ERROR);
                        erreur.setTitle("Erreur");
                        erreur.setHeaderText("Erreur lors de la suppression");
                        erreur.setContentText("Impossible de supprimer le membre : " + ex.getMessage());
                        erreur.showAndWait();
                    }
                }
            });

            buttonContainer.getChildren().addAll(modifierBtn, supprimerBtn);
            membreBox.getChildren().addAll(nomPrenom, email, telephone, buttonContainer);
            listeMembresContainer.getChildren().add(membreBox);
        }
    }

    private void supprimerMembre(Membre membre) {
        IHM.InfosMembre infos = new IHM.InfosMembre(
                membre
        );
        controleur.supprimerMembre(infos);
        controleur.consulterMembres();
    }



    private void ouvrirFenetreModificationMembre(Membre membre) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifierMembre.fxml"));
        loader.setController(this);
        Scene scene = new Scene(loader.load());

        Stage stage = new Stage();
        stage.setTitle("Modifier Membre");
        stage.setScene(scene);
        stage.show();

        // Initialiser les champs avec le membre à modifier
        this.initialiserModificationMembre(membre);
    }


    @FXML
    private void consulterMembresAjouterCLick() throws IOException {
        Stage secondaryStage = new Stage();
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("AjoutCoproprietaire.fxml"));
        mainViewLoader.setController(this);
        Scene mainScene = new Scene(mainViewLoader.load());

        secondaryStage.setTitle("GELPP");
        secondaryStage.setScene(mainScene);
        secondaryStage.show();
    }

    @FXML
    private void cancelConsulterMembresAction() throws IOException {
        this.consulterMembresbuttonRetour.getScene().getWindow().hide();
    }

    // -----------------------------------------------------------------------------------------
    // Page ModifierMembre
    // -----------------------------------------------------------------------------------------
    @FXML Button modifierMembrebuttonRetour, modifierMembrebuttonEnregistrer;
    @FXML TextField modifierMembretfNomCopro, modifierMembretfPrenomCopro, modifierMembretfEmail, modifierMembretfNumeroTelephone;

    private Membre membreAEditer = null;

    public void initialiserModificationMembre(Membre membre) {
        this.membreAEditer = membre;

        modifierMembretfNomCopro.setText(membre.getNom());
        modifierMembretfPrenomCopro.setText(membre.getPrenom());
        modifierMembretfEmail.setText(membre.getEmail());
        modifierMembretfNumeroTelephone.setText(membre.getNumTel());
    }

    @FXML
    private void modifierMembreEnregistrerClick() {
        String nom = modifierMembretfNomCopro.getText().trim();
        String prenom = modifierMembretfPrenomCopro.getText().trim();
        String email = modifierMembretfEmail.getText().trim();
        String numerotel = modifierMembretfNumeroTelephone.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || numerotel.isEmpty()) {
            informerUtilisateur("Erreur: Tous les champs doivent être remplis.", false);
            return;
        }

        Membre nouveauMembre = new Membre(nom, prenom, email, numerotel);
        IHM.ModifierMembre data = new IHM.ModifierMembre(membreAEditer, nouveauMembre);

        this.controleur.modifierMembre(data);
        controleur.consulterMembres();
        cancelModifierMembreAction();
    }

    @FXML private void modifierMembrestfNumeroTelephoneKeyPressed(){
        UtilitaireGenEvent.formatTel(modifierMembretfNumeroTelephone);
    }

    @FXML
    private void cancelModifierMembreAction(){
        this.modifierMembrebuttonRetour.getScene().getWindow().hide();
    }


    // -----------------------------------------------------------------------------------------
    // page évènement social
    // -----------------------------------------------------------------------------------------

    @FXML private TextField tfPrestataire, tfNomLieu, tfCodePostal, evenementSocialtfTitreEvenement, evenementSocialtfHoraireEvenement, evenementSocialtfBudget, evenementSocialtfAdresse;
    @FXML private Button eventSocialRetour, buttonAjouterEvenement;
    @FXML private RadioButton radiobuttonOui, radiobuttonNon;
    @FXML private ComboBox checkboxCategorie;
    @FXML private DatePicker evenementSocialdpDateEvenement;
    @FXML private CheckBox evenementSocialcbEvenementTermine;


    @FXML
    private void eventSocialRetour(){
        this.eventSocialRetour.getScene().getWindow().hide();
    }

    @FXML
    private void eventSocialValidationOnClick(){
        String titre = evenementSocialtfTitreEvenement.getText();
        String prestataire = tfPrestataire.getText();
        String NomLieu = tfNomLieu.getText();
        String Adresse = evenementSocialtfAdresse.getText();
        String Rue = evenementSocialtfAdresse.getText();
        String CodePostal = tfCodePostal.getText();
        String Categorie =  checkboxCategorie.getValue().toString();
        boolean mineur = radiobuttonOui.isSelected();

        LocalDate Date = evenementSocialdpDateEvenement.getValue();
        String horraireTxt = evenementSocialtfHoraireEvenement.getText().trim();

        String budget = evenementSocialtfBudget.getText().trim();
        boolean termine = evenementSocialcbEvenementTermine.isSelected();


        if (titre.isEmpty() || prestataire.isEmpty() || NomLieu.isEmpty() || Adresse.isEmpty() || Rue.isEmpty() || CodePostal.isEmpty() || horraireTxt.isEmpty() || budget.isEmpty() || Date == null) {
            informerUtilisateur("Erreur: Tous les champs doivent être remplis.", false);
            return;
        }


        // Conversion heure + date
        LocalDateTime dateHeure;
        try {
            DateTimeFormatter heureFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime heure = LocalTime.parse(horraireTxt, heureFormatter);
            dateHeure = LocalDateTime.of(Date, heure);
        } catch (Exception e) {
            informerUtilisateur("Erreur : Format de l'heure invalide. Format attendu : HH:mm", false);
            return;
        }
        Lieu lieuSoc = new Lieu(NomLieu, Adresse, CodePostal);

        EvenementSocial evenementSocial = new EvenementSocial(mineur, prestataire, Categorie, titre, dateHeure, budget, lieuSoc, termine);
        IHM.InfosNouvelEvenementSoc data = new InfosNouvelEvenementSoc(evenementSocial);
        controleur.creerEvenementSoc(data);

        eventSocialRetour();
    }
    @FXML private void evenementSocialtfHoraireEvenementKeyPressed(){
        UtilitaireGenEvent.formatHeureMin(evenementSocialtfHoraireEvenement);
    }

    @FXML private void evenementSocialtfBudgetKeyPressed(){
        UtilitaireGenEvent.nombreOnly(evenementSocialtfBudget);
    }

    // -----------------------------------------------------------------------------------------
    // Page EvenementOfficiel
    // -----------------------------------------------------------------------------------------

    @FXML Button evenementofficielbuttonRetour, evenementofficielbuttonAjouterEvenements,
            evenementofficielbuttonAjouterVote;

    @FXML TextField evenementofficieltfTitreEvenement, evenementofficieltfNomLieu,
            evenementofficieltfAdresse, evenementofficieltfRue, evenementofficieltfCodePostal, evenementofficieltfHoraireEvenement,
            evenementofficieltfBudget;

    @FXML CheckBox evenementofficielcbEvenementTermine;
    @FXML TextArea evenementofficieltaProcesVerbal;

    @FXML DatePicker evenementofficieldpDateEvenement;

    @FXML private void evenementofficieltfHoraireEvenementKeyPressed(){
        UtilitaireGenEvent.formatHeureMin(evenementofficieltfHoraireEvenement);
    }

    @FXML private void evenementofficieltfBudgetKeyPressed(){
        UtilitaireGenEvent.nombreOnly(evenementofficieltfBudget);
    }

    @FXML
    private void ajoutEvenementOfficielbuttonClick() {
        String titre = evenementofficieltfTitreEvenement.getText();
        String nomLieu = evenementofficieltfNomLieu.getText();
        String adresse = evenementofficieltfAdresse.getText();
        String codePostal = evenementofficieltfCodePostal.getText();

        LocalDate date = evenementofficieldpDateEvenement.getValue();
        String heureTexte = evenementofficieltfHoraireEvenement.getText().trim();

        String budgetTexte = evenementofficieltfBudget.getText().trim();
        boolean terminer = evenementofficielcbEvenementTermine.isSelected();
        String procesVerbal = evenementofficieltaProcesVerbal.getText();

        if (titre.isEmpty() || nomLieu.isEmpty() || adresse.isEmpty() || codePostal.isEmpty() || heureTexte.isEmpty() || budgetTexte.isEmpty() || date == null) {
            informerUtilisateur("Erreur: Tous les champs doivent être remplis.", false);
            return;
        }

        for (Evenement evenementExistant : controleur.getGenEvent().consulterCoproCourante().consulterEvenement()){
            if (date.equals(evenementExistant.getDateEvenement().toLocalDate())
                && (LocalTime.of(Integer.parseInt(heureTexte.substring(0,1)), Integer.parseInt(heureTexte.substring(3,4)))).equals(evenementExistant.getDateEvenement().toLocalTime())){
                informerUtilisateur("Erreur : Un événement existe déjà à cet horaire", false);
            }
        }

        // Conversion heure + date
        LocalDateTime dateHeure;

        try {
            DateTimeFormatter heureFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime heure = LocalTime.parse(heureTexte, heureFormatter);
            dateHeure = LocalDateTime.of(date, heure);
        } catch (Exception e) {
            informerUtilisateur("Erreur : Format de l'heure invalide. Format attendu : HH:mm", false);
            return;
        }
        Lieu lieuOff = new Lieu(nomLieu, adresse ,codePostal);

        EvenementOfficiel evenementOfficiel = new EvenementOfficiel(titre,dateHeure,terminer,budgetTexte,lieuOff);
        evenementOfficiel.modifierProcesVerbal(procesVerbal);
        InfosNouvelEvenementOffi data = new InfosNouvelEvenementOffi(evenementOfficiel);
        controleur.creerEvenement(data);

        controleur.consulterEvenements();
        cancelEvenementOfficielAction();
    }

    @FXML Button buttonAjouterVote;

    @FXML private void buttonAjouterVoteClick() throws IOException {
        Stage voteStage = new Stage();
        FXMLLoader voteViewLoader = new FXMLLoader(getClass().getResource("Vote.fxml"));
        voteViewLoader.setController(this);
        Scene mainScene = new Scene(voteViewLoader.load());
        voteStage.setTitle("GenEvent");
        voteStage.setScene(mainScene);
        voteStage.show();
        controleur.voteAjouterProposition(voteGpPropositions);
    }

    @FXML
    private void cancelEvenementOfficielAction(){
        this.evenementofficielbuttonRetour.getScene().getWindow().hide();
    }

    //
    // Page modifierEvenementOfficiel
    //

    @FXML Button modifierevenementofficielbuttonRetour, modifierevenementofficielbuttonConfirmerModification,
            modifierevenementofficielbuttonModifierVote;

    @FXML TextField modifierevenementofficieltfTitreEvenement, modifierevenementofficieltfNomLieu,
            modifierevenementofficieltfAdresse, modifierevenementofficieltfCodePostal, modifierevenementofficieltfHoraireEvenement,
            modifierevenementofficieltfBudget;

    @FXML CheckBox modifierevenementofficielcbEvenementTermine;
    @FXML TextArea modifierevenementofficieltaProcesVerbal;

    @FXML DatePicker modifierevenementofficieldpDateEvenement;

    @FXML
    private void cancelmodifierEvenementOfficielAction(){
        this.modifierevenementofficielbuttonRetour.getScene().getWindow().hide();
    }

    @FXML private void modifierevenementofficieltfHoraireEvenementKeyPressed(){
        UtilitaireGenEvent.formatHeureMin(modifierevenementofficieltfHoraireEvenement);
    }

    @FXML private void modifierevenementofficieltfBudgetKeyPressed(){
        UtilitaireGenEvent.nombreOnly(modifierevenementofficieltfBudget);
    }

    @FXML private void modifierajoutEvenementOfficielbuttonClick(){
        Evenement evenementCourant = controleur.getGenEvent().consulterEvenementCourant();
        String titre = modifierevenementofficieltfTitreEvenement.getText();
        String nomLieu = modifierevenementofficieltfNomLieu.getText();
        String adresse = modifierevenementofficieltfAdresse.getText();
        String codePostal = modifierevenementofficieltfCodePostal.getText();

        LocalDate date = modifierevenementofficieldpDateEvenement.getValue();
        String heureTexte = modifierevenementofficieltfHoraireEvenement.getText().trim();

        String budgetTexte = modifierevenementofficieltfBudget.getText().trim();
        boolean terminer = modifierevenementofficielcbEvenementTermine.isSelected();
        String procesVerbal = modifierevenementofficieltaProcesVerbal.getText();

        if (titre.isEmpty() || nomLieu.isEmpty() || adresse.isEmpty() || codePostal.isEmpty() || heureTexte.isEmpty() || budgetTexte.isEmpty() || date == null) {
            informerUtilisateur("Erreur: Tous les champs doivent être remplis.", false);
            return;
        }

        for (Evenement evenementExistant : controleur.getGenEvent().consulterCoproCourante().consulterEvenement()){
            if (evenementExistant != evenementCourant && date.equals(evenementExistant.getDateEvenement().toLocalDate())
                    && (LocalTime.of(Integer.parseInt(heureTexte.substring(0,1)), Integer.parseInt(heureTexte.substring(3,4)))).equals(evenementExistant.getDateEvenement().toLocalTime())){
                informerUtilisateur("Erreur : Un événement existe déjà à cet horaire", false);
            };
        }

        // Conversion heure + date
        LocalDateTime dateHeure;

        try {
            DateTimeFormatter heureFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime heure = LocalTime.parse(heureTexte, heureFormatter);
            dateHeure = LocalDateTime.of(date, heure);
        } catch (Exception e) {
            informerUtilisateur("Erreur : Format de l'heure invalide. Format attendu : HH:mm", false);
            return;
        }
        Lieu lieuOff = new Lieu(nomLieu, adresse ,codePostal);

        EvenementOfficiel evenementOfficiel = new EvenementOfficiel(titre,dateHeure,terminer,budgetTexte,lieuOff);
        evenementOfficiel.modifierProcesVerbal(procesVerbal);
        ModifierEvenement data = new ModifierEvenement((EvenementOfficiel) evenementCourant,evenementOfficiel);
        controleur.modifierEvenement(data);

        cancelmodifierEvenementOfficielAction();
    }
    //
    // Page modifierEvenementSocial
    //
    @FXML private TextField modifiertfPrestataire, modifiertfNomLieu, modifiertfCodePostal, modifierevenementSocialtfTitreEvenement, modifierevenementSocialtfHoraireEvenement, modifierevenementSocialtfBudget, modifierevenementSocialtfAdresse;
    @FXML private Button modifiereventSocialRetour, modifierbuttonAjouterEvenement;
    @FXML private RadioButton modifierradiobuttonOui, modifierradiobuttonNon;
    @FXML private ComboBox modifiercheckboxCategorie;
    @FXML private DatePicker modifierevenementSocialdpDateEvenement;
    @FXML private CheckBox modifierevenementSocialcbEvenementTermine;


    @FXML
    private void modifiereventSocialRetour(){
        this.modifiereventSocialRetour.getScene().getWindow().hide();
    }

    @FXML
    private void modifiereventSocialValidationOnClick(){
        Evenement evenementCourant = controleur.getGenEvent().consulterEvenementCourant();
        String titre = modifierevenementSocialtfTitreEvenement.getText();
        String prestataire = modifiertfPrestataire.getText();
        String nomLieu = modifiertfNomLieu.getText();
        String adresse = modifierevenementSocialtfAdresse.getText();
        String codePostal = modifiertfCodePostal.getText();
        String categorie =  modifiercheckboxCategorie.getValue().toString();
        boolean mineur = modifierradiobuttonOui.isSelected();

        LocalDate date = modifierevenementSocialdpDateEvenement.getValue();
        String heureTexte = modifierevenementSocialtfHoraireEvenement.getText().trim();

        String budgetTexte = modifierevenementSocialtfBudget.getText().trim();
        boolean termine = modifierevenementSocialcbEvenementTermine.isSelected();


        if (titre.isEmpty() || nomLieu.isEmpty() || adresse.isEmpty() || codePostal.isEmpty() || heureTexte.isEmpty() || budgetTexte.isEmpty() || date == null) {
            informerUtilisateur("Erreur: Tous les champs doivent être remplis.", false);
            return;
        }

        for (Evenement evenementExistant : controleur.getGenEvent().consulterCoproCourante().consulterEvenement()){
            if (evenementExistant != evenementCourant && date.equals(evenementExistant.getDateEvenement().toLocalDate())
                    && (LocalTime.of(Integer.parseInt(heureTexte.substring(0,1)), Integer.parseInt(heureTexte.substring(3,4)))).equals(evenementExistant.getDateEvenement().toLocalTime())){
                informerUtilisateur("Erreur : Un événement existe déjà à cet horaire", false);
            };
        }

        // Conversion heure + date
        LocalDateTime dateHeure;

        try {
            DateTimeFormatter heureFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime heure = LocalTime.parse(heureTexte, heureFormatter);
            dateHeure = LocalDateTime.of(date, heure);
        } catch (Exception e) {
            informerUtilisateur("Erreur : Format de l'heure invalide. Format attendu : HH:mm", false);
            return;
        }
        Lieu lieuSoc = new Lieu(nomLieu, adresse ,codePostal);

        EvenementSocial evenementSocial = new EvenementSocial(mineur, prestataire, categorie, titre, dateHeure, budgetTexte, lieuSoc, termine);
        ModifierEvenement data = new ModifierEvenement((EvenementSocial) evenementCourant,evenementSocial);
        controleur.modifierEvenement(data);

        modifiereventSocialRetour();
    }

    // -----------------------------------------------------------------------------------------
    // Page PopUpChoixEvenement
    // -----------------------------------------------------------------------------------------


    @FXML Button popupbuttonEvenementOfficiel, popupbuttonEvenementSocial, popupbuttonFermer;


    @FXML
    private void popupEvenementOfficielCLick() throws IOException {
        Stage secondaryStage = new Stage();
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("EvenementOfficiel.fxml"));
        mainViewLoader.setController(this);
        Scene mainScene = new Scene(mainViewLoader.load());

        secondaryStage.setTitle("GELPP");
        secondaryStage.setScene(mainScene);
        secondaryStage.show();
        cancelPopUpAction();
    }

    @FXML
    private void popupEvenementSocialCLick() throws IOException {
        Stage secondaryStage = new Stage();
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("EvenementSocial.fxml"));
        mainViewLoader.setController(this);
        Scene mainScene = new Scene(mainViewLoader.load());

        secondaryStage.setTitle("GELPP");
        secondaryStage.setScene(mainScene);
        secondaryStage.show();
        cancelPopUpAction();
    }

    @FXML
    private void cancelPopUpAction() {
        this.popupbuttonFermer.getScene().getWindow().hide();
    }



    // -----------------------------------------------------------------------------------------
    // page NouveauLieu
    // -----------------------------------------------------------------------------------------

    @FXML TextField lieutfNomLieu;
    @FXML TextField lieutfNumeroAdresse;
    @FXML TextField lieutfNomLieuAdresse;
    @FXML TextField lieutfCodePostal;
    @FXML Button lieuxButtonAjouterLieu;

    @FXML private void lieuxButtonAjouterLieuClick() throws IOException {
        this.lieuxButtonAjouterLieu.getScene().getWindow().hide();
    }

    @FXML private void lieutfNumeroAdresseKeyPressed() throws IOException {
        UtilitaireGenEvent.nombreOnly(lieutfNumeroAdresse);
    }

    @FXML private void lieutfCodePostalKeyPressed() throws IOException {
        UtilitaireGenEvent.nombreOnly(lieutfCodePostal);
    }



    // -----------------------------------------------------------------------------------------
    // page Vote
    // -----------------------------------------------------------------------------------------

    @FXML Button voteButtonRetour, voteButtonMoins1, votebuttonPlus1, voteButtonAjouterProposition, voteButtonValider;
    @FXML TextField voteTfNombreVote1;
    @FXML GridPane voteGpPropositions;

    @FXML
    private void cancelVoteAction() {
        this.voteButtonRetour.getScene().getWindow().hide();
    }

    @FXML private void voteButtonAjouterPropositionClick(){
        controleur.voteAjouterProposition(voteGpPropositions);
    }

    private Vote voteEnCours = null;

    @FXML
    private void voteButtonValiderClick() {
        ajouterVote();
    }

    private void ajouterVote() {
        HashMap<String, Integer> propositionsAvecVotes = new HashMap<>();
        boolean auMoinsUneProposition = false;

        for (Node node : voteGpPropositions.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);

            if (rowIndex != null && colIndex != null && colIndex == 1) {
                if (node instanceof TextField) {
                    TextField tfProposition = (TextField) node;
                    String proposition = tfProposition.getText().trim();

                    if (!proposition.isEmpty()) {
                        for (Node compteurNode : voteGpPropositions.getChildren()) {
                            Integer compteurRow = GridPane.getRowIndex(compteurNode);
                            Integer compteurCol = GridPane.getColumnIndex(compteurNode);

                            if (compteurRow != null && compteurCol != null &&
                                    compteurRow.equals(rowIndex) && compteurCol == 2) {

                                if (compteurNode instanceof HBox) {
                                    HBox compteurHBox = (HBox) compteurNode;
                                    if (compteurHBox.getChildren().size() > 1 &&
                                            compteurHBox.getChildren().get(1) instanceof TextField) {

                                        TextField tfCompteur = (TextField) compteurHBox.getChildren().get(1);
                                        try {
                                            int nombreVotes = Integer.parseInt(tfCompteur.getText());
                                            propositionsAvecVotes.put(proposition, nombreVotes);
                                            auMoinsUneProposition = true;
                                        } catch (NumberFormatException e) {
                                            propositionsAvecVotes.put(proposition, 0);
                                            auMoinsUneProposition = true;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (!auMoinsUneProposition) {
            informerUtilisateur("Erreur : Aucune proposition n'a été saisie pour ce vote.", false);
            return;
        }

        if (voteEnCours == null) {
            voteEnCours = new Vote();
        }

        for (String proposition : propositionsAvecVotes.keySet()) {
            voteEnCours.ajouterProposition(proposition);
            voteEnCours.modifierNombreVote(proposition, propositionsAvecVotes.get(proposition));
        }

        StringBuilder messageConfirmation = new StringBuilder();
        messageConfirmation.append("Vote créé avec succès !\n\nPropositions et votes :\n");

        propositionsAvecVotes.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> {
                    messageConfirmation.append("• ").append(entry.getKey())
                            .append(" : ").append(entry.getValue())
                            .append(" vote(s)\n");
                });

        informerUtilisateur(messageConfirmation.toString(), true);
        this.voteButtonValider.getScene().getWindow().hide();
    }

//-----  Implémentation des méthodes abstraites  -------------------------------

    @Override
    public void demarrerInteraction() {
        // démarrage de l'interface JavaFX
        Platform.startup(() -> {
            Stage primaryStage = new Stage();
            primaryStage.setOnCloseRequest((WindowEvent t) -> this.exitAction());
            try {
                this.start(primaryStage);
            }
            catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        });

        // attente de la fin de vie de l'interface JavaFX
        try {
            this.eolBarrier.await();
        }
        catch (InterruptedException exc) {
            System.err.println("Erreur d'exécution de l'interface.");
            System.err.flush();
        }
    }

    @Override
    public void informerUtilisateur(String msg, boolean succes) {
        final Alert alert = new Alert(
                succes ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING
        );
        alert.setTitle("GenEvent");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @Override
    public void saisirNouvelEvenement(List<Evenement> nomsExistants) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // -----------------------------------------------------------------------------------------
    // page ConsulterEvenementOfficiel
    // -----------------------------------------------------------------------------------------

    @FXML Button consulterEvenementofficielbuttonRetour, consulterEvenementofficielbuttonVoirVote;

    @FXML private Label consulteEvenementOfficiellabelTitreEvenement;
    @FXML private Label consulterEvenementofficiellabelNomLieu;
    @FXML private Label consulterEvenementofficiellabelAdresse;
    @FXML private Label consulterEvenementofficiellabelRue;
    @FXML private Label consulterEvenementofficiellabelCodePostal;
    @FXML private Label consulterEvenementofficiellabelDateEvenement;
    @FXML private Label ConsulterEvenementofficiellabelHeureEvenement;
    @FXML private Label ConsulterEvenementofficiellabelBudget;
    @FXML private Label evenementofficiellabelProcesVerbal;
    @FXML CheckBox ConsulterEvenementofficielcbEvenementTermine;
    @FXML TextArea ConsulterEvenementofficieltaProcesVerbal;

    public void afficherInformationsEvenementOfficiel(Evenement evenement) {
        afficherInformationsEvenementOff((EvenementOfficiel) evenement);
    }

    public void afficherInformationsEvenementOff(EvenementOfficiel evenement) {
        if (evenement == null) return;


        consulteEvenementOfficiellabelTitreEvenement.setText(evenement.getTitre());
        consulterEvenementofficiellabelNomLieu.setText(evenement.getLieu().getNom());
        consulterEvenementofficiellabelAdresse.setText(evenement.getLieu().getAdresse());
        consulterEvenementofficiellabelCodePostal.setText(evenement.getLieu().getCodePostal());

        consulterEvenementofficiellabelDateEvenement.setText(
                evenement.getDateEvenement().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );

        ConsulterEvenementofficiellabelHeureEvenement.setText(
                evenement.getDateEvenement().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
        );

        ConsulterEvenementofficiellabelBudget.setText(evenement.getBudget());

        if (evenement.consulterProcesVerbal() != null && !evenement.consulterProcesVerbal().isBlank()) {
            evenementofficiellabelProcesVerbal.setText(evenement.consulterProcesVerbal());
        } else {
            evenementofficiellabelProcesVerbal.setText("Pas encore de procés verbal");
        }
    }

    @FXML
    private void cancelConsulterEvenementOfficiel() {
        this.consulterEvenementofficielbuttonRetour.getScene().getWindow().hide();
    }


    // -----------------------------------------------------------------------------------------
    // page ConsulterEvenementSocial
    // -----------------------------------------------------------------------------------------

    public void afficherInformationsEvenementSocial(Evenement evenement) {
        afficherInformationsEvenementSoc((EvenementSocial) evenement);
    }

    @FXML Label consulterEvenementSociallabelTitre, consulterEvenementSociallabelPresataire, consulterEvenementSociallabelLieu,
            consulterEvenementSociallabelRue, consulterEvenementSociallabelCodePostal, consulterEvenementSociallabelCategorie, consulterEvenementSociallabelMineur,
            consulterEvenementSociallabelDate, consulterEvenementSociallabelBudget, consulterEvenementSociallabelHoraire;

    @FXML Button evenementSocialButtonRetour;

    public void afficherInformationsEvenementSoc(EvenementSocial evenement) {
        if (evenement == null) return;

        consulterEvenementSociallabelTitre.setText(evenement.getTitre());
        consulterEvenementSociallabelPresataire.setText(evenement.getPrestataire());
        consulterEvenementSociallabelLieu.setText(evenement.getLieu().getNom());
        consulterEvenementSociallabelRue.setText(evenement.getLieu().getAdresse());
        consulterEvenementSociallabelCodePostal.setText(evenement.getLieu().getCodePostal());
        consulterEvenementSociallabelCategorie.setText(evenement.getCategorie());

        if (evenement.interdireAuxMineurs()){
            consulterEvenementSociallabelMineur.setText("Oui");
        } else consulterEvenementSociallabelMineur.setText("Non");



        consulterEvenementSociallabelDate.setText(
                evenement.getDateEvenement().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );

        consulterEvenementSociallabelHoraire.setText(" "+
                evenement.getDateEvenement().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
        );

        consulterEvenementSociallabelBudget.setText(evenement.getBudget());
    }


    @FXML
    private void evenementSocialButtonRetourClick(){
        this.evenementSocialButtonRetour.getScene().getWindow().hide();
    }

}
