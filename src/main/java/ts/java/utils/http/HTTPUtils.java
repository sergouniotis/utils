/**
 * 
 */
package ts.java.utils.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ts.java.utils.CollectionUtils;
import ts.java.utils.StreamUtils;
import ts.java.utils.StringUtils;

/**
 * @author sergouniotis
 * 
 */
public final class HTTPUtils {

	private static final String POST = "POST";

	private static final String GET = "GET";

	public static final String CONTENT_TYPE = "Content-Type";

	private HTTPUtils() {

	}

	public static Map<Integer, byte[]> doPost(String proxyHost, int proxyPort, final String url, Map<String, String> headers,
			byte[] content, String... args) throws IOException {

		return post(proxyHost, proxyPort, url, headers, content);
	}

	public static Map<Integer, byte[]> doPost(final String url, Map<String, String> headers, byte[] content, String... args)
			throws IOException {

		return post(null, -1, url, headers, content);
	}

	private static Map<Integer, byte[]> post(String proxyHost, int proxyPort, final String url, Map<String, String> headers,
			byte[] content) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		ByteArrayInputStream body = new ByteArrayInputStream(content);

		HttpURLConnection serverConnection = makeRequest(proxyHost, proxyPort, url, headers, body);

		int responseCode = serverConnection.getResponseCode();

		switch (responseCode) {
			case 500:
				StreamUtils.copy(serverConnection.getErrorStream(), bos);
				break;

			default:
				StreamUtils.copy(serverConnection.getInputStream(), bos);
				break;
		}

		byte[] result = bos.toByteArray();

		bos.close();

		Map<Integer, byte[]> map = new HashMap<>();
		map.put(responseCode, result);
		return map;
	}

	private static HttpURLConnection makeRequest(String proxyHost, int proxyPort, String url, Map<String, String> headers,
			InputStream dataStream, String... args) throws IOException {

		String requestMethod = dataStream != null ? POST : GET;

		Proxy proxy = null;
		if (StringUtils.hasText(proxyHost)) {
			InetSocketAddress proxySocketAddress = new InetSocketAddress(proxyHost, proxyPort);
			proxy = new Proxy(Proxy.Type.HTTP, proxySocketAddress);
		}

		// Create connection to server.
		HttpURLConnection serverConnection = null;
		if (null != proxy) {
			serverConnection = (HttpURLConnection) new URL(url).openConnection(proxy);
		} else {
			serverConnection = (HttpURLConnection) new URL(url).openConnection();
		}

		if (2 == args.length) {
			String username = args[0];
			String password = args[1];
			String userPassword = username + ":" + password;
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
			serverConnection.setRequestProperty("Authorization", "Basic " + encoding);
		}

		serverConnection.setRequestMethod(requestMethod);
		serverConnection.setDoOutput(true);

		// Set headers, if available.
		if (null != headers && !CollectionUtils.isEmpty(headers.entrySet())) {
			Set<Entry<String, String>> entrySet = headers.entrySet();
			for (Entry<String, String> entry : entrySet) {
				serverConnection.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}

		// Stream the data, if required.
		if (POST.equals(requestMethod)) {
			StreamUtils.copy(dataStream, serverConnection.getOutputStream());
		}

		return serverConnection;
	}

}
