package team.cqr.cqrepoured.util;

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

import net.minecraftforge.fml.loading.FMLLoader;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.CQRMain;

public class CopyHelper {

	/**
	 * Small utility by meldexun to extract a folder from a jar
	 */
	public static void copyFromJarOrWorkspace(String sourceName, Path target, boolean overrideExisitingFiles) {
		try {
			if (!overrideExisitingFiles && Files.exists(target)) {
				return;
			}

			Path source;

			URL url = CQRMain.class.getResource("");
			CQRMain.logger.debug("URI resource: {}", url.toString());
			if (url.getProtocol().equals("jar")) {
				JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
				try (FileSystem fileSystem = FileSystems.newFileSystem(Paths.get(jarURLConnection.getJarFileURL().toURI()), CQRMain.class.getClassLoader())) {
					source = fileSystem.getPath(sourceName);
					copyFiles(source, target);
				}
			} else if (url.getProtocol().equals("modjar")) {
				source = FMLLoader.getLoadingModList().getModFileById(CQRConstants.MODID).getFile().findResource(sourceName);
				copyFiles(source, target);
			} else {
				URL resource = CQRMain.class.getResource(sourceName);
				if (resource != null) {
					CQRMain.logger.debug("URI resource: {}", resource.toString());
					source = Paths.get(resource.toURI());
					copyFiles(source, target);
				}
			}
		} catch (IOException | URISyntaxException e) {
			CQRMain.logger.error("Failed to copy file(s) from {} to {}", sourceName, target);
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
