/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mancala;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Leaderboard Controller class
 *
 * @author josephseddon
 */
public class FXMLLeaderboardController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private ListView users_listview;
    
    @FXML
    private MenuButton sort_userbutton;
    
    @FXML
    private MenuButton ascdesc_userbutton;
    
    String orderby;
    String ascdesc;
    ObservableList users;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshList();
  }    
     private void refreshList(){
         //Set items equal to an empty ArrayList
        users = FXCollections.observableArrayList ();
        
        //Select out of the DB, fill accordingly
        getUsers(users);
        
        //Set the listview to what we just populated with DB contents
        users_listview.setItems(users);
}
     private void getUsers(ObservableList users){
        Connection c;
        Statement stmt;
        
        orderby = sort_userbutton.getText();
        ascdesc = ascdesc_userbutton.getText();
        
        try {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
        c.setAutoCommit(false);
        System.out.println("Opened database successfully");
        
        if (orderby.equals("Wins")) { orderby = "wins";}
        else if (orderby.equals("Win/Loss Ratio")) { orderby = "ratio";}
        else if (orderby.equals("Username")) { orderby = "username";}
        else { orderby = "wins";}
        
        System.out.println("Query is: SELECT * FROM User" + " ORDER BY "  + orderby + " COLLATE NOCASE " + ascdesc);
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM User" + " ORDER BY " + orderby + " COLLATE NOCASE " + ascdesc);
        while ( rs.next() ) {
            String username = rs.getString("username");
            Integer wins = rs.getInt("wins");
            Double ratio  = rs.getDouble("ratio");
            System.out.println( "username = " + username );
            System.out.println( "wins = " + wins );
            System.out.println( "ratio = " + ratio);
            System.out.println();
            //IMPORTANT STATEMENT HERE:
            users.add(username + "\t" + wins + "\t" + ratio);
         }
        rs.close();
        stmt.close();
        c.close();
      
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    
       
    }
    
    @FXML
    private void sortUserButtonAction(ActionEvent event) throws IOException {
           MenuItem menu = (MenuItem) event.getSource();
           sort_userbutton.setText(menu.getText());
           refreshList();
    }
    @FXML
    private void ascdescUserButtonAction(ActionEvent event) throws IOException {
           MenuItem menu = (MenuItem) event.getSource();
           ascdesc_userbutton.setText(menu.getText());
           refreshList();
    }
}

