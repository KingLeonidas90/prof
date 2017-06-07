package sample;

import Sourcen.Adressbuch;
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
    private TableColumn<Kontakt, String> phone;

    @FXML
    private TableColumn<Kontakt, String> name;

    @FXML
    private TableColumn<Kontakt, String> email;

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
        tableContent = FXCollections.observableArrayList();
        configureTable();
    }

    private void configureTable() {
        name.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("name"));
        phone.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("phone"));
        email.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("email"));
        Kontakt[] alleKontkte =  showKontakte(adressbuch.getAlleKontakte());
        tableContent.addAll(alleKontkte);
        tableView.setItems(tableContent);
    }

    // Methode um die TextArea mit allen Kontakten zu füllen
    private Kontakt[] showKontakte(Kontakt [] kontakte) {
        //textArea.setEditable(false);
        //kontakte = adressbuch.getAlleKontakte();
        for( int i = 0; i<kontakte.length; i++ )
        { textArea.appendText(kontakte[i] + " \n" + "\n"); }
        return kontakte;
    }

    // Methode um eingebene Präfixe im Textfeld in der TextArea darzustellen
    private void filterList(String query) throws UngueltigerSchluesselException {

        Kontakt [] kontakte = adressbuch.getKontakte(query);
        showKontakte(kontakte);


    }



}