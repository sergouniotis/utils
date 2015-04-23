package ts.java.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public final class PackageScanner {

	private PackageScanner() {
		// NOTE Auto-generated constructor stub
	}

	public static Set<Class<?>> getClasses(String packageName) throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return getClasses(loader, packageName);
	}

	private static Set<Class<?>> getClasses(ClassLoader loader, String packageName) throws IOException, ClassNotFoundException {
		Set<Class<?>> classes = new HashSet<>();
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = loader.getResources(path);
		if (resources != null) {
			while (resources.hasMoreElements()) {
				String filePath = resources.nextElement().getFile();
				// WINDOWS HACK
				if (filePath.indexOf("%20") > 0) {
					filePath = filePath.replaceAll("%20", " ");
				}
				if (filePath != null) {
					if ((filePath.indexOf('!') > 0) && (filePath.indexOf(".jar") > 0)) {
						String jarPath = filePath.substring(0, filePath.indexOf('!')).substring(filePath.indexOf(':') + 1);
						// WINDOWS HACK
						if (jarPath.indexOf(':') >= 0) {
							jarPath = jarPath.substring(1);
						}
						classes.addAll(getFromJARFile(jarPath, path));
					} else {
						classes.addAll(getFromDirectory(new File(filePath), packageName));
					}
				}
			}
		}
		return classes;
	}

	private static Set<Class<?>> getFromDirectory(File directory, String packageName) throws ClassNotFoundException {
		Set<Class<?>> classes = new HashSet<>();
		if (directory.exists()) {
			for (String file : directory.list()) {
				if (file.endsWith(".class")) {
					String name = packageName + '.' + stripFilenameExtension(file);
					Class<?> clazz = Class.forName(name);
					classes.add(clazz);
				}
			}
		}
		return classes;
	}

	private static Set<Class<?>> getFromJARFile(String jar, String packageName) throws FileNotFoundException, IOException,
			ClassNotFoundException {
		Set<Class<?>> classes = new HashSet<>();
		InputStream instream = null;
		JarInputStream jarFile = null;
		try {
			instream = new FileInputStream(jar);
			jarFile = new JarInputStream(instream);
			JarEntry jarEntry = null;
			do {
				jarEntry = jarFile.getNextJarEntry();
				if (jarEntry != null) {
					String className = jarEntry.getName();
					if (className.endsWith(".class")) {
						className = stripFilenameExtension(className);
						if (className.startsWith(packageName)) {
							classes.add(Class.forName(className.replace('/', '.')));
						}
					}
				}
			}
			while (jarEntry != null);
		}
		finally {
			if (jarFile != null) {
				jarFile.close();
			}
			if (instream != null) {
				instream.close();
			}
		}
		return classes;
	}

	private static String stripFilenameExtension(String str) {
		return str.substring(0, str.length() - 6);
	}

}
