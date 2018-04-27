/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mancala;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.image.*;

/**
 * FXML Controller class
 *
 * @author Aled Walters
 */
public class FXMLPictureSelectionController implements Initializable {
    
    @FXML Label picTitleLabel;
    @FXML Button cancelButton;
    @FXML ImageView pic1Image;
    @FXML ImageView pic2Image;
    @FXML ImageView pic3Image;
    @FXML ImageView pic4Image;
    @FXML ImageView pic5Image;
    @FXML ImageView pic6Image;
    static boolean create = false;
    static String username;        
    
    /**
     * Initializes the controller class.
     * Sets user data to labels and imageview
     */
    public void initialize(URL url, ResourceBundle rb) {    
    }    
    
        /**
         * Takes user to Profile page or created profile page depending on 
         * state of create
         */
    @FXML
    public void cancelButtonAction(ActionEvent event) throws IOException {
        System.out.println("Cancel clicked");
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLUserCreated.fxml"));
        if (!create) {
            home_page_parent = FXMLLoader.load(getClass().getResource("FXMLOwnUserProfile.fxml"));           
        }
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }    
    
        /**
         * Saves image choice to the users profile in the database
         * @param num the number of the chosen image
         */
    @FXML
    public static void setImage(String num){
        Connection c;
        Statement stmt1;
        Statement stmt2;
        
        try {
            c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
            c.setAutoCommit(false);
            
            System.out.println("Opened database successfully");
            stmt1 = c.createStatement();     
            stmt2 = c.createStatement();   
            if (!create) {
                ResultSet rs = stmt1.executeQuery("SELECT * FROM CurrentUser WHERE activeuserid= '1'");
                System.out.println(rs.getString("CurrentUsers"));
                username = rs.getString("CurrentUsers");
                rs.close();
            }
            stmt2.executeUpdate("UPDATE User SET profileimage = 'pp"+num+".jpg' WHERE username =  '" + username + "'");
            stmt1.close();
            stmt2.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }     
        System.out.println("Method Called: selected image " + num); 
        
    }
    
        /**
         * Saves image 1 as the users profile picture
         * Takes user to Profile page or created profile page depending on 
         * state of create
         */
    @FXML protected void pic1Clicked (MouseEvent event) throws IOException {
        System.out.println("Image 1 Selected");
        setImage("1");
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLUserCreated.fxml"));
        if (!create) {
            home_page_parent = FXMLLoader.load(getClass().getResource("FXMLOwnUserProfile.fxml"));           
        }
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }
        /**
         * Saves image 2 as the users profile picture
         * Takes user to Profile page or created profile page depending on 
         * state of create
         */
    @FXML protected void pic2Clicked (MouseEvent event) throws IOException {
        System.out.println("Image 2 Selected");
        setImage("2");
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLUserCreated.fxml"));
        if (!create) {
            home_page_parent = FXMLLoader.load(getClass().getResource("FXMLOwnUserProfile.fxml"));           
        }
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }
        /**
         * Saves image 3 as the users profile picture
         * Takes user to Profile page or created profile page depending on 
         * state of create
         */
    @FXML protected void pic3Clicked (MouseEvent event) throws IOException {
        System.out.println("Image 3 Selected");
        setImage("3");
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLUserCreated.fxml"));
        if (!create) {
            home_page_parent = FXMLLoader.load(getClass().getResource("FXMLOwnUserProfile.fxml"));           
        }
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }
        /**
         * Saves image 4 as the users profile picture
         * Takes user to Profile page or created profile page depending on 
         * state of create
         */
    @FXML protected void pic4Clicked (MouseEvent event) throws IOException {
        System.out.println("Image 4 Selected");
        setImage("4");
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLUserCreated.fxml"));
        if (!create) {
            home_page_parent = FXMLLoader.load(getClass().getResource("FXMLOwnUserProfile.fxml"));           
        }
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }
        /**
         * Saves image 5 as the users profile picture
         * Takes user to Profile page or created profile page depending on 
         * state of create
         */
    @FXML protected void pic5Clicked (MouseEvent event) throws IOException {
        System.out.println("Image 5 Selected");
        setImage("5");
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLUserCreated.fxml"));
        if (!create) {
            home_page_parent = FXMLLoader.load(getClass().getResource("FXMLOwnUserProfile.fxml"));           
        }
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }
        /**
         * Saves image 6 as the users profile picture
         * Takes user to Profile page or created profile page depending on 
         * state of create
         */
    @FXML protected void pic6Clicked (MouseEvent event) throws IOException {
        System.out.println("Image 6 Selected");
        setImage("6");
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLUserCreated.fxml"));
        if (!create) {
            home_page_parent =  FXMLLoader.load(getClass().getResource("FXMLOwnUserProfile.fxml"));           
        }
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }
}
