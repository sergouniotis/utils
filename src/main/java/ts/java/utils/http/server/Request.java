package ts.java.utils.http.server;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Request {

	private static final Logger LOGGER = LoggerFactory.getLogger(Request.class);

	private InputStream input;

	private String uri;

	public Request(InputStream input) {
		this.input = input;
	}

	public InputStream getInput() {
		return input;
	}

	public String getUri() {
		return uri;
	}

	public void parse() {
		// Read a set of characters from the socket
		StringBuffer request = new StringBuffer(Utils.BUFFER_SIZE);
		int i;
		byte[] buffer = new byte[Utils.BUFFER_SIZE];

		try {
			i = input.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			i = -1;
		}

		for (int j = 0; j < i; j++) {
			request.append((char) buffer[j]);
		}

		String data = request.toString();

		LOGGER.info(data);

		String[] lines = data.split("\n");

		String content = lines[lines.length - 1];

		LOGGER.info(content);

		uri = parseUri(data);

		synchronized (Monitor.INSTANCE) {
			Monitor.INSTANCE.notify();
		}

	}

	private String parseUri(String requestString) {
		int index1, index2;
		index1 = requestString.indexOf(' ');

		if (index1 != -1) {
			index2 = requestString.indexOf(' ', index1 + 1);
			if (index2 > index1)
				return requestString.substring(index1 + 1, index2);
		}

		return null;
	}

}
