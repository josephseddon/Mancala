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
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Aled Walters
 */
public class FXMLOwnUserProfileController implements Initializable {
    
    @FXML 
    private ImageView profilePicture;
    @FXML
    private Button editButton;
    @FXML
    private Button pictureButton;
    @FXML
    private Button favouritesButton;
    @FXML
    private Label username;
    @FXML
    private Label firstName;
    @FXML 
    private Label lastName;
    
    /**
     * Initializes the controller class.
     * Sets user data to labels and imageview
     */
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
            ResultSet rscurrent = stmt1.executeQuery("SELECT * FROM CurrentUser WHERE activeuserid= '1'");
            ResultSet rs = stmt2.executeQuery("SELECT * FROM User WHERE username =  '" + rscurrent.getString("CurrentUsers") + "'");
            
            username.setText(rs.getString("username"));
            firstName.setText(rs.getString("firstname"));
            lastName.setText(rs.getString("lname"));
            
            File imageFile = new File("src//mancala//"+rs.getString("profileimage"));
            Image profileImage = new Image(imageFile.toURI().toString());
            profilePicture.setImage(profileImage);
            System.out.println(rs.getString("profileimage"));
            
            rs.close();
            rscurrent.close();
            stmt1.close();
            stmt2.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }     
    
    }
    /**
     * Returns user to homepage
     */
    @FXML
    public void backButtonClick(ActionEvent event) throws IOException {
         System.out.println("BacktoMenu button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLHomePage.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    }   
    
    @FXML
    public void favouritesButtonClick(ActionEvent event) throws IOException {
         System.out.println("Favourite button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLFavoriteUser.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    } 
    
    /**
     * Takes user to edit profile page
     */
    @FXML
    public void editButtonClick(ActionEvent event) throws IOException {
         System.out.println("Edit Profile button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLEditUserProfile.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    }   
    
    /**
     * Takes user to picture select page
     */
    @FXML
    public void pictureButtonClick(ActionEvent event) throws IOException {
         System.out.println("ChangePicture button clicked");
         FXMLPictureSelectionController.create = false;
         FXMLPictureSelectionController.username = username.getText();
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLPictureSelection.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    } 
    public void statsButtonClick(ActionEvent event) throws IOException {
         System.out.println("ChangePicture button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLOwnUserStats.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    } 
}