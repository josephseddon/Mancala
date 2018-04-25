/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mancala;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Neo
 */
public class FXMLHomePageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML 
    private Button startnewgameButton;
    
    @FXML 
    private Button startoldgameButton;
    
    @FXML 
    private Button leaderboardButton;
    
    @FXML 
    public void startnewgameButtonAction(ActionEvent event) throws IOException {
         System.out.println("Start new game button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("BoardView.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    }
    
    public void startoldgameButtonAction(ActionEvent event) throws IOException {
         System.out.println("Start old game button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("BoardViewOld.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    }
    
    public void leaderboardButtonAction(ActionEvent event) throws IOException {
         System.out.println("Opening Leaderboard");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLLeaderboard.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
