import java.io.*;
import java.net.*;
import java.util.*;

public class PersonalPageServer extends ServerTask {
	public static void main(String args[]) {
		PersonalPageServer task = new PersonalPageServer();
		task.registerDynamicPage("/contact", new ContactPage());	
		HTTPServer server = new HTTPServer(task);	
		server.start();
	}
	private HashMap<String, DynamicPage> dynamicPageList = new HashMap<String, DynamicPage>();
	private HTTPMethod method = HTTPMethod.GET;
	private String filePath = "";
	private String content = "";

	public void registerDynamicPage(String key, DynamicPage page) {
		dynamicPageList.put(key, page);
		System.out.println("dynamic page registered: " + key);
	}

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
						method = HTTPMethod.GET;
					}
					if (args[0].equals("POST")) {
						System.out.println("Going to respond POST");
						this.filePath = args[1];
						havingContent = true;
						method = HTTPMethod.POST;
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

	protected void sendStaticSource(String path, PrintWriter writer, OutputStream out) {
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
	}
	protected void sendResponse(OutputStream out) {
		PrintWriter writer = new PrintWriter(out);
		HTTPServer.printSucceedHeader(writer);
		System.out.println(filePath);
		if (dynamicPageList.containsKey(filePath)) {
			switch (method) {
				case GET:
					dynamicPageList.get(filePath).get(content, out);
					break;
				case POST:
					dynamicPageList.get(filePath).post(content, out);
					break;
			}
		} else {
			String path = "../webPage" + filePath;
			sendStaticSource(path, writer, out);
		}
		writer.close();
	}
	private void get(String arg) {
		this.filePath = arg;
	}
	protected ServerTask duplicate() {			
		PersonalPageServer task = new PersonalPageServer();
		task.setClient(client);
		task.registerDynamicPage("/contact", new ContactPage());	
		return task;
	}
}











