import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

public class Server extends Thread {
	// Socket to be used on server
	private Socket serverSocket;

	// Constructor
	Server(Socket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void run() {
		try {
			// Input from client
			InputStream in = this.serverSocket.getInputStream();
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(inReader);

			// Read input from client
			String data = null;
			while ((data = br.readLine()) != null) {
				// Print Data from client on server
				executeCommand(data);
				if (data.equals("close")) {
					serverSocket.close();
				}
				System.out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void executeCommand(String command) {
		Process proc;
		try {
			proc = Runtime.getRuntime().exec(command);
			StreamGobbler errorGobbler = new StreamGobbler(
					proc.getErrorStream(), "ERROR");

			StreamGobbler outputGobbler = new StreamGobbler(
					proc.getInputStream(), "OUTPUT");

			errorGobbler.start();
			outputGobbler.start();

			int exitVal = proc.waitFor();
		} catch (IOException e) {
			System.out.println("Unkown Command: " + command);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		// Setup SSL Socket Factory
		ServerSocketFactory socketFactory = SSLServerSocketFactory.getDefault();
		// Create new server socket
		ServerSocket serverSocket = socketFactory.createServerSocket(12500);

		System.out.println("Ready...");
		while (true) {
			// Listen for client and connect
			new Server(serverSocket.accept()).start();
			System.out.println("Connected Successfully!");
		}
	}
}
