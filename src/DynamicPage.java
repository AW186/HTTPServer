import java.io.*;
import java.net.*;

public interface DynamicPage {
	public void get(String args, OutputStream out);
	public void post(String args, OutputStream out);
}
