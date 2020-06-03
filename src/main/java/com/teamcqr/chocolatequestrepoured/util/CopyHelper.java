package com.teamcqr.chocolatequestrepoured.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import com.teamcqr.chocolatequestrepoured.CQRMain;

public class CopyHelper {
	/*
	 * Small utility by meldexun to extract a folder from a jar
	 */
	public static void copyFromJar(String source, Path target) throws URISyntaxException, IOException {
		Path path;

		URL url = CQRMain.class.getResource("");
		if (url.getProtocol().equals("jar")) {
			JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
			try (FileSystem fileSystem = FileSystems.newFileSystem(Paths.get(jarURLConnection.getJarFileURL().toURI()), CQRMain.class.getClassLoader())) {
				path = fileSystem.getPath(source);
				copyFiles(path, target);
			}
		} else {
			path = new File(CQRMain.class.getResource(source).toURI()).toPath();
			copyFiles(path, target);
		}
	}

	private static void copyFiles(Path source, Path target) throws IOException {
		Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				Files.createDirectories(target.resolve(source.relativize(dir).toString()));
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.copy(file, target.resolve(source.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}

		});
	}

}
