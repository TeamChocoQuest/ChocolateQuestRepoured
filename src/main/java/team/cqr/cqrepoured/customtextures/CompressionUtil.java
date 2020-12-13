package team.cqr.cqrepoured.customtextures;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class CompressionUtil {

	public static byte[] encodeFileToBase64(File file) {
		try {
			byte[] fileContent = Files.readAllBytes(file.toPath());
			return Base64.getEncoder().encode(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	public static boolean decodeBase64ToFile(String filePathWithNameAndExtension, byte[] base64) {
		return decodeBase64ToFile(new File(filePathWithNameAndExtension), base64);
	}

	public static boolean decodeBase64ToFile(File targetFile, byte[] base64) {
		byte[] dearr = Base64.getDecoder().decode(base64);
		try (FileOutputStream fos = new FileOutputStream(targetFile)) {
			fos.write(dearr);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
