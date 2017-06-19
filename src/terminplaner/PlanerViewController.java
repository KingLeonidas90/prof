package terminplaner;

import Sourcen.Kontakt;
import Sourcen.UngueltigerSchluesselException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;

import Sourcen.Adressbuch;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    private ListView<Kontakt> terminliste;

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


    }

    private void addTermin() {


    }

    private void showTermine() {

    }

    private void configureMenu() {
        termineMenu.getItems().add(new MenuItem("Speichern"));
        termineMenu.getItems().add(new MenuItem("Laden"));
        kontakteMenu.getItems().add(new MenuItem("Bearbeiten"));

        // Event Handler um termine zu speichern
        termineMenu.setOnAction(event -> saveTermine());

        // Event Handler um Termine zu laden
        termineMenu.setOnAction(event -> loadTermine());

        // Event Handler um Kontakte zu bearbeiten
        kontakteMenu.setOnAction(event -> editKontakte());


    }


    private void configureList() {

        planer.get
        System.out.println(planer);



    }

    private void saveTermine() {


    }

    private void loadTermine() {
    }

    private void editKontakte() {
    }


}
