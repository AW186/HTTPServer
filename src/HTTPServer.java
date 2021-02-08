import java.io.*;
import java.net.*;

public class HTTPServer {
	ServerTask task;
	public static void printSucceedHeader(PrintWriter out) {
		out.println("HTTP/1.1 200 OK");
	}
	
	public HTTPServer(ServerTask task) {
		this.task = task;
	}

	public void start() {
		ServerSocket server;
		System.out.println("Starting up HTTP server...");
		try {
			server = new ServerSocket(80);
		} catch (Exception e) {
			System.out.println("Error: " + e);
			return;
		}
		while(true) {
			try {
				Socket client = server.accept();
				System.out.println("Connected to a client");
				task.setClient(client);			
				task.start();
			} catch(Exception e) {
				System.out.println("Error " + e);
			}
		}
	}
}













