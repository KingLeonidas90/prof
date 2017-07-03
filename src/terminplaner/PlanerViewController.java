package terminplaner;

import Sourcen.UngueltigerSchluesselException;
import Sourcen.ViewHelper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;

import Sourcen.Adressbuch;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import Sourcen.AdressbuchViewController;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    private Menu termineMenu;

    @FXML
    private Menu kontakteMenu;

    @FXML
    private ListView<Termin> terminliste;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Erzeugen einer Instanz von ObservableList<Termin>
        data = FXCollections.observableArrayList();

        // Adressbuch initialisiert
        adressen = new Adressbuch();

        // Termin erstellt für den Kontakt John aus dem Adressbuch
        try {
            planer = new Terminplaner(adressen.getKontakt("john"));
        } catch (UngueltigerSchluesselException e) {
            e.printStackTrace();
        }


        // Datepicker mit dem aktuellen Datum eingestellt und einen Event Handler initialisiert
        date.setValue(LocalDate.now());
        date.setOnAction((e) -> showTermine());

        // AddButton mit Event Handler initialisiert
        addButton.setOnAction(event -> addTermin());

        //angepasste MenuBar laden
        configureMenu();
        configureList();
        //saveTermine();
    }

    private void addTermin() {
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
        termineMenu.getItems().add(new MenuItem("Speichern"));
        termineMenu.getItems().add(new MenuItem("Laden"));
        kontakteMenu.getItems().add(new MenuItem("Bearbeiten"));

        // Event Handler um termine zu speichern
        termineMenu.setOnAction(event -> loadTermine());

        // Event Handler um Termine zu laden
        termineMenu.setOnAction(event -> loadTermine());

        // Event Handler um Kontakte zu bearbeiten
        kontakteMenu.setOnAction(event -> editKontakte());
    }

    private void configureList() {

        /* Termin termineDave = (Termin) planer.getTermineTag(LocalDate.of(2014,10,24));
        data.add(termineDave);
        terminliste.setItems(data);
        System.out.println(planer);*/

        date.setValue(getSelectedDate());
        terminliste.setItems(data);

        ObservableList<Termin> s = terminliste.getSelectionModel().getSelectedItems();
        s.addListener((ListChangeListener.Change<? extends Termin> c) -> editTermin());
    }

    private void saveTermine(){

        //FileChooser erstellen
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Speicherort wählen");
        //Auswgewählten Ordner in Variable speichern
        File file = fileChooser.showSaveDialog(new Stage());

        //FileOutputstream initialisieren
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objOutputStream = new ObjectOutputStream(fileOutputStream);
            objOutputStream.writeObject(planer);
            objOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ViewHelper.showError(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            ViewHelper.showError("Datei konnte nicht gespeichert werden");
        }

        //Termine des Planers in ausgewählten Ordner abspeichern  <-- auf IOException prüfen
    }

    private void loadTermine() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Datei auswählen");
        File file = fileChooser.showOpenDialog(new Stage());

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Terminplaner planer = (Terminplaner) objectInputStream.readObject();
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            ViewHelper.showError("Datei konnte nicht geladen werden");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        showTermine();

    }

    public Adressbuch getAdressbuch() {
        return adressen;
    }

    public LocalDate getSelectedDate() {
        return date.getValue();
    }

    private void editTermin() {
    }


    private void editKontakte() {

        showAdresessView();
    }

    public  void showAdresessView() {
        URL location = new ViewHelper().getClass().getResource("../Sourcen/adressbuchView.fxml");

        Initializable controller = new AdressbuchViewController(adressen);
        ViewHelper.showView(controller, location);
    }
}