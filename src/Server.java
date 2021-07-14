import java.net.*;
import java.io.*;

public class Server {
  public static void main(String[] args) throws IOException {

	ServerSocket ActionServerSocket = null;
    boolean listening = true;
    String ActionServerName = "Server";
    int ActionServerNumber = 4445;
    
    int SharedVariable = 1;

    //Create the shared object in the global scope...
    
    SharedState ourSharedActionStateObject = new SharedState(SharedVariable);
        
    // Make the server socket

    try {
      ActionServerSocket = new ServerSocket(ActionServerNumber);
    } catch (IOException e) {
      System.err.println("Could not start " + ActionServerName + " specified port.");
      System.exit(-1);
    }
    System.out.println(ActionServerName + " started");

    //Got to do this in the correct order with only four clients!  Can automate this...
    
    while (listening){
      new ServerThread(ActionServerSocket.accept(), "ActionServerThread1", ourSharedActionStateObject).start();
      new ServerThread(ActionServerSocket.accept(), "ActionServerThread2", ourSharedActionStateObject).start();
      new ServerThread(ActionServerSocket.accept(), "ActionServerThread3", ourSharedActionStateObject).start();
      System.out.println("New " + ActionServerName + " thread started.");
    }
    ActionServerSocket.close();
  }
}