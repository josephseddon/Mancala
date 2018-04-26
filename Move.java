package mancala;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javafx.event.ActionEvent;

/** 
 * This class is responsible for the game mechanics and is the controller class for BoardView.fxml
 * @author Jonathon Mateo
 *
 */


public class Move {
	private int[] boardArray = new int[14];
	private String player1ID;
	private String player2ID;
	private int winner = -1;
	private static int gameID = 0;
	private static int player = 0;
	private static int waitingPlayer = 2;
	private static int playerTurn = 1;
	private static boolean repeatTurn = false;
	private Label[] holeLabels;
	private boolean vsCPU = true;
	private int mancalaCount1 = 0;
	private int mancalaCount2 = 0;
	
	@FXML Label value0;
	@FXML Label value1;
	@FXML Label value2;
	@FXML Label value3;
	@FXML Label value4;
	@FXML Label value5;
	@FXML Label value6;
	@FXML Label value7;
	@FXML Label value8;
	@FXML Label value9;
	@FXML Label value10;
	@FXML Label value11;
	@FXML Label value12;
	@FXML Label value13;
	@FXML Label turnLabel;
	
	/**
	 * initialise the game, setting pit values and correct label text.
	 * @throws InterruptedException
	 */
	
	public void initialize() throws InterruptedException {
		System.out.println(vsCPU);
		for(int i = 0; i <= 13; ++i) {
				boardArray[i] = 4;
			}
		boardArray[6] = 0;
		boardArray[13] = 0;
		labelArray();
		setValues();
		player = playerStart();
		int loggedInUsers = getLoggedInUsers();
		System.out.println(loggedInUsers + " " + vsCPU);
		if (loggedInUsers == 2) {
			vsCPU = false;
		}
		setPlayerID();
		System.out.println(vsCPU);
		System.out.println(player1ID);//loggedInUsers + player1ID + player2ID);
		turnLabel.setText("Player " + Integer.toString(getPlayer()) + " select pit!");
		if (vsCPU == true && player == 2) {
			turnLabel.setText("CPU will act as Player 2. CPU's turn first: Click here for CPU to move.");
		}
		
	}
	
