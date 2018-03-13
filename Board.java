// Author: Christopher Richard Bilger
import java.util.Random;
import java.util.Scanner;
import java.io.Console;

class Board{
	// Global variable initialization
	private static String consonants = "BCDFGHJKLMNPQRSTVWXYZ";
	private static String vowels = "AEIOU";
	private static String allAcceptableCharacters = consonants + vowels + "'-& ";
	
	/**
	 * The spin method is used to generate a random integer inclusively
	 * between 1 and 8, and then multiply that number by 100.
	 * @return int A random number between 100-800, incrementing by 100
	 */
   private static int spin(){
      return (new Random().nextInt(8)+1)*100;
   }
	
	/**
	 * The main method of the program that will run the game.
	 */
   public static void main(String[] args){
		// Get the player's names
      Scanner input = new Scanner(System.in);
		
		System.out.println("Enter all player's names separated by a space");
		System.out.print("Example: Chris Dom Dane\n>> ");
		
		// Parse the player's name and check for >3 names
		String[] names = input.nextLine().split(" ");
		while(names.length < 3){
			System.out.println("Please enter at least 3 names.");
			names = input.nextLine().split(" ");
		}
		
		// Create the player objects
		Player player_1 = new Player(names[0]);
		Player player_2 = new Player(names[1]);
		Player player_3 = new Player(names[2]);
		Player[] allPlayers = new Player[]{player_1, player_2, player_3};
		
		// Create the category string by letting the user input it
		System.out.println("Please input a category of your choice.");
		System.out.println("Your category must be between 5 and 50 letters long.");
		
		char[] hiddenCharacters = System.console().readPassword();
		String userInput = new String(hiddenCharacters).toUpperCase();
		
		boolean isAcceptable = true;
		for(int index = 0; index < userInput.length(); index++){
			if(!allAcceptableCharacters.contains(userInput.split("")[index]))
				isAcceptable = false;
		}
		
		while(userInput.isEmpty()
				|| userInput == null
				|| userInput.length() < 5
				|| userInput.length() > 50
				|| isAcceptable == false){
			System.out.println("Please input a category of your choice.");
			System.out.println("Your category must be between 5 and 50 letters long.");
			
			hiddenCharacters = System.console().readPassword();
			userInput = new String(hiddenCharacters).toUpperCase();
			
			isAcceptable = true;
			for(int index = 0; index < userInput.length(); index++){
				if(!allAcceptableCharacters.contains(userInput.split("")[index]))
					isAcceptable = false;
			}
		}
		
		String category = userInput; // Now we know that the userInput variable abides by the rules
		
		// Start the game loop
		boolean gameOver = false;
		int playerTurn = -1;
		String guessedLetters = category.replaceAll("[A-Z]","*");
		String allLettersGuessedSoFar = "";
		
		while(!gameOver){
			//player turn changer
			playerTurn += 1;
			if(playerTurn >= 3)
				playerTurn = 0;
			
			System.out.println("It is now " + allPlayers[playerTurn].getName() + "'s turn!");
			
			int turnChoice = 0;
			while(turnChoice == 0){
				System.out.println("Would you like to; guess a letter, buy a vowel, or solve the puzzle?");
				System.out.println("1: Guess a letter");
				System.out.println("2: Buy a vowel");
				System.out.println("3: Solve the puzzle");
				System.out.println("4: Exit the game");
				
				userInput = input.nextLine();
				if(userInput.isEmpty() || userInput == null)
					continue;
				else {
					turnChoice = Character.getNumericValue(userInput.charAt(0));
					if(turnChoice>4 || turnChoice<1)
						turnChoice = 0;
				}
			}
			
			// Allows the player to 'guess again' if they got the correct choice
			// on their previous guess.
			boolean correct = true;
			switch(turnChoice){
				case 1:
					while(correct){
						int spin = spin();
						System.out.println("You have spun the wheel for.. $"+spin+"!");
						System.out.println("Please guess a letter, not including a vowel...");
						System.out.println("Enter nothing to go back...");
						
						userInput = input.nextLine();
						if(userInput.isEmpty() || userInput == null){
							playerTurn -= 1;
							break;
						}
						
						String letter = Character.toString(userInput.charAt(0)).toUpperCase();
						
						if(!consonants.contains(letter)){
							System.out.println("Sorry! You didn't select a consonant!");
							correct = false;
							break;
						}
						
						if(category.contains(letter)){
							allLettersGuessedSoFar += letter;
							guessedLetters = category.replaceAll("[^ "+allLettersGuessedSoFar+"]", "*"); // Ex: replaceAll("[^G]", "*");
							System.out.println("You guessed "+letter+"! The new category phrase is "+guessedLetters+"!");
						} else {
							System.out.println("Sorry! The letter "+letter+" is not in the category phrase.");
							correct = false;
							break;
						}
						
						// Pay out if correct
						int depositRate = guessedLetters.length() - guessedLetters.replaceAll(letter, "").length();
						allPlayers[playerTurn].setBalance(allPlayers[playerTurn].getBalance()+(depositRate*spin));
						
						// Check for board completion
						if(guessedLetters == category) {
							System.out.println("The category has been finished, and the winner is "+allPlayers[playerTurn].getName()+"!");
							System.out.println("The final balances are as follows:");
							for(Player gamePlayer : allPlayers)
								System.out.println(gamePlayer.getName()+": $"+gamePlayer.getBalance());
							
							gameOver = true;
							break;
						}
					}
					
					break;
				case 2:
					while(correct){
						System.out.println("Which vowel would you like to buy? (A, E, I, O, U)");
						System.out.println("Enter nothing to go back...");
						
						userInput = input.nextLine();
						if(userInput.isEmpty() || userInput == null){
							playerTurn -= 1;
							break;
						}
						
						String letter = Character.toString(userInput.charAt(0)).toUpperCase();
						
						// Verify that they have $100 dollars to pay for the vowel
						if(allPlayers[playerTurn].getBalance() >= 100)
							allPlayers[playerTurn].setBalance(allPlayers[playerTurn].getBalance()-100);
						else {
							System.out.println("Sorry, but you do not have $100 to buy a vowel!");
							playerTurn -= 1;
							break;
						}
						
						
						if(!vowels.contains(letter)){
							System.out.println("Sorry! You didn't select a vowel!");
							correct = false;
							break;
						}
						
						if(category.contains(letter)){
							allLettersGuessedSoFar += letter;
							guessedLetters = category.replaceAll("[^ "+allLettersGuessedSoFar+"]", "*"); // Ex: replaceAll("[^G]", "*");
							System.out.println("You guessed "+letter+"! The new category phrase is "+guessedLetters+"!");
						} else {
							System.out.println("Sorry! The letter "+letter+" is not in the category phrase.");
							correct = false;
							break;
						}
						
						// Check for board completion
						if(guessedLetters == category) {
							System.out.println("The category has been finished, and the winner is "+allPlayers[playerTurn].getName()+"!");
							System.out.println("The final balances are as follows:");
							for(Player gamePlayer : allPlayers)
								System.out.println(gamePlayer.getName()+": $"+gamePlayer.getBalance());
							
							gameOver = true;
							break;
						}
					}
					
					break;
				case 3:
					System.out.println(allPlayers[playerTurn].getName()+" is going to guess the puzzle!");
					System.out.println("What do you think the category phrase is?");
					String guess = input.nextLine().toUpperCase();
					
					if(guess.equals(category)){
						System.out.println("Correct! You have won! You also get an extra $1000!");
						allPlayers[playerTurn].setBalance(allPlayers[playerTurn].getBalance() + 1000);
						
						System.out.println("The final balances are as follows:");
						for(Player gamePlayer : allPlayers)
							System.out.println(gamePlayer.getName()+": $"+gamePlayer.getBalance());
						
						gameOver = true;
					} else {
						System.out.println("Sorry! The category that you guessed is incorrect.");
						correct = false;
					}
					
					break;
				case 4:
					return;
				default:
					break;
			}
		}
   }
}

// Citations:
// https://stackoverflow.com/questions/19388037/converting-characters-to-integers-in-java
// https://stackoverflow.com/questions/7940053/how-to-replace-all-characters-in-a-user-input-string-except-one
// https://stackoverflow.com/questions/13245490/check-how-many-times-string-contains-character-g-in-eligible-string