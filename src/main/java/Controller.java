import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.util.Optional;

public class Controller {

    @FXML
    private TextArea textArea;
    @FXML
    private Menu menuFermer;
    @FXML
    private StackPane stackPaneBottom;
    @FXML
    private AnchorPane anchorPaneBottom;

    private IntegerProperty numberOfWords = new SimpleIntegerProperty();
    private Racer racer;

    @FXML
    void initialize() {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            numberOfWords.set(newValue.split(" ").length);
        });
        showWelcome();
    }

    private void showWelcome() {
        TextInputDialog dialog = new TextInputDialog("500");
        dialog.setTitle("Objectif");
        dialog.setContentText("Déterminez un objecif de productivité en mots/heure (1 page = 350 mots.");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> {
            int objective = 0;
            try {
                objective = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Veuillez entrer un nombre entier positif.");
                alert.showAndWait();
            }
            racer = initRacer(objective, numberOfWords);
            racer.start();
        });
    }

    private Racer initRacer(int objective, IntegerProperty numberOfWords) {
        Racer racer = new RacerImpl(objective, numberOfWords);
        Util.setAllAnchorsTo0(racer);
        anchorPaneBottom.getChildren().add(0, racer);
        return racer;
    }

}
