package team.cqr.cqrepoured.util.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.annotation.Nullable;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;

public class FileIOUtil {

	private static final FilenameFilter NBT_FILE_FILTER = (dir, name) -> name.endsWith(".nbt");

	public static FilenameFilter getNBTFileFilter() {
		return NBT_FILE_FILTER;
	}

	@Nullable
	public static File getWorldRootFolder(IWorld world) {
		File levelDatFile = getDatFileInWorldAtPath(world, "level");
		if(levelDatFile != null) {
			return levelDatFile.getParentFile();
		}
		return null;
	}
	
	@Nullable
	public static File getDatFileInWorldAtPath(IWorld world, String filePath) {
		if(!(world instanceof ServerWorld)) {
			return null;
		}
		if(filePath.endsWith(".dat")) {
			filePath = filePath.substring(0, ".dat".length());
		}
		ServerWorld sw = (ServerWorld) world;
		DimensionSavedDataManager storage = sw.getDataStorage();
		return storage.getDataFile(filePath);
	}
	
	public static void writeNBTToFile(CompoundNBT compound, File file) {
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

	public static CompoundNBT readNBTFromFile(File file) {
		try {
			CompoundNBT compound;
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
		CrashReportCategory category = crash.addCategory("File Info");
		category.setDetail("File", file);
	}

}
