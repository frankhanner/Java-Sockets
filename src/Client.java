import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class Client {

	public static void main(String[] args) throws UnknownHostException,
			IOException {

		SocketFactory clientFactory = SSLSocketFactory.getDefault();
		Socket clientSocket = clientFactory.createSocket("localhost", 12500);

		InputStream in = System.in;
		InputStreamReader inReader = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(inReader);

		OutputStream out = clientSocket.getOutputStream();
		OutputStreamWriter outWriter = new OutputStreamWriter(out);
		BufferedWriter bw = new BufferedWriter(outWriter);

		String string = null;
		while ((string = br.readLine()) != null) {
			bw.write(string + '\n');
			bw.flush();
		}
	}
}
