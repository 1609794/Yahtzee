
import java.net.*;
import java.io.*;


public class SharedState {
	
	private SharedState mySharedObj;
	private String myThreadName;
	private int mySharedVariable;
	private boolean accessing = false; // true if a thread has a lock, false otherwise
	private int threadsWaiting=0; // number of waiating threads/writers
	
	private int player1round = 1;
	private int player2round = 1;
	private int player3round = 1;
	
	private int player1score = 0;
	private int player2score = 0;
	private int player3score = 0;
	
	private boolean player1finish = false;
	private boolean player2finish = false;
	private boolean player3finish = false;
	

	// Constructor	
	
		SharedState(int SharedVariable) {
			mySharedVariable = SharedVariable;
		}

	//Attempt to aquire a lock
	
		  public synchronized void acquireLock() throws InterruptedException{
		        Thread me = Thread.currentThread(); // get a ref to the current thread
		        System.out.println(me.getName()+" is attempting to acquire a lock!");	
		        ++threadsWaiting;
			    while (accessing) {  // while someone else is accessing or threadsWaiting > 0
			      System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
			      //wait for the lock to be released - see releaseLock() below 
			      wait();
			    }
			    // nobody has got a lock so get one
			    --threadsWaiting;
			    accessing = true;
			    System.out.println(me.getName()+" got a lock!"); 
			  }
		  
	// Releases a lock to when a thread is finished
		  
		  public synchronized void releaseLock() throws InterruptedException, IOException {
			  //release the lock and tell everyone
		      accessing = false;
		      notifyAll();
		      Thread me = Thread.currentThread(); // get a ref to the current thread
		      System.out.println(me.getName()+" released a lock!");
		      
		  }

	/* The processInput method */	  
		  
		public String processInput(String myThreadName, String theInput) {
			System.out.println(myThreadName + " received "+ theInput);
			String theOutput = null;
			if (theInput.equalsIgnoreCase("quit")) {
				theOutput = "Game finished";
				return theOutput;
			}
			if(theInput.equalsIgnoreCase("Game Over")) {
				if(myThreadName.equalsIgnoreCase("ActionServerThread1")) {
					player1finish = true;
				}
				else if(myThreadName.equalsIgnoreCase("ActionServerThread2")) {
					player2finish = true;
				}
				else {
					player3finish = true;
				}
			
				if(player1finish == true && player2finish == true && player3finish == true) {
					String winner = Winner();
						if(winner == "player 1") {
							theOutput = "Player 1 is the winner with: " + player1score + " points. Player 2 scored: " + player2score + " and Player 3 scored: " + player3score;
							System.out.println(theOutput);
							return theOutput;
						}
						else if(winner == "player 2") {
							theOutput = "Player 2 is the winner with: " + player2score + " points. Player 1 scored: " + player1score + " and Player 3 scored: " + player3score;
							System.out.println(theOutput);
							return theOutput;
						}
						else if(winner == "player 3"){
							theOutput = "Player 3 is the winner with: " + player3score + " points. Player 2 scored: " + player2score + " and Player 1 scored: " + player1score;
							System.out.println(theOutput);
							return theOutput;
						}
				}	
				else {
					theOutput = "The game hasn't finished for every player. Please wait...";
					System.out.println(theOutput);
					return theOutput;
				}
				
		}
			int theInputinteger = Integer.parseInt(theInput);
			if(mySharedVariable < 14) {
			if(myThreadName.equals("ActionServerThread1")) {		
				if(mySharedVariable == player1round) {
				player1score = theInputinteger;
				theOutput = "Round for player1 : " + player1round + " player 1 score: " + player1score + " player 2 score: " + player2score + " player 3 score: " + player3score;
				player1round += 1;
				//Increment global round check
				GlobalRound();
				}
				else {
					theOutput = "not the same round";
				}
			}
		
			else if (myThreadName.equals("ActionServerThread2")) {	
				if(mySharedVariable == player2round) {
				player2score = theInputinteger;
				theOutput =  "Round for player2 : " + player2round + " player 2 score: " + player2score + " player 1 score: " + player1score + " player 3 score: " + player3score;
				player2round += 1;
				//increment global round check
				GlobalRound();
				}
				else {
					theOutput = "not the same round";
				}
			}
			
			else if (myThreadName.equals("ActionServerThread3")) {	
				if(mySharedVariable == player3round) {
				player3score = theInputinteger;
				theOutput =  "Round for player3 : " + player3round + " player 3 score: " + player3score + " player 1 score: " + player1score + " player 2 score: " + player2score;
				player3round += 1;
				//increment global round check
				GlobalRound();
				}
				else {
					theOutput = "not the same round";
				}
			}
		} else {
				theOutput = "The Game is Finished!!";
			}
			System.out.println(theOutput);
			return theOutput;
		}
		
		
		private void GlobalRound() {
		// TODO Auto-generated method stub
			if(player1round > mySharedVariable && player2round > mySharedVariable && player3round > mySharedVariable) {
				mySharedVariable++;
			}
	}
		
		private String Winner() {
			String winner;
			if(player1score > player2score && player1score > player3score) {
				winner = "player 1";
				return winner;
			}
			else if(player2score > player1score && player2score > player3score) {
				winner = "player 2";
				return winner;
			}
			else {
				winner = "player 3";
				return winner;
			}
		}
		
}









