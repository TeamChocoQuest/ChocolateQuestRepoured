package com.teamcqr.chocolatequestrepoured.customtextures;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class CompressionUtil {

	public static String encodeFileToBase64(File file) {
		try {
			byte[] fileContent = Files.readAllBytes(file.toPath());
			return Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			throw new IllegalStateException("could not read file " + file, e);
		}
	}

	public static boolean decodeBase64ToFile(String filePathWithNameAndExtension, String base64) {
		return decodeBase64ToFile(new File(filePathWithNameAndExtension), base64);
	}

	public static boolean decodeBase64ToFile(File targetFile, String base64) {
		byte dearr[] = Base64.getDecoder().decode(base64);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(targetFile);
			fos.write(dearr);
			fos.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
