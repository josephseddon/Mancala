package mancala;
//win conditions and adding up of holes upon win 
//labels to show whose go it is and maybe highlighting which holes are playable?

import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Move {
	private int[] boardarray = new int[14];
	private int player1ID = 1;
	private int player2ID = 2;
	private int winner = -1;
	private static int gameID = 0;
	private static int player = 1;
	private static int waitingPlayer = 2;
	private static int playerTurn = 1;
	private static boolean repeatTurn = false;
	private Label[] holeLabels;
	
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
	
	public void initialize() {
		for(int i = 0; i <= 13; ++i){
				boardarray[i] = 4;
			}
		boardarray[6] = 0;
		boardarray[13] = 0;
		labelArray();
		setValues();
		System.out.println("Player " + getPlayer() +  " pick a hole!");
	}

	public int getPlayer() {
		return player;
	}
	public void setPlayer(int a) {
		player = a;
	}

	public int getWaitingPlayer() {
		return waitingPlayer;
	}
	public static void setWaitingPlayer(int a){
		waitingPlayer = a;
	}



	public int getPlayerTurn(){
		return playerTurn;
	}
	public void incrPlayerTurn(){
		++playerTurn;
	}

	public void move (int[] boardarray, int indx) {
		int t = 0;
		if (hasWon() > 0){
			if (hasWon() == 1){
				turnLabel.setText("Player 1 wins! Congratulations!");
				System.out.println("Player 1 wins!");
			}
			if (hasWon() == 2){
				turnLabel.setText("Player 2 wins! Congratulations!");
				System.out.println("Player 2 Wins!");
			}
			if (hasWon() == 3) {
				turnLabel.setText("It's a tie!");
				System.out.println("Draw!");
			}
		}
		if (hasWon() < 0){
			int player =  getPlayer();
			int playerTurn =  getPlayerTurn();
			int waitingPlayer =  getWaitingPlayer();
			if (checkSelection( getBoardArray(), player, indx) == false){
				turnLabel.setText("Hey! You are Player " + player + ", please select a pit from your side. Remember, you cannot select a Mancala.");
				System.out.println("Error: You are player " + player + ", please select a pit from your side. You cannot select a Mancala.");
			}
			if(checkTurn(player, playerTurn) == true && checkSelection( getBoardArray(), player, indx) == true){
				setBoardArray(redistributeStones(getBoardArray(), indx));
				for(int i = 0; i < 14; ++i){
			      	System.out.println(i + ": " + boardarray[i]);
				}
    		}
		}
		
	}


	public boolean checkTurn (int player, int playerTurn){
		if(playerTurn % 2 == 0 && player == 1){
			return false;
		}
		if(playerTurn % 2 == 1 && player == 2){
			return false;
		}
		return true;
	}

	public boolean checkSelection (int[] boardarray, int player, int indx){
		if(player == 1 && indx > 6 || player == 2 && indx < 7){
			return false;
		}
		if(indx == 6 || indx == 13){
			return false;
		}
		if(boardarray[indx] == 0){
			return false;
		}
		return true;
	}

	public int[] redistributeStones ( int[] boardarray, int indx){
			int	hold =  getStoneCount(indx);
			boardarray[indx] = 0;
			int lasthole = 0;
			for(int i = indx + 1; i < 14 && hold > 0; ++i){
					if( isOppMancalaStore(i,  getPlayer()) == false){
						++boardarray[i];
						--hold;
						lasthole = i;
						System.out.println("hold: " + hold + " last hole: " + lasthole + " stones: " + boardarray[lasthole] + "player = " +  getPlayer() +  isOwnMancalaStore(lasthole,  getPlayer()));
					}
						if(i == 13 && hold > 0){
							for(int j = 0; j < 14 && hold > 0; ++j){
								if( isOppMancalaStore(j,  getPlayer()) == false){
								++boardarray[j];
								--hold;
								lasthole = j;
								System.out.println("hold: " + hold + " last hole: " + lasthole + " stones: " + boardarray[lasthole] + "player = " +  getPlayer() +  isOwnMancalaStore(lasthole,  getPlayer()));
								}
							}
						}
			}

			if(boardarray[lasthole] == 1 && hold == 0 &&  isOwnMancalaStore(lasthole,  getPlayer()) == false){
					repeatTurn = false;
						incrPlayerTurn();
						int t = player;
						player = waitingPlayer;
						waitingPlayer = t;
						setPlayer(player);
						setWaitingPlayer(waitingPlayer);
						changeTurnLabel();
						System.out.println("Player " +  getPlayer() + " select pit.");
					System.out.println("last stone in empty hole");
					setValues();
					return boardarray;
			}
			if(boardarray[lasthole] > 1 && hold == 0 &&  isOwnMancalaStore(lasthole,  getPlayer()) == false){
				redistributeStones(boardarray, lasthole);
				System.out.println("recursed");
				return boardarray;
			}
			if(isOwnMancalaStore(lasthole,  getPlayer()) == true && hold == 0){
				repeatTurn = true;
				changeTurnLabel();
				System.out.println("Last stone dropped in your Mancala! Player " +  getPlayer() + " select hole: ");
				setValues();
				return boardarray;
				}
			System.out.println("end reached");
			return boardarray;
	
	}
	
	public void main(String args[]){
		System.out.println("Player " + player + " select pit.");
		//ADD FIRST EVENT HERE
		
	}
	
	public int getGameID(){
		return gameID;
	}


	public int[] getBoardArray(){
		return boardarray;
	}

	public int[] setBoardArray(int[] postMove){
		boardarray = postMove;
		return boardarray;
	}

	public int getStoneCount(int indx){
		int stoneCount = boardarray[indx];
		return stoneCount;
	}

	public boolean isOppMancalaStore(int indx, int player){
		if ((indx == 6 && player == 2)||(indx == 13 && player == 1)){
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isOwnMancalaStore(int indx, int player){
		if ((indx == 6 && player == 1)||(indx == 13 && player == 2)){
			return true;
		}
		else {
			return false;
		}
	}


	public int getPlayerAssignment(int indx){
		if(indx <= 6){
			return 1;
		}
		if(indx > 6){
			return 2;
		}
		return -1;
	}

	public int hasWon(){
		int sideCount1 = 0;
		int sideCount2 = 0;
		int mancalaCount1 = 0;
		int mancalaCount2 = 0;
		for(int i = 0; i < 6 ; ++i){
				sideCount1 = sideCount1 + boardarray[i];
				sideCount2 = sideCount2 + boardarray[i+7];
		}
		if(sideCount1 == 0) {
			mancalaCount1 = boardarray[6] + sideCount2;
			mancalaCount2 = boardarray[13];
			if (mancalaCount1 > mancalaCount2){
				winner = 1;
			}
			if (mancalaCount1 < mancalaCount2){
				winner = 2;
			}
			if (mancalaCount1 == mancalaCount2){
				winner = 3;
			}
		}
		if(sideCount2 == 0) {
			mancalaCount2 = boardarray[13] + sideCount1;
			mancalaCount1 = boardarray[6];
			if (mancalaCount1 > mancalaCount2){
				winner = 1;
			}
			if (mancalaCount1 < mancalaCount2){
				winner = 2;
			}
			if (mancalaCount1 == mancalaCount2){
				winner = 3;
			}
		}
		return winner;
	}
	
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

	@FXML protected void setValues() { 
		for(int i = 0; i < 14; ++i) {
			holeLabels[i].setText(Integer.toString(boardarray[i]));
		}
	}
	
	@FXML protected void changeTurnLabel() {
		if(repeatTurn == true) {
			turnLabel.setText("Last stone dropped in your Mancala! Player " + Integer.toString(getPlayer()) + " select another pit!");
		}
		else {
		turnLabel.setText("Player " + Integer.toString(getPlayer()) + " select pit!");
		}
	}
	

	
	@FXML protected void clickedOn0 (MouseEvent event) {
		move(getBoardArray(), 0);
	}
	@FXML protected void clickedOn1 (MouseEvent event) {
		move(getBoardArray(), 1);
	}
	@FXML protected void clickedOn2 (MouseEvent event) {
		move(getBoardArray(), 2);
	}
	@FXML protected void clickedOn3 (MouseEvent event) {
		move(getBoardArray(), 3);
	}
	@FXML protected void clickedOn4 (MouseEvent event) {
		move(getBoardArray(), 4);
	}
	@FXML protected void clickedOn5 (MouseEvent event) {
		move(getBoardArray(), 5);
	}
	@FXML protected void clickedOn6 (MouseEvent event) {
		move(getBoardArray(), 6);
	}
	@FXML protected void clickedOn7 (MouseEvent event) {
		move(getBoardArray(), 7);
	}
	@FXML protected void clickedOn8 (MouseEvent event) {
		move(getBoardArray(), 8);
	}
	@FXML protected void clickedOn9 (MouseEvent event) {
		move(getBoardArray(), 9);
	}
	@FXML protected void clickedOn10 (MouseEvent event) {
		move(getBoardArray(), 10);
	}
	@FXML protected void clickedOn11 (MouseEvent event) {
		move(getBoardArray(), 11);
	}
	@FXML protected void clickedOn12 (MouseEvent event) {
		move(getBoardArray(), 12);
	}
	@FXML protected void clickedOn13 (MouseEvent event) {
		move(getBoardArray(), 13);
	}
	


}
