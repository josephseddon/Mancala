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
public class FXMLOwnUserStatsController implements Initializable {

    @FXML
    private Label wins;
    @FXML
    private Label losses;
    @FXML 
    private Label draws;
    @FXML 
    private Label ratio;
    
    
    /**
     * Initializes the controller class.
     * Sets user data to labels
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
            
            wins.setText(rs.getString("wins"));
            losses.setText(rs.getString("losses"));
            draws.setText(rs.getString("draws"));
            ratio.setText(rs.getString("ratio"));
            
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
}