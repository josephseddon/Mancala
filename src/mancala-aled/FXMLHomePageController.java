/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mancala;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
import javafx.scene.image.*;

/**
 * FXML Controller class
 *
 * @author josephseddpm
 */
public class FXMLHomePageController implements Initializable {

    /**
     * Initializes the controller class.
     */
   
    @FXML 
    private ImageView profilePicture;
    @FXML 
    private Button startgameButton;
    @FXML
    private Button profileButton;
    @FXML 
    private Button logoutButton;
    
    @FXML 
    public void startgameButtonAction(ActionEvent event) throws IOException {
         System.out.println("Start game button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("BoardView.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    }
    
    @FXML 
    public void profileButtonClick(ActionEvent event) throws IOException {
         System.out.println("ViewProfile button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLOwnUserProfile.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    }
    
    @FXML 
    public void logoutButtonClick(ActionEvent event) throws IOException {
         System.out.println("Logout button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLLoginPage.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        Connection c;
        Statement stmt1;
        Statement stmt2;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
            c.setAutoCommit(false);
            
            System.out.println("Opened database successfully");
            stmt1 = c.createStatement();     
            stmt2 = c.createStatement();   
            ResultSet rscurrent = stmt1.executeQuery("SELECT * FROM CurrentUser WHERE PlayerNo= '1'");
            System.out.println(rscurrent.getString("CurrentUsers"));
            ResultSet rs = stmt2.executeQuery("SELECT * FROM User WHERE username =  '" + rscurrent.getString("CurrentUsers") + "'");
            
            File imageFile = new File("src//mancala//"+rs.getString("profileimage"));
            Image profileImage = new Image(imageFile.toURI().toString());
            profilePicture.setImage(profileImage);
            System.out.println(rs.getString("profileimage"));
            
            rscurrent.close();
            rs.close();
            stmt1.close();
            stmt2.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }      
    }    
    
}
