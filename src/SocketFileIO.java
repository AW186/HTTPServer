import java.io.*;
import java.net.*;
import java.lang.Object;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class SocketFileIO {
	
	public static String getExtention(String fileName) {
		String segments[] = fileName.split("\\.");
		if (segments.length > 1) {
			return segments[segments.length-1];
		}
		return "";
	}

	public static synchronized void sendTextFile(Socket sock, String path) {
		
	}

	public static synchronized void sendTextFile(PrintWriter out, String path) {
		File file = new File(path);
		
		if(file.exists() && !file.isDirectory()) { 
			System.out.println("File exist: " + path);
			try {
				BufferedReader inputFile = new BufferedReader(
					new InputStreamReader(
					new FileInputStream(path)));
				String line = inputFile.readLine();
				while(line != null) { 
					out.println(line);
					line = inputFile.readLine();
				}
			} catch (Exception e) {
				System.out.println("Error: " + e);
			}
		} else {
			System.out.println("File don't exist: " + path);
		}
	}

	public static synchronized void sendImageFile(OutputStream out, String path) {
		BufferedImage img;
		File file = new File(path);
		if(file.exists() && !file.isDirectory()) { 
			try {
				img = ImageIO.read(file);
				ImageIO.write(img, getExtention(path), out);
				System.out.println("Image should be sent to the browser");
			} catch (Exception e) {
				System.out.println("Error: " + e);
			}
		}
	}
}


















