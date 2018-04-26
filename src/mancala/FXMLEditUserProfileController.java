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
 * A menu for changing the details of a user's profile
 * @author Aled Walters
 */
public class FXMLEditUserProfileController implements Initializable {

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
            stmt = c.createStatement();            
            ResultSet rscurrent = stmt.executeQuery("SELECT * FROM CurrentUser WHERE activeuserid= '1'");
            currentUser = rscurrent.getString("CurrentUsers");
            ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE username =  '" + currentUser + "'");
            usernameTextBox.setText(rs.getString("username"));   
            firstnameTextBox.setText(rs.getString("firstname"));
            lastnameTextBox.setText(rs.getString("lname"));            
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
        username = usernameTextBox.getText();   
        firstname = firstnameTextBox.getText();
        lastname = lastnameTextBox.getText();
        System.out.println("Save button clicked");
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Save");
        alert.setHeaderText("Save any changes?");
        alert.setContentText("Are you sure you want to make these changes?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK){
            //if (checkValid(username)){
                saveChanges(currentUser, username, firstname, lastname);
                Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLOwnUserProfile.fxml"));
                Scene home_page_scene = new Scene(home_page_parent);
                Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                app_stage.setScene(home_page_scene);
                app_stage.show();
            //}
        } else {
        }
    }
    
    @FXML
    public void saveChanges(String currentUser, String uname, String fname, String lname) throws SQLException {
        try {
            Connection c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
            c.setAutoCommit(false);                        
            PreparedStatement pstmt = c.prepareStatement("UPDATE User SET username = ?" + ", firstname = ?" + ", lname = ?" + " WHERE username =  ?");
            pstmt.setString(1, uname);
            pstmt.setString(2, fname);
            pstmt.setString(3, lname);            
            pstmt.setString(4, currentUser);            
            pstmt.executeUpdate();
            pstmt.close();
            Statement stmt = c.createStatement();
            stmt.executeUpdate("UPDATE CurrentUser SET CurrentUsers = '" + username + "' WHERE activeuserid = '1'");
            stmt.close();
            c.commit();
            c.close();
         } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
            System.out.println("SQLException");
        }
    }
    /*@FXML
    public boolean checkValid(String uname){
        try {            
            Statement stmt;
            Connection c = DriverManager.getConnection("jdbc:sqlite:mancala.db");  
            c.setAutoCommit(false);          
            System.out.println("Opened database successfully");
            stmt = c.createStatement();    
            ResultSet rscount = stmt.executeQuery("SELECT Count (*) AS 'total' FROM User WHERE username = '" + uname + "'");
            if (rscount.getInt("total")> 0){
                Alert alert = new Alert(AlertType.ERROR);    
                alert.setTitle("Error");
                alert.setHeaderText("Invalid username");
                alert.setContentText("This username already exists."); 
            } else if (uname.equals("")){                                           
                Alert alert = new Alert(AlertType.ERROR);    
                alert.setTitle("Error");
                alert.setHeaderText("Invalid username");
                alert.setContentText("Your username cannot be empty.");          
            } else {
                rscount.close();
                stmt.close();
                c.commit();
                c.close();
                return false;
            }
                rscount.close();
                stmt.close();
                c.commit();
                c.close();
                System.out.println("Query Passed");
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
            System.out.println("SQLException");
        }
        return true;         
    }*/
}

