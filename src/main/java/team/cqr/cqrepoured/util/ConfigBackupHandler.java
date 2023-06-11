package team.cqr.cqrepoured.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

import team.cqr.cqrepoured.CQRMain;

public class ConfigBackupHandler {

	private static final Map<String, String> CONFIG_VERSION_MAP = new HashMap<>();

	public static void registerConfig(String configFile, String configVersion) {
		if (configFile == null) {
			throw new NullPointerException("Argument configFile cannot be null!");
		}
		if (configFile.isEmpty()) {
			throw new IllegalArgumentException("Argument configFile cannot be empty!");
		}
		if (configVersion == null) {
			throw new NullPointerException("Argument configVersion cannot be null!");
		}
		if (configVersion.isEmpty()) {
			throw new IllegalArgumentException("Argument configVersion cannot be empty!");
		}
		if (CONFIG_VERSION_MAP.containsKey(configFile)) {
			throw new IllegalArgumentException("Config is already registered! " + configFile);
		}
		CONFIG_VERSION_MAP.put(configFile, configVersion);
	}

	public static void checkAndBackupConfigs() {
		if (!CQRMain.CQ_CONFIG_FOLDER.exists()) {
			return;
		}

		List<File> filesToBackup = new ArrayList<>();

		File configVersionsFile = new File(CQRMain.CQ_CONFIG_FOLDER, "configVersions.properties");
		try {
			if (!configVersionsFile.exists()) {
				configVersionsFile.createNewFile();
			}

			Properties prop = new Properties();

			try (InputStream in = new FileInputStream(configVersionsFile)) {
				prop.load(in);
			}

			for (Map.Entry<String, String> entry : CONFIG_VERSION_MAP.entrySet()) {
				if (!entry.getValue().equals(prop.get(entry.getKey()))) {
					File file = new File(CQRMain.CQ_CONFIG_FOLDER, entry.getKey());
					if (file.exists()) {
						filesToBackup.add(file);
					}
				}
				prop.setProperty(entry.getKey(), entry.getValue());
			}

			try (OutputStream out = new FileOutputStream(configVersionsFile)) {
				prop.store(out, "Do NOT modify this file unless you know what you are doing!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!filesToBackup.isEmpty()) {
			File backupFolder = new File(CQRMain.CQ_CONFIG_FOLDER, "backup");
			backupFolder.mkdirs();
			LocalDateTime dateTime = LocalDateTime.now();
			String backupFileName = dateTime.toLocalDate() + "_" + dateTime.getHour() + "-" + dateTime.getMinute() + "-" + dateTime.getSecond();
			File backupFile = new File(backupFolder, backupFileName + ".zip");
			if (backupFile.exists()) {
				int i = 1;
				do {
					backupFile = new File(backupFolder, backupFileName + "_" + i++ + ".zip");
				} while (backupFile.exists());
			}
			try {
				if (!backupFile.exists()) {
					backupFile.createNewFile();
				}
				try (ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(backupFile)))) {
					int i = CQRMain.CQ_CONFIG_FOLDER.getAbsolutePath().length() + 1;
					for (File fileToBackup : filesToBackup) {
						if (fileToBackup.isDirectory()) {
							for (File file : FileUtils.listFiles(fileToBackup, null, true)) {
								out.putNextEntry(new ZipEntry(file.getAbsolutePath().substring(i)));

								try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
									int b;
									while ((b = in.read()) != -1) {
										out.write(b);
									}
								}

								out.closeEntry();
							}
							FileUtils.deleteQuietly(fileToBackup);
						} else {
							out.putNextEntry(new ZipEntry(fileToBackup.getAbsolutePath().substring(i)));

							try (InputStream in = new BufferedInputStream(new FileInputStream(fileToBackup))) {
								int b;
								while ((b = in.read()) != -1) {
									out.write(b);
								}
							}

							out.closeEntry();
							FileUtils.deleteQuietly(fileToBackup);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		configVersionsFile.toString();
	}

}
