package ts.java.utils.http.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

	public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

	private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

	private boolean shutdown;

	private String host = "localhost";

	private int port = 8081;

	public void await() {

		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName(host));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			System.exit(1);
		}

		// Loop waiting for a request
		while (!shutdown) {
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			try {
				socket = serverSocket.accept();
				input = socket.getInputStream();
				output = socket.getOutputStream();

				// create Request object and parse
				Request request = new Request(input);
				request.parse();

				// create Response object
				Response response = new Response(output);
				response.setRequest(request);
				// response.sendStaticResource();
				response.sendOk();

				// Close the socket
				socket.close();

				// check if the previous URI is a shutdown command
				shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
				if (shutdown) {
					LOGGER.info("Server shutdown.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
}
