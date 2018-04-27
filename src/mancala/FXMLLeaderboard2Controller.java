/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mancala;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.javafx.scene.control.skin.TableCellSkin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author josephseddpm
 */
public class FXMLLeaderboard2Controller implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML private TableView<FavoriteUser> tbView;
    @FXML private TableColumn<FavoriteUser,String> username;
    @FXML private TableColumn<FavoriteUser,String> wins;
    @FXML private TableColumn<FavoriteUser,String> ratio;
    @FXML private TableColumn<FavoriteUser,String> operation;
    @FXML private Button mainmenuButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showList();
    }    
    
    private void showList() {
		// TODO Auto-generated method stub
		String sql = ("SELECT username, wins, ratio FROM User ORDER BY wins COLLATE NOCASE DESC");
		List<String> markedList = getMarkedList();
		username.setCellValueFactory(new PropertyValueFactory<FavoriteUser,String>("username"));                
		wins.setCellValueFactory(new PropertyValueFactory<>	("wins"));
        ratio.setCellValueFactory(new PropertyValueFactory<>("ratio"));
        username.setCellFactory((col)->new TableCell<FavoriteUser,String>(){
        	public void updateItem(String item, boolean empty) {
        		super.updateItem(item, empty);
        		if(!empty) {
        			if(markedList.contains(item)) this.setText(item+"  ♥");
        			else this.setText(item);
        		}
        	}
        });
		operation.setCellFactory((col)-> new TableCell<FavoriteUser,String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                    	String username = this.getTableView().getItems().
                        		get(this.getIndex()).getUsername();
                    	
                    	if(markedList.contains(username)) {
                    		Button unmarkBtn = new Button("unmark");
	                        this.setGraphic(unmarkBtn);
	                        unmarkBtn.setOnMouseClicked((me) -> {
	                            unmarkFavorite(username);
	                            showList();
	                        });
                    	}else {
                    		Button markBtn = new Button("mark");
	                        this.setGraphic(markBtn);
	                        markBtn.setOnMouseClicked((me) -> {
	                            markFavorite(username);
	                            showList();
	                        });
                    	} 
                    }
                }
		});
		ObservableList<FavoriteUser> list = FXCollections.observableArrayList();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = SqlHelper.getConnection();
			ps = c.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) { 
				FavoriteUser rowData = new FavoriteUser();
				rowData.setUsername(rs.getString(1)); 
                rowData.setWins(rs.getString(2));
                rowData.setRatio(rs.getString(3));
                list.add(rowData);  
			}
			ps.close();
			c.close();
			tbView.setItems(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			SqlHelper.closeConnection(c);
		}
	}
    
    public List<String> getMarkedList() {
    	String sql = "select favorite_username from FavoriteUser where username = ?";
    	String username = getCurrentUsername();
    	Connection c =null;
    	PreparedStatement ps = null;
    	List<String> markedList = new ArrayList<String>();
    	try {
			c =SqlHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				markedList.add(rs.getString(1));
			}
			ps.close();
			c.close();
		} catch (Exception e) {
			
		}finally {
			SqlHelper.closeConnection(c);
		}
    	return markedList;
    }
    public void markFavorite(String markUsername) {
		String currentUsername = getCurrentUsername();
		String sql = "insert into FavoriteUser values(?,?)";
		List<String> l = new ArrayList<String>();
		l.add(currentUsername);
		l.add(markUsername);
		SqlHelper.execute(sql,l);
	}
	
	public void unmarkFavorite(String unmarkUsername) {
		String currentUsername= getCurrentUsername();
		String sql = "delete from FavoriteUser where username"+
				" = ?"+
				" and favorite_username = ?";
		List<String> l = new ArrayList<String>();
		l.add(currentUsername);
		l.add(unmarkUsername);
		SqlHelper.execute(sql,l);
	}
	
	public String getCurrentUsername() {
		String sql = "select currentUsers from CurrentUser where activeuserid = 1";
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = SqlHelper.getConnection();
			ps = c.prepareStatement(sql);
			rs = ps.executeQuery();
			String currentUsername = rs.getString(1);
			c.close();
			return currentUsername;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
        
     public void mainmenuButtonClick(ActionEvent event) throws IOException {
         System.out.println("BacktoMenu button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLHomePage.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    }    
}
