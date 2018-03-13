// Author: Christopher Richard Bilger
class Player{
	// Global variables being initialized
   private String name;
   private int balance;
	
	/**
	 * Constructor with the player's name to create a Player object.
	 * @param name Name of the player
	 */
   public Player(String name){
      this.name = name;
      this.balance = 0;
   }
   
	/**
	 * Getter method for the player's name.
	 * @return String The name of the player
	 */
   public String getName(){
      return this.name;
   }
   
	/**
	 * Getter method for the balance of the player.
	 * @return int The current balance of the player
	 */
   public int getBalance(){
      return this.balance;
   }
   
   /**
	 * Setter method for the balance of the player.
	 * @param amount The amount to set the player's balance to
	 */
	public void setBalance(int amount){
      this.balance = amount;
   }
}