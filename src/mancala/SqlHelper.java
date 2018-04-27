package mancala;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SqlHelper {
	
	/**
	 * only execute sql whose parameters are type of String 
	 * @param sql
	 * @param list 
	 */
	public static void execute(String sql,List<String> list) {
		Connection c = null;
		try {
			c = getConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			for(int i = 0 ; i < list.size(); ++i) {
				ps.setString(i+1, list.get(i));
			}
			ps.execute();
			ps.close();
			c.commit();
			c.close();
		} catch (SQLException e) {
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
	public static Connection getConnection() throws SQLException {
		Connection c = null;
		c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
        c.setAutoCommit(false);
        return c;
	}
	public static void closeConnection(Connection c) {
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
