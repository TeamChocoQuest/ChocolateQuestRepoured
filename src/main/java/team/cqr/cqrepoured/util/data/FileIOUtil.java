package team.cqr.cqrepoured.util.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.crash.ReportedException;

public class FileIOUtil {

	private static final FilenameFilter NBT_FILE_FILTER = (dir, name) -> name.endsWith(".nbt");

	public static FilenameFilter getNBTFileFilter() {
		return NBT_FILE_FILTER;
	}

	public static void writeNBTToFile(NBTTagCompound compound, File file) {
		try {
			file.getParentFile().mkdirs();
			try (OutputStream outStream = new FileOutputStream(file)) {
				CompressedStreamTools.writeCompressed(compound, outStream);
			}
		} catch (IOException e) {
			CrashReport crash = new CrashReport("Failed writing NBT to file!", e);
			addFileInfo(crash, file);
			throw new ReportedException(crash);
		}
	}

	public static NBTTagCompound readNBTFromFile(File file) {
		try {
			NBTTagCompound compound;
			try (InputStream in = new FileInputStream(file)) {
				compound = CompressedStreamTools.readCompressed(in);
			}
			return compound;
		} catch (IOException e) {
			CrashReport crash = new CrashReport("Failed reading NBT from file!", e);
			addFileInfo(crash, file);
			throw new ReportedException(crash);
		}
	}

	public static void writePropToFile(Properties prop, File file) {
		try {
			file.getParentFile().mkdirs();
			try (OutputStream out = new FileOutputStream(file)) {
				prop.store(out, null);
			}
		} catch (IOException e) {
			CrashReport crash = new CrashReport("Failed writing Properties to file!", e);
			addFileInfo(crash, file);
			throw new ReportedException(crash);
		}
	}

	public static Properties readPropFromFile(File file) {
		try {
			Properties prop = new Properties();
			try (InputStream in = new FileInputStream(file)) {
				prop.load(in);
			}
			return prop;
		} catch (IOException e) {
			CrashReport crash = new CrashReport("Failed reading Properties from file!", e);
			addFileInfo(crash, file);
			throw new ReportedException(crash);
		}
	}

	private static void addFileInfo(CrashReport crash, File file) {
		CrashReportCategory category = crash.makeCategory("File Info");
		category.addCrashSection("File", file);
	}

}
