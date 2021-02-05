import java.io.*;
import java.net.*;

public class PersonalPageServer extends ServerTask{
	public static void main(String args[]) {
		HTTPServer server = new HTTPServer(new PersonalPageServer());	
		server.start();
	}
	private String filePath = "";
	private String content = "";
	protected void questDidRecieved(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = "";
		boolean havingContent = false;
		int contentSize = 0;
		do {
			try {
				line = reader.readLine();

				String args[] = line.split(" ");
				if (args.length > 1) {
					if (args[0].equals("GET")) {
						System.out.println("Going to respond GET");
						this.filePath = args[1];
					}
					if (args[0].equals("POST")) {
						System.out.println("Going to respond POST");
						havingContent = true;
					}
					if (args[0].equals("Content-Length:")) {
						contentSize = Integer.parseInt(args[1]);
					}
				}
				System.out.println(line);
			} catch (Exception e) {
				System.out.println("Error: " + e);
				return;
			}
		} while (!line.equals(""));
		if (havingContent) {
			if (contentSize != 0) {
				readContent(reader, contentSize);
			} else {
				readContent(reader);
			}
		}
	}

	private void readContent(BufferedReader reader) {
		this.content = "";
		String line;
		do {
			try {
				line = reader.readLine();
				this.content += line + "\n";
				System.out.println(line);
			} catch (Exception e) {
				System.out.println("Error: " + e);
				return;
			}
		} while(line.length() != 0);
	}
	private void readContent(BufferedReader reader, int size) {
		long remainingSize = size;
		this.content = "";
		String line;
		do {
			try {
				this.content += (char)reader.read();
				remainingSize--;
			} catch (Exception e) {
				System.out.println("Error: " + e);
				return;
			}
		} while(remainingSize > 0);
		System.out.println(this.content);
	}

	protected void sendResponse(OutputStream out) {
		PrintWriter writer = new PrintWriter(out);
		HTTPServer.printSucceedHeader(writer);
		String path = "../webPage" + filePath;
		File file = new File(path);
		ContentType type = ContentType.UNKNOWN;
		if (filePath.split("\\.").length == 2) {
			String extension = filePath.split("\\.")[1];
			System.out.println("extension: " + extension);
			writer.println("content-type: " + 
				ContentTypeConvertor.convertToContentType(extension));
			if (ContentTypeConvertor.isText(extension)) {
				type = ContentType.TEXT;
			} else if (ContentTypeConvertor.isImage(extension)) {
				type = ContentType.IMAGE;
			}
		}
		writer.println("");
		switch(type) {
			case TEXT:
				SocketFileIO.sendTextFile(writer, path);
				break;
			case IMAGE:
				SocketFileIO.sendImageFile(out, path);
				break;
			default:
				SocketFileIO.sendTextFile(writer, path);
				break;
		}
		writer.close();
	}
	private void get(String arg) {
		this.filePath = arg;
	}
}













