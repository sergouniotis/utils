/**
 * 
 */
package ts.java.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Simple utility methods for dealing with streams. All copy methods use a block size of 4096 bytes.
 * 
 * @author sergouniotis
 * 
 */
public final class StreamUtils {

	public static final int BUFFER_SIZE = 4096;

	private StreamUtils() {

	}

	public static String read(InputStream is) throws IOException {

		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			int next = is.read();
			while (next > -1) {
				bos.write(next);
				next = is.read();
			}
			bos.flush();
			byte[] result = bos.toByteArray();

			return new String(result);
		}
	}

	/*
	 * Read bytes from inputStream and writes to OutputStream, later converts OutputStream to byte array in Java.
	 */
	public static byte[] toByteArray(InputStream is) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

			int reads = is.read();

			while (reads != -1) {
				baos.write(reads);
				reads = is.read();
			}

			return baos.toByteArray();
		}

	}

	/**
	 * Copy the contents of the given InputStream into a new byte array. Leaves the stream open when done.
	 * 
	 * @param in
	 *        the stream to copy from
	 * @return the new byte array that has been copied to
	 * @throws IOException
	 *         in case of I/O errors
	 */
	public static byte[] copyToByteArray(InputStream in) throws IOException {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE)) {
			copy(in, out);
			return out.toByteArray();
		}
	}

	/**
	 * Copy the contents of the given InputStream to the given OutputStream. Leaves both streams open when done.
	 * 
	 * @param in
	 *        the InputStream to copy from
	 * @param out
	 *        the OutputStream to copy to
	 * @return the number of bytes copied
	 * @throws IOException
	 *         in case of I/O errors
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException {
		Assert.notNull(in, "No InputStream specified");
		Assert.notNull(out, "No OutputStream specified");
		int byteCount = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;
		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
			byteCount += bytesRead;
		}
		out.flush();
		return byteCount;
	}

	/**
	 * Copy the contents of the given byte array to the given OutputStream. Leaves the stream open when done.
	 * 
	 * @param in
	 *        the byte array to copy from
	 * @param out
	 *        the OutputStream to copy to
	 * @throws IOException
	 *         in case of I/O errors
	 */
	public static void copy(byte[] in, OutputStream out) throws IOException {
		Assert.notNull(in, "No input byte array specified");
		Assert.notNull(out, "No OutputStream specified");
		out.write(in);
	}

}
