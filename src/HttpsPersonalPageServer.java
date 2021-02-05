import java.io.*;
import java.net.*;
import com.sun.net.*;
import java.lang.Object;
import com.sun.net.httpserver.*;

public class HttpsPersonalPageServer extends ServerTask{
	public static void main(String args[]) {
		HttpsServer server = HttpsServer.create
	}

	private String filePath = "";

	protected void questDidRecieved(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = "";
		do {
			try {
				line = reader.readLine();

				String args[] = line.split(" ");
				if (args.length > 1) {
					if (args[0].equals("GET")) {
						this.filePath = args[1];
					}
				}
				System.out.println(line);
			} catch (Exception e) {
				System.out.println("Error: " + e);
				return;
			}
		} while (!line.equals(""));
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













