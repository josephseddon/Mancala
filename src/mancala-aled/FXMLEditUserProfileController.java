/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mancala;

import java.io.File;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException; 
import java.util.ResourceBundle;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;
import javafx.scene.image.*;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * FXML Controller class
 *
 * @author Aled Walters
 */
public class FXMLEditUserProfileController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML private TextField usernameTextBox;
    @FXML private TextField firstnameTextBox;
    @FXML private TextField lastnameTextBox;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private MenuButton pictureMenu;
    private String currentUser;
    private String username;
    private String firstname;
    private String lastname;
    
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
            currentUser = rscurrent.getString("CurrentUsers");
            ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE username =  '" + currentUser + "'");
            
            username = rs.getString("username");
            firstname = rs.getString("firstname");
            lastname = rs.getString("lname");
            rs.close();
            rscurrent.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        usernameTextBox.setText(username);   
        firstnameTextBox.setText(firstname);
        lastnameTextBox.setText(lastname);
    }
    
    @FXML
    public void cancelButtonClick(ActionEvent event) throws IOException {
        System.out.println("Back button clicked");
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Cancel any changes?");
        alert.setContentText("Are you sure you want to cancel? Any changes will not be saved.");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK){
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLOwnUserProfile.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
        } else {
        }
    }
    @FXML
    public void saveButtonClick(ActionEvent event) throws IOException, SQLException {
        System.out.println("Save button clicked");
        /*Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Save");
        alert.setHeaderText("Save any changes?");
        alert.setContentText("Are you sure you want to make these changes?");
        Optional<ButtonType> option = alert.showAndWait();
         */   saveChanges(currentUser, username, firstname, lastname);
            Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLOwnUserProfile.fxml"));
            Scene home_page_scene = new Scene(home_page_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(home_page_scene);
            app_stage.show();
       /* if (option.get() == ButtonType.OK){
            saveChanges(currentUser, username, firstname, lastname);
            Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLOwnUserProfile.fxml"));
            Scene home_page_scene = new Scene(home_page_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(home_page_scene);
            app_stage.show();
        } else {
        }*/
    }
    
    @FXML
    public void saveChanges(String currentUser, String uname, String fname, String lname) throws SQLException {
        try {
            Connection c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
            c.setAutoCommit(false);
            
            PreparedStatement pstmt1 = c.prepareStatement("SELECT * FROM User WHERE username= ?");
            PreparedStatement pstmt2 = c.prepareStatement("UPDATE User SET username = ?" + ", firstname = ?," + " lname = ?" + " WHERE username =  ?");
            
            pstmt1.setString(1, currentUser);
            ResultSet rscheck = pstmt1.executeQuery();
            pstmt2.setString(1, uname);
            pstmt2.setString(2, fname);
            pstmt2.setString(3, lname);
            pstmt2.setString(4, currentUser);
            pstmt2.executeUpdate();
            
            rscheck.close();
            c.commit();
            c.close();
                   
        
        
        
        
        
       /* 
        Statement stmt1;
        Statement stmt2;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
            c.setAutoCommit(false);            
            System.out.println("Opened database successfully");
            stmt1 = c.createStatement();     
            stmt2 = c.createStatement();  
            ResultSet rscheck = stmt1.executeQuery("SELECT * FROM User WHERE username= '" + uname + "'"); //Comment ouit?
            if (rscheck.getString("username").equals(uname)){
                Alert alert = new Alert(AlertType.ERROR);    
                alert.setTitle("Error");
                alert.setHeaderText("Invalid username");
                alert.setContentText("The input username already exists."); 
            } else if (uname.equals("")){                                               //Need to create validity checks
                Alert alert = new Alert(AlertType.ERROR);    
                alert.setTitle("Error");
                alert.setHeaderText("Invalid username");
                alert.setContentText("The input username cannot be empty.");          
           // } else {            
            
            //Change to prepared statement
            stmt2.executeUpdate("UPDATE User SET username = '" + uname + "', firstname = '" + fname + "', lname = '" + lname + "' WHERE username =  '" + currentUser + "'");
            stmt1.close();
            stmt2.close();
            rscheck.close();
            c.commit();
            c.close();*/
            
        } catch (NoResultException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
         
    }
}

