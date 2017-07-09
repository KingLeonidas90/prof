package adressbuch;

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

    public AdressbuchViewController(Adressbuch adressbuch){
        this.adressbuch = adressbuch;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // neues Adressbuch Objekt initialisieren

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
        // Erzeugen einer Instanz von ObservableList<Kontakt>
        tableContent = FXCollections.observableArrayList();
        searchField.textProperty().addListener(e -> filterList());
        configureTable();
    }

    private void configureTable() {
        tableView.setEditable(true);

        // Festlegen, mit welchem Attributwert der Tabelle die Spalte gekoppelt werden soll, in diesem Fall mit "Name"
        name.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("Name"));
        // Der Zelle einer Spalte ein Texteingabefeld zuweisen, dieses wird bei einem Doppelklick auf die Zelle angezeigt
        name.setCellFactory(TextFieldTableCell.<Kontakt>forTableColumn());
        //EventListener, der bei Eingabe die Methode updateName aufruft
        name.setOnEditCommit(event -> updateName(event));

        // Festlegen, mit welchem Attributwert der Tabelle die Spalte gekoppelt werden soll, in diesem Fall mit "Telefon"
        phone.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("Telefon"));
        // Der Zelle einer Spalte ein Texteingabefeld zuweisen, dieses wird bei einem Doppelklick auf die Zelle angezeigt
        phone.setCellFactory(TextFieldTableCell.<Kontakt>forTableColumn());
        phone.setOnEditCommit(event -> updatePhone(event));

        // Festlegen, mit welchem Attributwert der Tabelle die Spalte gekoppelt werden soll, in diesem Fall mit "Email"
        email.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("Email"));
        // Der Zelle einer Spalte ein Texteingabefeld zuweisen, dieses wird bei einem Doppelklick auf die Zelle angezeigt
        email.setCellFactory(TextFieldTableCell.<Kontakt>forTableColumn());
        email.setOnEditCommit(event -> updateEmail(event));


        showKontakte(adressbuch.getAlleKontakte());
        tableView.setItems(tableContent);


        //EventListener für den addButton erstellt
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
                    ViewHelper.showError(e.toString());
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
    private void filterList() {

        String tf = searchField.getText();
        Kontakt [] kontakte = adressbuch.getKontakte(tf);
        showKontakte(kontakte);


    }


    private void updateName(TableColumn.CellEditEvent<Kontakt, String> event) {

        String alt = event.getOldValue();
        String neu = event.getNewValue();

        if (alt.equals(neu)) return;

        // Zugriff auf den Index der editierten Zeile
        int index = event.getTablePosition().getRow();

        // Zugriff auf das dazugehörige Kontakt-Objekt in der Tabelle
        Kontakt alterKontakt = tableView.getItems().get(index);


        // Aktualisiersten Kontakt in der Datenbank einpflegen
        Kontakt aktualisierterKontakt = new Kontakt(neu, alterKontakt.getTelefon(), alterKontakt.getEmail());


        try {
            adressbuch.updateKontakt(checkOnWhichKeyIsNeeded(alterKontakt), aktualisierterKontakt);
        } catch (KeinPassenderKontaktException ex) {
            ex.printStackTrace();
            ViewHelper.showError(ex.toString());
        } catch (UngueltigerSchluesselException ex) {
            ex.printStackTrace();
            ViewHelper.showError(ex.toString());
        }

        // Neues Laden der TableView, damit der geänderte Name in der richtigen Reihenfolge angezeigt wird
        showKontakte(adressbuch.getAlleKontakte());


        //Testausgabe
        try {
            System.out.print("neuer Kontakt eingetragen:  ");

            System.out.println(adressbuch.getKontakt(aktualisierterKontakt.getName()));

        } catch (UngueltigerSchluesselException e) {
            e.printStackTrace();
            ViewHelper.showError(e.toString());
        }
    }

    private void updatePhone(TableColumn.CellEditEvent<Kontakt, String> event) {
        String alt = event.getOldValue();
        String neu = event.getNewValue();
        if (alt.equals(neu)) return;
        int index = event.getTablePosition().getRow();
        Kontakt alterKontakt = tableView.getItems().get(index);
        Kontakt aktualisierterKontakt = new Kontakt(alterKontakt.getName(), neu, alterKontakt.getEmail());

        try {
            adressbuch.updateKontakt(checkOnWhichKeyIsNeeded(alterKontakt), aktualisierterKontakt);
        } catch (KeinPassenderKontaktException ex) {
            ViewHelper.showError(ex.toString());
            ex.printStackTrace();
        } catch (UngueltigerSchluesselException ex) {
            ViewHelper.showError(ex.toString());
            ex.printStackTrace();
        }
        showKontakte(adressbuch.getAlleKontakte());

        //Testausgabe
        try {
            System.out.print("neue Telefonnummer eingetragen:  ");
            System.out.println(adressbuch.getKontakt(aktualisierterKontakt.getTelefon()));
        } catch (UngueltigerSchluesselException e) {
            e.printStackTrace();
            ViewHelper.showError(e.toString());
        }
    }

    private void updateEmail(TableColumn.CellEditEvent<Kontakt, String> event) {
        String alt = event.getOldValue();
        String neu = event.getNewValue();
        if (alt.equals(neu)) return;
        int index = event.getTablePosition().getRow();
        Kontakt alterKontakt = tableView.getItems().get(index);

        Kontakt aktualisierterKontakt = new Kontakt(alterKontakt.getName(), alterKontakt.getTelefon(),  neu);

        try {
            adressbuch.updateKontakt(checkOnWhichKeyIsNeeded(alterKontakt), aktualisierterKontakt);
        } catch (KeinPassenderKontaktException ex) {
            ex.printStackTrace();
            ViewHelper.showError(ex.toString());
        } catch (UngueltigerSchluesselException ex) {
            ex.printStackTrace();
            ViewHelper.showError(ex.toString());
        }

        showKontakte(adressbuch.getAlleKontakte());

        //Testausgabe
        try {
            System.out.print("neue Emailadresse eingetragen:  ");
            System.out.println(adressbuch.getKontakt(aktualisierterKontakt.getEmail()));
        } catch (UngueltigerSchluesselException e) {
            ViewHelper.showError(e.toString());
            e.printStackTrace();
        }


    }

    private String checkOnWhichKeyIsNeeded(Kontakt kontakt) {

        //Wenn der Kontakt keinen Namen hat, wird die Telefonnummer als Schluessel zurueckgegeben
        if (kontakt.getName().trim().length() != 0) {
            return kontakt.getName();
        }
        return kontakt.getTelefon();
    }




}