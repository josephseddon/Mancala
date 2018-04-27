/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mancala;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author josephseddpm
 */
public class FXMLCreateUserController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Label usertaken_Label;
    
    @FXML
    public TextField createuser_username_box;
    
    @FXML
    public TextField createuser_firstname_box;
    
    @FXML
    public TextField createuser_lastname_box;
    
    @FXML
    private Button createuser_Button;
    
    public void createuserButtonAction(ActionEvent event) throws IOException {
        System.out.println("Create User button clicked");
        FXMLPictureSelectionController.create = true;
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLPictureSelection.fxml"));
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
           if (usertaken())
            {
               usertaken_Label.setText("Sorry, that username is taken. Please choose another");
            }
           else
           {
               createuser();
               FXMLPictureSelectionController.username = createuser_username_box.getText();
               app_stage.setScene(home_page_scene);
               app_stage.show();
           }
    }
    
    public void createuser()
    {
        System.out.println("INSERT INTO User (username, firstname, lname) VALUES ('" + createuser_username_box.getText() + "', '" + createuser_firstname_box.getText() + "', '" + createuser_lastname_box.getText() + "')");
      
        Connection c;
        Statement stmt;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
            c.setAutoCommit(false);
            
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            
            stmt.executeUpdate("INSERT INTO User (username, firstname, lname) VALUES ('" + createuser_username_box.getText() + "','" + createuser_firstname_box.getText() + "','" + createuser_lastname_box.getText() + "')");
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }
    
    private boolean usertaken()
    {
        boolean user_taken = false;
        System.out.println("SELECT * FROM User WHERE username= " + "'" + createuser_username_box.getText() + "'");
        
        Connection c;
        Statement stmt;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
            c.setAutoCommit(false);
            
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE username= " + "'" + createuser_username_box.getText() + "'");
            
                while (rs.next() )  {
                    if (rs.getString("username") != null) {
                        String username = rs.getString("username");
                        System.out.println("username = " + username );
                        user_taken = true;
                    }
                }
           
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return user_taken;
    }
    public void cancelButtonAction(ActionEvent event) throws IOException{
    	System.out.println("Cancel button clicked");
    	Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
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