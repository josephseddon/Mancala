package mancala;

/**
 * 
 * @author Neo
 *
 */
public class FavoriteUser {
	private String username;
	private String firstname;
	private String lname;
	private String wins;
	private String losses;
    private String ratio;
    
    /**
     * Returns the Username.
     * @return username
     */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Set new Username.
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Get the first name of favourite.
	 * @return first name
	 */
	public String getFirstname() {
		return firstname;
	}
	
	/**
	 * Set first name.
	 * @param firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	/**
	 * Get last name.
	 * @return
	 */
	public String getLname() {
		return lname;
	}
	
	/**
	 * Set last name.
	 * @param lname
	 */
	public void setLname(String lname) {
		this.lname = lname;
	}
	
	/**
	 * Get number of wins.
	 * @return
	 */
	public String getWins() {
		return wins;
	}
	
	/**
	 * Set number of wins.
	 * @param wins
	 */
	public void setWins(String wins) {
		this.wins = wins;
	}
	
	/**
	 * Get number of losses.
	 * @return
	 */
	public String getLosses() {
		return losses;
	}
	
	/**
	 * Set number of losses.
	 * @param losses
	 */
	public void setLosses(String losses) {
		this.losses = losses;
	}
	
	/**
	 * Get win/loss ratio.
	 * @return
	 */
    public String getRatio() {
		return ratio;
	}
    
    /**
     * Set win/loss ratio.
     * @param ratio
     */
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
}
