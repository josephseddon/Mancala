/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mancala;

import java.sql.Statement;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author josephseddpm
 */
public class FXMLLoginPageController implements Initializable {
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button gotocreateuser_button;

    @FXML
    private TextField username_box;
    
    @FXML
    private Label usererror_Label; 
    
    @FXML
    public void loginButtonAction(ActionEvent event) throws IOException {
         System.out.println("Login button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLHomePage.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            if (login())
             {
                setCurrentUser();
                app_stage.setScene(home_page_scene);
                app_stage.show();
             }
            else
            {
                usererror_Label.setText("Sorry, user does not exist. Create a new user or try again");
            }
    }
    
    public void gotocreateuserButtonAction(ActionEvent event) throws IOException {
         System.out.println("Gotocreateuser button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLCreateUser.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    }
    
    private boolean login()
    {
        boolean let_in = false;
        System.out.println("SELECT * FROM User WHERE username= " + "'" + username_box.getText() + "'");
        
        Connection c;
        Statement stmt;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
            c.setAutoCommit(false);
            
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE username= " + "'" + username_box.getText() + "'");
            
                while (rs.next() )  {
                    if (rs.getString("username") != null) {
                        String username = rs.getString("username");
                        System.out.println("username = " + username );
                        let_in = true;
                    }
                }
           
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return let_in;
    }
    
    public void setCurrentUser()
    {
        System.out.println("INSERT INTO CurrentUser (CurrentUsers) VALUES ('" + username_box.getText() + "')");
      
        Connection c;
        Statement stmt;
        
        try {
            c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            stmt.executeUpdate("INSERT INTO CurrentUser (CurrentUsers) VALUES ('" + username_box.getText() + "')");
            stmt.close();
            c.commit();
            c.close();
        } 
        
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
