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
public class FXML2ndPlayerLoginController implements Initializable {

    @FXML
    private TextField username_box;
    
    @FXML
    private Label usererror_Label; 
    
    @FXML
    public void secondplayerloginButtonAction(ActionEvent event) throws IOException {
         System.out.println("Login button clicked");
          if (login())
             {
                setCurrentUser();
                Parent home_page_parent = FXMLLoader.load(getClass().getResource("BoardView.fxml"));
                Scene home_page_scene = new Scene(home_page_parent);
                Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                app_stage.setScene(home_page_scene);
                app_stage.show();
             }
            else
            {
                usererror_Label.setText("Sorry, this username is unavailable. Try again or play as guest");
            }
    }
    
    private boolean login()
    {
        boolean let_in = false;
        System.out.println("SELECT * FROM User WHERE username= " + "'" + username_box.getText() + "'");
        
        Connection c;
        Statement stmt1;
        Statement stmt2;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
            c.setAutoCommit(false);
            
            System.out.println("Opened database successfully");
            stmt1 = c.createStatement();
            stmt2 = c.createStatement();
            
            ResultSet rsUser = stmt1.executeQuery("SELECT * FROM User WHERE username= " + "'" + username_box.getText() + "'");
            ResultSet rsCurrent = stmt2.executeQuery("SELECT * FROM CurrentUser WHERE activeuserid = '1'");
            
                while (rsUser.next() )  {
                    if ((rsUser.getString("username") != null) && !username_box.getText().equals(rsCurrent.getString("CurrentUsers"))) {
                        String username = rsUser.getString("username");
                        System.out.println("username = " + username );
                        let_in = true;
                    }
                }
           
            stmt1.close();
            stmt2.close();
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
        System.out.println("INSERT INTO CurrentUser (activeuserid, CurrentUsers) VALUES ( 2 ,CurrentUsers) VALUES ('" + username_box.getText() + "')");
      
        Connection c;
        Statement stmt;
        
        try {
            c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            stmt.executeUpdate("INSERT INTO CurrentUser (activeuserid, CurrentUsers) VALUES ( 2 ,'" + username_box.getText() + "')");
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
    
    public void computergameButtonClick(ActionEvent event) throws IOException {
         System.out.println("Computer player button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("BoardView.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    }   
    
    public void guestlogin()
    {
        System.out.println("Guest player button clicked");
        System.out.println("INSERT INTO CurrentUser (activeuserid, CurrentUsers) VALUES ( 2 ,CurrentUsers) VALUES ('Guest')");
      
        Connection c;
        Statement stmt;
        
        try {
            c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            stmt.executeUpdate("INSERT INTO CurrentUser (activeuserid, CurrentUsers) VALUES ( 2 ,'Guest')");
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
    
    public void guestplayerButtonClick(ActionEvent event) throws IOException {
         guestlogin();
         System.out.println("Computer player button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("BoardView.fxml"));
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