	public void setPlayerID() { 
		Connection c;
	    Statement stmt;
	        
	    try {
	    	c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
	        c.setAutoCommit(false);
	        System.out.println("Opened database successfully");
	        stmt = c.createStatement();
	        ResultSet player1 = stmt.executeQuery("SELECT CurrentUsers FROM CurrentUser WHERE activeuserid = 1");
	        while (player1.next()) {
	        	player1ID = player1.getString("CurrentUsers");
	        }
	        if (vsCPU == false) {
	        	ResultSet player2 = stmt.executeQuery("SELECT CurrentUsers FROM CurrentUser WHERE activeuserid = 2");
	        	while (player2.next()) {
	        	player2ID = player2.getString("CurrentUsers");
	        	}
	        }
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
	
	public int getLoggedInUsers() {
		int loggedInUsers = 1;
		Connection c;
	    Statement stmt;
	    
	    try {
	    	c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
	        c.setAutoCommit(false);
	        System.out.println("Opened database successfully");
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT Count (*) AS total FROM CurrentUser");
	        loggedInUsers = rs.getInt("total");
	        
	        stmt.close();
	        c.commit();
	        c.close();
	    } 
	        
	    catch (Exception e) {
	    	System.err.println(e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	    }
	    System.out.println("Operation done successfully");
	    return loggedInUsers;
    }
	
	/**
	 * chooses random player to start.
	 * @return random player number.
	 */
	public int playerStart() { 
		    Random random = new Random();
		    boolean isOne = random.nextBoolean();
		    if (isOne) {
		    	return 1;
		    }
		    else {
		    	incrPlayerTurn();
		    	waitingPlayer = 1;
		    	return 2;
		    }
		}

	/**
	 * Get the current active player.
	 * @return the current active player number.
	 */
	public int getPlayer() {
		return player;
	}
	
	/**
	 * Set the new active player.
	 * @param a the new active player number.
	 */
	public void setPlayer(int a) {
		player = a;
	}

	/**
	 * Get the current waiting player.
	 * @return the current waiting player number.
	 */
	public int getWaitingPlayer() {
		return waitingPlayer;
	}
	
	/**
	 * Set the new waiting player.
	 * @param a the new waiting player.
	 */
	public static void setWaitingPlayer(int a) {
		waitingPlayer = a;
	}


	/**
	 * Get the number of turns that have been made.
	 * @return the number of turns that have been made.
	 */
	public int getPlayerTurn() {
		return playerTurn;
	}
	
	/**
	 * Increment the player turn value.
	 */
	public void incrPlayerTurn() {
		++playerTurn;
	}

	/**
	 * Executes the move when player/cpu selects pit. Evaluates win condition and ensures valid selection of pit.
	 * @param boardArray
	 * @param indx
	 * @throws InterruptedException
	 */
	public void move(int[] boardArray, int indx) throws InterruptedException {
		int t = 0;
		if (hasWon() > 0){
			if (hasWon() == 1) {
				turnLabel.setText("Player 1 wins! Congratulations!");
				System.out.println("Player 1 wins!");
				updateUserHistory(hasWon());
				updateGameHistory(hasWon() , mancalaCount1, mancalaCount2);
			}
			if (hasWon() == 2) {
				turnLabel.setText("Player 2 wins! Congratulations!");
				System.out.println("Player 2 Wins!");
				updateUserHistory(hasWon());
				updateGameHistory(hasWon(), mancalaCount1, mancalaCount2);
			}
			if (hasWon() == 3) {
				turnLabel.setText("It's a tie!");
				System.out.println("Draw!");
				updateUserHistory(hasWon());
				updateGameHistory(hasWon(), mancalaCount1, mancalaCount2);
			}
		}
		if (hasWon() < 0) {
			int player =  getPlayer();
			int playerTurn =  getPlayerTurn();
			int waitingPlayer =  getWaitingPlayer();
			if (checkSelection(getBoardArray(), player, indx) == false) {
				if (vsCPU == true && player == 2) { 
					turnLabel.setText("Steady on there, Champ! Click here for CPU to move before you make your move!");
				}
				else {
				turnLabel.setText("Hey! You are Player " + player + ", please select a pit from your side. Remember, you cannot select a Mancala or an empty pit!");
				System.out.println("Error: You are player " + player + ", please select a pit from your side. You cannot select a Mancala.");
				}
			}
			if (checkTurn(player, playerTurn) == true && checkSelection( getBoardArray(), player, indx) == true){
				setBoardArray(redistributeStones(getBoardArray(), indx));
				for(int i = 0; i < 14; ++i) {
			      	System.out.println(i + ": " + boardArray[i]);
				}
			}
		}
	}
	
	public void updateUserHistory(int winner) { 
		double oneWins = 0;
		double oneLosses = 0;
		double oneDraws = 0;
		double twoWins = 0;
		double twoLosses = 0;
		double twoDraws = 0;
		if (winner == 1) {
			Connection c;
		    Statement stmt;
		        
		    try {
		    	c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
		        c.setAutoCommit(false);
		        System.out.println("Opened database successfully");
		        stmt = c.createStatement();
		        ResultSet rs1 =  stmt.executeQuery("SELECT wins FROM User WHERE username = '" + player1ID + "'");
		        while (rs1.next()) {
		        oneWins = rs1.getDouble("wins");
		        }
		        ++oneWins;
		        stmt.executeUpdate("UPDATE User SET wins = '"+ oneWins + "' WHERE username = '" + player1ID + "'");
		        ResultSet rs2 = stmt.executeQuery("SELECT losses FROM User WHERE username = '" + player1ID + "'");
		        while  (rs2.next()) {
		        oneLosses = rs2.getDouble("losses");
		        }
		        double oneRatio = oneWins / oneLosses;
		        stmt.executeUpdate("UPDATE User SET ratio = '" + oneRatio + "' WHERE username = '" + player1ID + "'");
		        if (vsCPU == false) {
		        	ResultSet rs3 = stmt.executeQuery("SELECT losses FROM User WHERE username = '" + player2ID + "'");
		        	while (rs3.next()) {
		        		twoLosses =  rs3.getDouble("losses");
		        	}
		        	++twoLosses;
		        	stmt.executeUpdate("UPDATE User SET losses = '"+ twoLosses + "' WHERE username = '" + player2ID + "'");
		        	ResultSet rs4 = stmt.executeQuery("SELECT wins FROM User WHERE username = '" + player2ID + "'");
		        	while (rs4.next()) {
		        		twoWins = rs4.getDouble("wins");
		        	}
		        	double twoRatio = twoWins / twoLosses;
		        	stmt.executeUpdate("UPDATE User SET ratio = '" + twoRatio + "' WHERE username = '" + player2ID + "'");
		        }
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
			
		if (winner == 2) {
			Connection c;
		    Statement stmt;
		        
		    try {
		    	c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
		        c.setAutoCommit(false);
		        System.out.println("Opened database successfully");
		        stmt = c.createStatement();
		        if (vsCPU == false) {
		        	ResultSet rs1 =  stmt.executeQuery("SELECT wins FROM User WHERE username = '" + player2ID + "'");
		        	while (rs1.next()) {
		        		twoWins = rs1.getDouble("wins");
		        	}
		        	++twoWins;
		        	stmt.executeUpdate("UPDATE User SET wins = '"+ twoWins + "' WHERE username = '" + player2ID + "'");
		        	ResultSet rs2 = stmt.executeQuery("SELECT losses FROM User WHERE username = '" + player2ID + "'");
		        	while (rs2.next()) {
		        		twoLosses = rs2.getDouble("losses");	
		        	}
		        	double twoRatio = twoWins / twoLosses;
		        	stmt.executeUpdate("UPDATE User SET ratio = '" + twoRatio + "' WHERE username = '" + player2ID + "'");
		        }
		        ResultSet rs3 = stmt.executeQuery("SELECT losses FROM User WHERE username = '" + player1ID + "'");
		        while (rs3.next()) {
		        	oneLosses =  rs3.getDouble("losses");
		        }
		        ++oneLosses;
		        stmt.executeUpdate("UPDATE User SET losses = '"+ oneLosses + "' WHERE username = '" + player1ID + "'");
		        ResultSet rs4 = stmt.executeQuery("SELECT wins FROM User WHERE username = '" + player1ID + "'");
		        while (rs4.next()) {
		        	oneWins = rs4.getDouble("wins");
		        }
		        double oneRatio = oneWins / oneLosses;
		        stmt.executeUpdate("UPDATE User SET ratio = '" + oneRatio + "' WHERE username = '" + player1ID + "'");
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
		if (winner == 3) {
			Connection c;
		    Statement stmt;
		        
		    try {
		    	c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
		        c.setAutoCommit(false);
		        System.out.println("Opened database successfully");
		        stmt = c.createStatement();
		        if (vsCPU == false) {
		        	ResultSet rs1 = stmt.executeQuery("SELECT draws FROM User WHERE username = '" + player2ID + "'");
		        	while (rs1.next()) {
		        		twoDraws = rs1.getDouble("draws");
		        	}
		        	++twoDraws;
		        	stmt.executeUpdate("UPDATE User SET draws = '"+ twoDraws + "' WHERE username = '" + player2ID + "'");
		        	}
		        ResultSet rs2 = stmt.executeQuery("SELECT draws FROM User WHERE username = '" + player1ID + "'");
		        while (rs2.next()) {
		        	oneDraws = rs2.getDouble("draws");
		        }
		        ++oneDraws;
		        stmt.executeUpdate("UPDATE User SET wins = '"+ oneDraws + "' WHERE username = '" + player1ID + "'");
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
	}
	
	public void updateGameHistory(int winner, int mancalaCount1, int mancalaCount2) {
		if (winner == 1 || winner == 3) {
			Connection c;
		    Statement stmt;
		        
		    try {
		    	c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
		        c.setAutoCommit(false);
		        System.out.println("Opened database successfully");
		        stmt = c.createStatement();
		        if (vsCPU == true) {
		        	stmt.executeUpdate("INSERT INTO gamehistory (winnerid, loserid, winnerscore, loserscore) VALUES ('" + player1ID + "' , 'CPU', '" + mancalaCount1 + "', '" + mancalaCount2 + "')"); 
		        }
		        else {
		        	stmt.executeUpdate("INSERT INTO gamehistory (winnerid, loserid, winnerscore, loserscore) VALUES ('" + player1ID + "' , '" + player2ID + "', '" + mancalaCount1 + "', '" + mancalaCount2 + "')");
		        }
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
		if (winner == 2) {
			Connection c;
		    Statement stmt; 
		    try {
		    	c = DriverManager.getConnection("jdbc:sqlite:mancala.db");
		        c.setAutoCommit(false);
		        System.out.println("Opened database successfully");
		        stmt = c.createStatement();
		        if (vsCPU == true) {
		        	stmt.executeUpdate("INSERT INTO gamehistory (winnerid, loserid, winnerscore, loserscore) VALUES ('CPU', '" + player1ID + "', '" + mancalaCount2 + "', '" + mancalaCount1 + "')"); 
		        }
		        else {
		        	stmt.executeUpdate("INSERT INTO gamehistory (winnerid, loserid, winnerscore, loserscore) VALUES ('" + player2ID + "' , '" + player1ID + "', '" + mancalaCount2 + "', '" + mancalaCount1 + "')");
		        }
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
	}

	/**
	 * Checks the player who has selected is the correct player with reference to turn.
	 * @param player
	 * @param playerTurn
	 * @return
	 */
	public boolean checkTurn(int player, int playerTurn) {
		if (playerTurn % 2 == 0 && player == 1) {
			return false;
		}
		if (playerTurn % 2 == 1 && player == 2) {
			return false;
		}
		return true;
	}

	/**
	 * Checks the pit selection is valid in terms of side of the board and number of stones and if it is a store.
	 * @param boardArray
	 * @param player
	 * @param indx
	 * @return
	 */
	public boolean checkSelection (int[] boardArray, int player, int indx) {
		if (player == 1 && indx > 6 || player == 2 && indx < 7){
			return false;
		}
		if (indx == 6 || indx == 13) {
			return false;
		}
		if (boardArray[indx] == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Redistributes the stones based on pit selection, increments player turn when end turn condition met.
	 * @param boardArray
	 * @param indx
	 * @return
	 */
	public int[] redistributeStones ( int[] boardArray, int indx) {
			int	hold =  getStoneCount(indx);
			boardArray[indx] = 0;
			int lasthole = 0;
			for(int i = indx + 1; i < 14 && hold > 0; ++i) {
					if ( isOppMancalaStore(i,  getPlayer()) == false) {
						++boardArray[i];
						--hold;
						lasthole = i;
					}
						if (i == 13 && hold > 0){
							for(int j = 0; j < 14 && hold > 0; ++j) {
								if ( isOppMancalaStore(j,  getPlayer()) == false) {
								++boardArray[j];
								--hold;
								lasthole = j;
								}
							}
						}
			}

			if (boardArray[lasthole] == 1 && hold == 0 &&  isOwnMancalaStore(lasthole,  getPlayer()) == false) {
					repeatTurn = false;
					incrPlayerTurn();
					int t = player;
					player = waitingPlayer;
					waitingPlayer = t;
					setPlayer(player);
					setWaitingPlayer(waitingPlayer);
					changeTurnLabel();
					setValues();
					return boardArray;
			}
			if (boardArray[lasthole] > 1 && hold == 0 &&  isOwnMancalaStore(lasthole,  getPlayer()) == false) {
				redistributeStones(boardArray, lasthole);
				System.out.println("recursed");
				return boardArray;
			}
			if (isOwnMancalaStore(lasthole,  getPlayer()) == true && hold == 0) {
				repeatTurn = true;
				changeTurnLabel();
				setValues();
				return boardArray;
				}
			return boardArray;
	}
	
	public void main(String args[]) {
	}
	
	/**
	 * Get the current gameID.
	 * @return the current GameID.
	 */
	public int getGameID() {
		return gameID;
	}

	/**
	 * Get the current state of the Board Array.
	 * @return Board Array.
	 */
	public int[] getBoardArray() {
		return boardArray;
	}

	/**
	 * Set new values for Board Array
	 * @param postMove
	 * @return the Board Array post move.
	 */
	public int[] setBoardArray(int[] postMove) {
		boardArray = postMove;
		return boardArray;
	}

	/**
	 * Get the number of stones in a pit.
	 * @param indx number of the pit.
	 * @return the number of stones in the corresponding pit.
	 */
	public int getStoneCount(int indx) {
		int stoneCount = boardArray[indx];
		return stoneCount;
	}

	/**
	 * Checks if the pit selection is the opponent's mancala store.
	 * @param indx of the selected pit.
	 * @param current player
	 * @return boolean true if selection is opponent's mancala store.
	 */
	public boolean isOppMancalaStore(int indx, int player) {
		if ((indx == 6 && player == 2)||(indx == 13 && player == 1)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Checks if the pit selection is the owns mancala store.
	 * @param indx of the selected pit.
	 * @param current player
	 * @return boolean true if selection is own mancala store.
	 */
	public boolean isOwnMancalaStore(int indx, int player) {
		if ((indx == 6 && player == 1)||(indx == 13 && player == 2)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Get the player assignment for a pit.
	 * @param indx of selected pit.
	 * @return player number of player whose side it is on.
	 */
	public int getPlayerAssignment(int indx) {
		if (indx <= 6){
			return 1;
		}
		if (indx > 6){
			return 2;
		}
		return -1;
	}
	
	/**
	 * Checks win condition.
	 * @return integer corresponding to player who has won. Or 3 for a draw, -1 for not over.
	 */
	public int hasWon() {
		int sideCount1 = 0;
		int sideCount2 = 0;
		for(int i = 0; i < 6 ; ++i){
				sideCount1 = sideCount1 + boardArray[i];
				sideCount2 = sideCount2 + boardArray[i+7];
		}
		if (sideCount1 == 0) {
			mancalaCount1 = boardArray[6] + sideCount2;
			mancalaCount2 = boardArray[13];
			if (mancalaCount1 > mancalaCount2) {
				winner = 1;
			}
			if (mancalaCount1 < mancalaCount2) {
				winner = 2;
			}
			if (mancalaCount1 == mancalaCount2) {
				winner = 3;
			}
		}
		if (sideCount2 == 0) {
			mancalaCount2 = boardArray[13] + sideCount1;
			mancalaCount1 = boardArray[6];
			if (mancalaCount1 > mancalaCount2) {
				winner = 1;
			}
			if (mancalaCount1 < mancalaCount2) {
				winner = 2;
			}
			if (mancalaCount1 == mancalaCount2) {
				winner = 3;
			}
		}
		return winner;
	}
	
	/**
	 * Determines pit selection of CPU player.
	 * @throws InterruptedException
	 */
	public void cpuSelection() throws InterruptedException {
		int selection = 0;
		if (vsCPU == true && getPlayer() == 2) {
			for (int i = 0; i < 14; ++i) { 
				if (checkSelection(getBoardArray(), getPlayer(), i) == true) {
					selection = i;
				}
			}
		turnLabel.setText("CPU selected pit " + Integer.toString(selection) + "!");
		move(getBoardArray(), selection);
		}
	}
	
	/**
	 * Populates pit labels with corresponding values.
	 */
	private void labelArray() {
		holeLabels = new Label[14];
		holeLabels[0] = value0;
	    holeLabels[1] = value1;
	    holeLabels[2] = value2;
	    holeLabels[3] = value3;
	    holeLabels[4] = value4;
	    holeLabels[5] = value5;
	    holeLabels[6] = value6;
	    holeLabels[7] = value7;
	    holeLabels[8] = value8;
	    holeLabels[9] = value9;
	    holeLabels[10] = value10;
	    holeLabels[11] = value11;
	    holeLabels[12] = value12;
	    holeLabels[13] = value13;
	}

	/**
	 * Changes values displayed in labels to corresponding values in Board Array.
	 */
	@FXML protected void setValues() { 
		for(int i = 0; i < 14; ++i) {
			holeLabels[i].setText(Integer.toString(boardArray[i]));
		}
	}
	
	/** 
	 * Changes text in game label to inform player what to do next.
	 */
	@FXML protected void changeTurnLabel() {
		if (repeatTurn == true) {
			if (vsCPU == true && getPlayer() == 2) {
				turnLabel.setText("Last stone dropped in CPUs Mancala! Click here for CPU move.");
			}
			else {
				turnLabel.setText("Last stone dropped in your Mancala! Player " + Integer.toString(getPlayer()) + " select another pit!");
			}
		}
		else {
			if (vsCPU == true && getPlayer() == 2) {
				turnLabel.setText("Click here for CPU move.");
			}
			else {
				turnLabel.setText("Player " + Integer.toString(getPlayer()) + " select pit!");
			}
		}
	}
	
	/**
	 * Executes CPU move when label clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void cpuLabelClicked (MouseEvent event) throws InterruptedException {
		cpuSelection();
	}
	
	/**
	 * Executes player move when pit 0 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn0(MouseEvent event) throws InterruptedException {
		move(getBoardArray(), 0);
	}
	
	/**
	 * Executes player move when pit 1 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn1(MouseEvent event) throws InterruptedException {
		move(getBoardArray(), 1);
	}
	
	/**
	 * Executes player move when pit 2 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn2(MouseEvent event) throws InterruptedException {
		move(getBoardArray(), 2);
	}
	
	/**
	 * Executes player move when pit 3 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn3(MouseEvent event) throws InterruptedException {
		move(getBoardArray(), 3);
	}
	
	/**
	 * Executes player move when pit 4 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn4(MouseEvent event) throws InterruptedException {
		move(getBoardArray(), 4);
	}
	
	/**
	 * Executes player move when pit 5 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn5(MouseEvent event) throws InterruptedException {
		move(getBoardArray(), 5);
	}
	
	/**
	 * Executes player move when pit 6 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn6(MouseEvent event) throws InterruptedException {
		move(getBoardArray(), 6);
	}
	
	/**
	 * Executes player move when pit 7 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn7(MouseEvent event) throws InterruptedException {
		if (vsCPU == false) {
			move(getBoardArray(), 7);
		}
	}
	
	/**
	 * Executes player move when pit 8 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn8(MouseEvent event) throws InterruptedException {
		if (vsCPU == false) {
			move(getBoardArray(), 8);
		}
	}
	
	/**
	 * Executes player move when pit 9 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn9(MouseEvent event) throws InterruptedException {
		if (vsCPU == false) {
			move(getBoardArray(), 9);
		}
	}
	
	/**
	 * Executes player move when pit 10 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn10(MouseEvent event) throws InterruptedException {
		if (vsCPU == false) {
			move(getBoardArray(), 10);
		}
	}
	
	/**
	 * Executes player move when pit 11 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn11(MouseEvent event) throws InterruptedException {
		if (vsCPU == false) {
			move(getBoardArray(), 11);
		}
	}
	
	/**
	 * Executes player move when pit 12 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn12(MouseEvent event) throws InterruptedException {
		if (vsCPU == false) {
			move(getBoardArray(), 12);
		}
	}
	
	/**
	 * Executes player move when pit 13 is clicked.
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML protected void clickedOn13(MouseEvent event) throws InterruptedException {
		if (vsCPU == false) {
			move(getBoardArray(), 13);
		}
	}
}
