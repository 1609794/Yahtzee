
import java.net.*;
import java.io.*;

public class ServerThread extends Thread {

		private Socket socket = null;
		private SharedState mySharedStateObject;
		private String myServerThreadName;
		private int mySharedVariable;
	   
		//Setup the thread
	  	public ServerThread(Socket socket, String ServerThreadName, SharedState SharedObject) {
	  		this.socket = socket;
	  		mySharedStateObject = SharedObject;
	  		myServerThreadName = ServerThreadName;
	  	}
	  	
	    public void run() {
	        try {
	            System.out.println(myServerThreadName + "initialising.");
	            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            String inputLine, outputLine;

	            while ((inputLine = in.readLine()) != null) {
	          	  // Get a lock first
	          	  try { 
	          		  mySharedStateObject.acquireLock();  
	          		  outputLine = mySharedStateObject.processInput(myServerThreadName, inputLine);
	          		  out.println(outputLine);
	          		  mySharedStateObject.releaseLock();  
	          	  } 
	          	  catch(InterruptedException e) {
	          		  System.err.println("Failed to get lock when reading:"+e);
	          	  }
	            }

	             out.close();
	             in.close();
	             socket.close();

	          } catch (IOException e){
	        	  e.printStackTrace();
	          }
	    }
}
