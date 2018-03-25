import java.net.*;
import java.io.*;

public class Server{
	public static void main (String[] args)throws Exception{
		try{
			String sentence;
			String clientreply;

			ServerSocket welcomeSocket = new ServerSocket(5678);  
			while(true){
			Socket connectionSocket = welcomeSocket.accept();
			
			DataInputStream inputStreamFromServerUser = new DataInputStream(System.in);
			InputStreamReader serverUserStreamReader = new InputStreamReader(inputStreamFromServerUser);
			BufferedReader inFromServerUser = new BufferedReader(serverUserStreamReader);
			
			System.out.println("Enter string to send to client:");

			DataOutputStream outputStreamToClient = new DataOutputStream(connectionSocket.getOutputStream());
			sentence = inFromServerUser.readLine();

			outputStreamToClient.writeBytes(sentence + '\n');

			DataInputStream inputStreamFromClient = new DataInputStream(connectionSocket.getInputStream());
			InputStreamReader clientStreamReader = new InputStreamReader(inputStreamFromClient);
			BufferedReader inFromClient = new BufferedReader(clientStreamReader);

			clientreply = inFromClient.readLine();
			System.out.println("From client: " + clientreply);
			}
		}
		catch(IOException e){
			System.out.println(e);
		}
	}
	
}