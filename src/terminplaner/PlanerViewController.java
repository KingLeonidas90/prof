package terminplaner;

import adressbuch.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

/**
 * FXML Controller class fuer die Terminplaner-Hauptansicht.
 *
 * @author beuth
 */
public class PlanerViewController implements Initializable {


    private Terminplaner planer;
    private Adressbuch adressen;
    private ObservableList<Termin> data;

    @FXML
    private MenuBar menuBar;

    @FXML
    private DatePicker date;

    @FXML
    private Label titel;

    @FXML
    private Button addButton;

    @FXML
    private ListView<Termin> terminliste;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Adressbuch initialisiert
        adressen = new Adressbuch();
        Kontakt john = null;

        // Termin erstellt für den Kontakt John aus dem Adressbuch
        try {
            john = adressen.getKontakt("john");
        } catch (UngueltigerSchluesselException e) {
            System.out.println("Im Adressbuch ist kein Kontakt namens John enthalten");
        }
        planer = new Terminplaner(john);

        // Datepicker mit dem aktuellen Datum eingestellt und einen Event Handler initialisiert
        date.setValue(LocalDate.now());
        date.setOnAction((e) -> showTermine());

        // AddButton mit Event Handler initialisiert
        addButton.setOnAction(event -> addTermin());

        // Erzeugen einer Instanz von ObservableList<Termin>
        data = FXCollections.observableArrayList();

        //angepasste MenuBar laden
        configureMenu();
        configureList();
        showTermine();



    }

    private void addTermin() {
        URL location = new ViewHelper().getClass().getResource("../terminplaner/terminView.fxml");

        // Da es sich um einen neuen Termin handelt, muss dem Kontruktor der Wert null Als Termin übergeben werden
        Initializable controller = new TerminViewController(null, this);
        ViewHelper.showView(controller, location);

    }

    private void showTermine() {
        terminliste.getItems().clear();
        date.setValue(getSelectedDate());
        if (planer.getTermineTag(date.getValue()) != null) {

            data.addAll(planer.getTermineTag(date.getValue()));
        } else {
            terminliste.getItems().clear();
        }
        terminliste.setItems(data);


    }

    private void configureMenu() {
        MenuItem laden = new MenuItem("Laden");
        laden.setOnAction(e -> loadTermine());
        MenuItem speichern = new MenuItem("Speichern");
        speichern.setOnAction(e -> saveTermine());
        menuBar.getMenus().get(0).getItems().addAll(laden, speichern);
        MenuItem bearbeiten = new MenuItem("Bearbeiten");
        bearbeiten.setOnAction(e -> editKontakte());
        menuBar.getMenus().get(1).getItems().add(bearbeiten);


    }


    private void configureList() {


       // date.setValue(getSelectedDate());
        terminliste.setItems(data);


        ObservableList<Termin> s = terminliste.getSelectionModel().getSelectedItems();
        s.addListener((ListChangeListener.Change<? extends Termin> c) -> editTermin());


    }

    private void saveTermine() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(titel.getScene().getWindow());
        try {
            planer.save(file);
        } catch(IOException ex) {
            ViewHelper.showError("Datei konnte nicht gespeichert werden.");
            System.out.println(ex);
        }


    }

    public Adressbuch getAdressbuch() {
        return adressen;
    }

    public LocalDate getSelectedDate() {
        return date.getValue();
    }

    private void editTermin() {
        Termin t = terminliste.getSelectionModel().getSelectedItem();
        if(t == null) return;
        PlanerViewController view = null;
        if(planer.updateErlaubt(t))
            view = this;
        Initializable controller = new TerminViewController(t, view);
        URL location = getClass().getResource("/terminplaner/terminView.fxml");
        ViewHelper.showView(controller, location);

    }

    public void processTermin(Termin t) throws TerminUeberschneidungException {

        planer.setTermin(t);
        showTermine();
    }


    private void loadTermine() {

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(titel.getScene().getWindow());
        try {
            planer.load(file);
        } catch(IOException ex) {
            ViewHelper.showError("Datei konnte nicht geladen werden.");
            System.out.println(ex);
        }
    }

    private void editKontakte() {

        showAdresessView();

    }

    public void showAdresessView() {
        URL location = new ViewHelper().getClass().getResource("../adressbuch/adressbuchView.fxml");

        Initializable controller = new AdressbuchViewController(adressen);
        ViewHelper.showView(controller, location);
    }


}
