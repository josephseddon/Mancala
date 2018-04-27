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

public class FXMLFavoriteUserController implements Initializable{

	@FXML private TableView tbView;
	@FXML private TableColumn username;
	@FXML private TableColumn firstname;
	@FXML private TableColumn lname;
	@FXML private TableColumn wins;
	@FXML private TableColumn losses;
	@FXML private TableColumn operation;
        
        @FXML
    public void backButtonClick(ActionEvent event) throws IOException {
         System.out.println("BacktoMenu button clicked");
         Parent home_page_parent = FXMLLoader.load(getClass().getResource("FXMLOwnUserProfile.fxml"));
         Scene home_page_scene = new Scene(home_page_parent);
         Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         app_stage.setScene(home_page_scene);
         app_stage.show();
    }   
        
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		showList();
	}

	private void showList() {
		// TODO Auto-generated method stub
		String currentUsername = getCurrentUsername();
		String sql = "select u.username, u.firstname, u.lname, u.wins, u.losses from"+
				" FavoriteUser fu left join User u  on fu.favorite_username = u. username where "+
				"fu.username = '"+currentUsername+"'";
		username.setCellValueFactory(new PropertyValueFactory("username"));          
		firstname.setCellValueFactory(new PropertyValueFactory("firstname"));  
		lname.setCellValueFactory(new PropertyValueFactory("lname"));          
		wins.setCellValueFactory(new PropertyValueFactory("wins"));
		losses.setCellValueFactory(new PropertyValueFactory("losses"));
		operation.setCellFactory(new Callback<TableColumn, TableCell>() {
	        @Override
	        public TableCell call(TableColumn param) {
	            TableCell cell = new TableCell<FavoriteUser,String>() {
	                @Override
	                public void updateItem(String item, boolean empty) {
	                    super.updateItem(item, empty);
	                    this.setText(null);
	                    this.setGraphic(null);
	                    if (!empty) {
	                        Button unmarkBtn = new Button("unmark");
	                        this.setGraphic(unmarkBtn);
	                        unmarkBtn.setOnMouseClicked((me) -> {
	                            String unmarkUsername = this.getTableView().getItems().
	                            		get(this.getIndex()).getUsername();
	                            unmarkFavorite(unmarkUsername);
	                            showList();
	                        });
	                    }
	                }
	            };
	            return cell;
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
                rowData.setFirstname(rs.getString(2));
                rowData.setLname(rs.getString(3));   
                rowData.setWins(rs.getString(4));
                rowData.setLosses(rs.getString(5));
                list.add(rowData);  
			}
			ps.close();
			c.close();
			tbView.setItems(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void markFavorite(String markUsername) {
		String currentUsername = getCurrentUsername();
		String sql = "insert into FavoriteUser values("+
				" (?,?)";
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
	
}
