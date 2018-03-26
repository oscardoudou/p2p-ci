package project;
import java.net.*;
import java.io.*;

public class Client{
	public static void main (String[] args) throws Exception{
		try{
			String serverSentence;
			String capitalizedSentence;
			String clientSentence;
			 
			while(true){
			Socket clientSocket = new Socket("127.0.0.1", 5678);
			
			DataInputStream inputStreamFromServer = new DataInputStream(clientSocket.getInputStream());
			InputStreamReader serverStreamReader = new InputStreamReader(inputStreamFromServer);
			BufferedReader inFromServer = new BufferedReader(serverStreamReader);

			serverSentence = inFromServer.readLine();
			System.out.println("From server: " + serverSentence);

			BufferedReader outToServer = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter string to send to server:");
			clientSentence = outToServer.readLine();
			DataOutputStream outputStreamToServer = new DataOutputStream(clientSocket.getOutputStream());

			//capitalizedSentence = serverSentence.toUpperCase();
			outputStreamToServer.writeBytes(clientSentence + '\n');
			//outputStreamToServer.writeBytes(capitalizedSentence + '\n');

			//System.out.println("Here is the capitalized result sent to server: " + capitalizedSentence);
			clientSocket.close();
			}
			// 
		}
		catch (IOException e){
			System.out.println(e);
		}
	}
}