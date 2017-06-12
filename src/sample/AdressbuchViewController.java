package sample;

import Sourcen.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;


import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ResourceBundle;

import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;

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

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private Button addButton;

    @FXML
    private TextField phoneField;

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
        tableView.setEditable(true);


        name.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("Name"));
        name.setCellFactory(TextFieldTableCell.<Kontakt>forTableColumn());
        name.setOnEditCommit(event -> updateName(event));


        phone.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("Telefon"));
        phone.setCellFactory(TextFieldTableCell.<Kontakt>forTableColumn());
        phone.setOnEditCommit(event -> updatePhone(event));


        email.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("Email"));
        email.setCellFactory(TextFieldTableCell.<Kontakt>forTableColumn());
        email.setOnEditCommit(event -> updateEmail(event));


        showKontakte(adressbuch.getAlleKontakte());
        tableView.setItems(tableContent);



        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


              String eingabeName =  nameField.getText();
              String eingabeTelefon =  phoneField.getText();
              String eingabeEmail = emailField.getText();

              if(eingabeName.trim().length() == 0 && eingabeTelefon.trim().length() == 0 && eingabeEmail.trim().length() == 0 ||eingabeName.trim().length() == 0 && eingabeTelefon.trim().length() == 0 ) {
                  return;
              }

              Kontakt neuerKontakt = new Kontakt(eingabeName,eingabeTelefon,eingabeEmail);
                try {
                    adressbuch.addKontakt(neuerKontakt);
                } catch (DoppelterSchluesselException e) {
                    e.printStackTrace();
                }
                tableView.setItems(tableContent);
              nameField.clear();
              phoneField.clear();
              emailField.clear();
              showKontakte(adressbuch.getAlleKontakte());

            }
        });
    }

    // Methode um die TextArea mit allen Kontakten zu füllen
    private void showKontakte(Kontakt [] kontakte) {
        tableContent.clear();
        //textArea.setEditable(false);
        //kontakte = adressbuch.getAlleKontakte();
        tableContent.addAll(kontakte);
    }

    // Methode um eingebene Präfixe im Textfeld in der TextArea darzustellen
    private void filterList(String query) throws UngueltigerSchluesselException {

        Kontakt [] kontakte = adressbuch.getKontakte(query);
        showKontakte(kontakte);


    }


    private void updateName(TableColumn.CellEditEvent<Kontakt, String> event) {

        String alt = event.getOldValue();
        String neu = event.getNewValue();

        if (alt.equals(neu)) return;

        int index = event.getTablePosition().getRow();

        Kontakt k = tableView.getItems().get(index);
        k.setName(neu);

        // Aktualisiersten Kontakt in der Datenbank einpflegen
        Kontakt aktualisierterKontakt = new Kontakt(k.getName(), k.getTelefon(), k.getEmail());


        try {
            adressbuch.updateKontakt(alt, aktualisierterKontakt);
        } catch (KeinPassenderKontaktException ex) {
            ex.printStackTrace();
        } catch (UngueltigerSchluesselException ex) {
            ex.printStackTrace();
        }

        showKontakte(adressbuch.getAlleKontakte());

        try {
            System.out.println(adressbuch.getKontakt(alt));
        } catch (UngueltigerSchluesselException e) {
            e.printStackTrace();
        }

    }

    private void updatePhone(TableColumn.CellEditEvent<Kontakt, String> event) {
        String alt = event.getOldValue();
        String neu = event.getNewValue();
        if (alt.equals(neu)) return;
        int index = event.getTablePosition().getRow();
        Kontakt k = tableView.getItems().get(index);
        k.setTelefon(neu); }

    private void updateEmail(TableColumn.CellEditEvent<Kontakt, String> event) {
        String alt = event.getOldValue();
        String neu = event.getNewValue();
        if (alt.equals(neu)) return;
        int index = event.getTablePosition().getRow();
        Kontakt k = tableView.getItems().get(index);
        k.setEmail(neu);


    }




}