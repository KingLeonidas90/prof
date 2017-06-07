package sample;

import Sourcen.Adressbuch;
import Sourcen.KeinPassenderKontaktException;
import Sourcen.Kontakt;
import Sourcen.UngueltigerSchluesselException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;


import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ResourceBundle;

public class AdressbuchViewController implements Initializable{

    private Adressbuch adressbuch;
    private Kontakt  kontakte;

    // ObservableList benachrichtigt die Tabelle automatisch, wenn die Liste geändert wurde.
    //Die Tabelle aktualisiert daraufhin ihre Anzeige.
    private ObservableList<Kontakt> tableContent;


    @FXML
    private TextField searchField;

    @FXML
    private TextArea textArea;

    @FXML
    private TableColumn<Kontakt, String> phoneColumn;

    @FXML
    private TableColumn<Kontakt, String> nameColumn;

    @FXML
    private TableColumn<Kontakt, String> emailColumn;

    @FXML
    private TableView<Kontakt> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // neues Adressbuch Objekt initialisieren
       adressbuch = new Adressbuch();
        //showKontakte(adressbuch.getAlleKontakte());
        /*searchField.setText("Hier wird ein neuer String eingetragen");
        String txt = searchField.getText();
        System.out.println(txt);*/
        /*searchField.setPromptText("Geben Sie hier Ihren Text ein");
        searchField.textProperty().addListener(event -> {
            try {
                textArea.clear();
                filterList(searchField.getText());

            } catch (UngueltigerSchluesselException e) {
                e.printStackTrace();
            }
        } );*/

        //ObservableList  initialisieren
        tableContent = FXCollections.observableArrayList();
        configureTable();
    }

    private void configureTable() {
        //Inhalt den entsprechenden Spalten zuweisen
        nameColumn.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("Name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("Telefon"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("Email"));

        //tabelle editierbar machen
        tableView.setEditable(true);
        //alle Zeilen in der Namensspalte editierbar machen
        nameColumn.setEditable(true);
        //allen Zeilen ein Texteingabefeld zuweisen
        nameColumn.setCellFactory(TextFieldTableCell.<Kontakt>forTableColumn());
        //eventhandler fuer das Beenden der Texteingabe
        nameColumn.setOnEditCommit((e)-> updatetName(e));

        //alle Zeilen in der Namensspalte editierbar machen
        phoneColumn.setEditable(true);
        //allen Zeilen ein Texteingabefeld zuweisen
        phoneColumn.setCellFactory(TextFieldTableCell.<Kontakt>forTableColumn());
        //eventhandler fuer das Beenden der Texteingabe
        phoneColumn.setOnEditCommit((e)-> updatetPhone(e));

        //alle Zeilen in der Namensspalte editierbar machen
        emailColumn.setEditable(true);
        //allen Zeilen ein Texteingabefeld zuweisen
        emailColumn.setCellFactory(TextFieldTableCell.<Kontakt>forTableColumn());
        //eventhandler fuer das Beenden der Texteingabe
        emailColumn.setOnEditCommit((e)-> updateEmail(e));


        //Alle Kontakte aus Adressbuch in die ObservableList hinzufügen
        showKontakte(adressbuch.getAlleKontakte());
        //ObservableList wird in der Tabelle als Items gesetzt
        tableView.setItems(tableContent);
    }

    // Methode um die TextArea mit allen Kontakten zu füllen
    private void showKontakte(Kontakt [] kontakte) {
        //Liste leeren
        tableContent.clear();
        //uebergebene Kontakte zu ObservableList hinzufügen
        tableContent.addAll(kontakte);
    }

    private void updatetName(TableColumn.CellEditEvent<Kontakt, String> event){

        String alterName= event.getOldValue();
        String neuerName = event.getNewValue();

        if (alterName.equals(neuerName)){
            return;
        }

        int rowIndex = event.getTablePosition().getRow();

        Kontakt k = tableView.getItems().get(rowIndex);
        k.setName(neuerName);

        //Kontakt in der Datenbank aktualisieren
        Kontakt aktualisierterKontakt = new Kontakt(k.getName(), k.getTelefon(), k.getEmail());
        //TODO: nicht mit altem Namen aufrufen, sondern alten Kontakt bekommen und Schluessel mit Methode selber wählen (name oder nummer)
        try {
            adressbuch.updateKontakt(alterName, aktualisierterKontakt);
        } catch (KeinPassenderKontaktException ex) {
            ex.printStackTrace();
        } catch (UngueltigerSchluesselException ex) {
            ex.printStackTrace();
        }

        showKontakte(adressbuch.getAlleKontakte());

        try {
            System.out.println(adressbuch.getKontakt(alterName));
        } catch (UngueltigerSchluesselException e) {
            e.printStackTrace();
        }
    }

    //TODO: Logik implementieren
    private void updatetPhone(TableColumn.CellEditEvent<Kontakt, String> event){

    }

    //TODO: Logik implementieren
    private void updateEmail(TableColumn.CellEditEvent<Kontakt, String> event){

    }

    private String checkOnWhichKey(Kontakt kontakt){
        if (kontakt.getName().trim().length() != 0){
            return kontakt.getName();
        }
        return kontakt.getTelefon();
    }

    // Methode um eingebene Präfixe im Textfeld in der TextArea darzustellen
    private void filterList(String query) throws UngueltigerSchluesselException {

        Kontakt [] kontakte = adressbuch.getKontakte(query);
        showKontakte(kontakte);
    }
}