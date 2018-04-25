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
    private Label username;
    @FXML
    private Label firstName;
    @FXML 
    private Label lastName;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection c;
        Statement stmt;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
            c.setAutoCommit(false);
            
            System.out.println("Opened database successfully");
            stmt = c.createStatement();            
            ResultSet rscurrent = stmt.executeQuery("SELECT * FROM CurrentUser WHERE PlayerNo= '1'");
            ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE username =  '" + rscurrent.getString("CurrentUsers") + "'");
            
            username.setText(rs.getString("username"));
            firstName.setText(rs.getString("firstname"));
            lastName.setText(rs.getString("lname"));
            
            File imageFile = new File("src//mancala//"+rs.getString("profileimage"));
            Image profileImage = new Image(imageFile.toURI().toString());
            profilePicture.setImage(profileImage);
            System.out.println(rs.getString("profileimage"));
            
            rs.close();
            rscurrent.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }     
    
}
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
    public void editButtonClick(ActionEvent event) throws IOException {
         System.out.println("Edit Profile button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLEditUserProfile.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    }   
    @FXML
    public void pictureButtonClick(ActionEvent event) throws IOException {
         System.out.println("ChangePicture button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLPictureSelection.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    } 
}