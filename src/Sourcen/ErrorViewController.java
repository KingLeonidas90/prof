/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Sourcen;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author beuth
 */
public class ErrorViewController implements Initializable {

    @FXML
    private Button errorButton;

    @FXML
    private Button viewButton;

    @FXML
    Label fehler;
    private String error;
    
    public ErrorViewController(String error) {
        this.error = error;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorButton.setOnAction(event -> showExampleError ());
        viewButton.setOnAction(event -> showExampleView ());
        fehler.setText(error);
    }

    private void showExampleError () {
            ViewHelper.showError("So kann man eine Fehlermeldung anzeigen!");
            }

    private void showExampleView() {
            Initializable controller = new InputViewController(this);
            URL location = getClass().getResource("inputView.fxml");
            ViewHelper.showView(controller, location);
            }
    
}
