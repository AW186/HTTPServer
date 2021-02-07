import java.io.*;
import java.text.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class ContactPage implements DynamicPage {
	public void get(String args, OutputStream out) {
		storeData(args);
		sendResponse(args, out);
	}
	public void post(String args, OutputStream out) {
		if (storeData(args)) {
			System.out.println("Successfully recieved Data");
		} else {
			System.out.println("Failed to recieved Data");
		}
		sendResponse(args, out);
	}

	public boolean storeData(String args) {
		boolean isFormatCorrect = true; 
		try {
/*			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/ContactList?"
				+ "user=Arthur&password=2001");
			System.out.println("creating statement...");
			Statement statement = connect.createStatement(); */
			Iterable<String> data = Arrays.asList(args.split("&"));
			HashMap<String, String> dict = new HashMap<String, String>();
			data.forEach((arg) -> {
				if (arg.split("=").length == 2) {
					dict.put(arg.split("=")[0], arg.split("=")[1]);
				}
			});
			isFormatCorrect = 
				dict.containsKey("firstName") &&
				dict.containsKey("lastName") &&
				dict.containsKey("email") &&
				dict.containsKey("content");
			System.out.println(isFormatCorrect ? "valid input" : "invalid input");
			if (isFormatCorrect) {
				SimpleDateFormat f  = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
				String date = f.format(new Date(System.currentTimeMillis()));
				String fileName = "../../Messages/" + 
					dict.get("firstName") +
                    "_" +
                    dict.get("lastName") +
                    "-" + date; 
				File file = new File(fileName);
				file.createNewFile();
				FileWriter writer = new FileWriter(fileName);
				writer.write(dict.get("email") + "\n");
				writer.write(dict.get("content"));
				writer.close();
	/*			statement.executeQuery("INSERT INTO contactPerson VALUES ('" +
					dict.get("firstName") + "','" +
					dict.get("lastName") + "','" +
					dict.get("email") + "','" +
					date + "','" +
					"../../Messages/" + fileName + "');"); */
			}
	//		connect.close();
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return isFormatCorrect;
	}

	private void sendResponse(String args, OutputStream out) {
		PrintWriter writer = new PrintWriter(out);
		HTTPServer.printSucceedHeader(writer);	
		writer.println("");
		SocketFileIO.sendTextFile(writer, "../webPage/contact.html");
		writer.close();
	}
}
