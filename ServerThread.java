package project;
import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
	Socket socket = null;
	public ServerThread(Socket socket){
		this.socket = socket;
	}
	public void run (){
		String sentence;
		String clientreply;
		try{
			DataInputStream inputStreamFromServerUser = new DataInputStream(System.in);
			InputStreamReader serverUserStreamReader = new InputStreamReader(inputStreamFromServerUser);
			BufferedReader inFromServerUser = new BufferedReader(serverUserStreamReader);
			
			System.out.println("Enter string to send to client:");

			DataOutputStream outputStreamToClient = new DataOutputStream(socket.getOutputStream());
			sentence = inFromServerUser.readLine();

			outputStreamToClient.writeBytes(sentence + '\n');

			DataInputStream inputStreamFromClient = new DataInputStream(socket.getInputStream());
			InputStreamReader clientStreamReader = new InputStreamReader(inputStreamFromClient);
			BufferedReader inFromClient = new BufferedReader(clientStreamReader);

			clientreply = inFromClient.readLine();
			System.out.println("From client: " + clientreply);
		}
		catch(IOException e){
			System.out.println(e);
		}
	}
}