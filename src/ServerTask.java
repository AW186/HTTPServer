import java.io.*;
import java.net.*;

public class ServerTask extends Thread {
	private Socket client;
	private boolean enable = false;
	ServerTask() {
	
	}
	ServerTask(Socket client) {
		this.client = client;
		enable = true;
	}
	public void setClient(Socket client) {
		this.client = client;
		enable = true;
	}
	protected void questDidRecieved(InputStream in) {

	}

	protected void sendResponse(OutputStream out) {

	}

	public void run() {
		try {
			questDidRecieved(client.getInputStream());
			sendResponse(client.getOutputStream());
			client.close();
		} catch (Exception e) {
			System.out.println("Error e");
		}
	}
}














