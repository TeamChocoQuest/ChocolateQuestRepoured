package team.cqr.cqrepoured.common.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerLevel;

public class FileIOUtil {

	public static FilenameFilter getNBTFileFilter() {
		return (dir, name) -> name.endsWith(".nbt");
	}

	public static void writeNBT(File file, CompoundTag nbt) {
		writeNBT(file.toPath(), nbt);
	}

	public static CompoundTag readNBT(File file) {
		return readNBT(file.toPath());
	}

	public static void writeProperties(File file, Properties properties) {
		writeProperties(file.toPath(), properties);
	}

	public static Properties readProperties(File file) {
		return readProperties(file.toPath());
	}

	public static void writeBufferedData(File file, IOConsumer<DataOutputStream> writer) {
		writeBufferedData(file.toPath(), writer);
	}

	public static void readBufferedData(File file, IOConsumer<DataInputStream> reader) {
		readBufferedData(file.toPath(), reader);
	}

	public static <R> R readBufferedData(File file, IOFunction<DataInputStream, R> reader) {
		return readBufferedData(file.toPath(), reader);
	}

	public static void writeBuffered(File file, IOConsumer<BufferedOutputStream> writer) {
		writeBuffered(file.toPath(), writer);
	}

	public static void readBuffered(File file, IOConsumer<BufferedInputStream> reader) {
		readBuffered(file.toPath(), reader);
	}

	public static <R> R readBuffered(File file, IOFunction<BufferedInputStream, R> reader) {
		return readBuffered(file.toPath(), reader);
	}

	public static void write(File file, IOConsumer<OutputStream> writer) {
		write(file.toPath(), writer);
	}

	public static void read(File file, IOConsumer<InputStream> reader) {
		read(file.toPath(), reader);
	}

	public static <R> R read(File file, IOFunction<InputStream, R> reader) {
		return read(file.toPath(), reader);
	}

	public static void writeNBT(Path file, CompoundTag nbt) {
		write(file, out -> NbtIo.writeCompressed(nbt, out));
	}

	public static CompoundTag readNBT(Path file) {
		return read(file, (IOFunction<InputStream, CompoundTag>) NbtIo::readCompressed);
	}

	public static void writeProperties(Path file, Properties properties) {
		writeBuffered(file, out -> properties.store(out, null));
	}

	public static Properties readProperties(Path file) {
		Properties properties = new Properties();
		readBuffered(file, (IOConsumer<BufferedInputStream>) properties::load);
		return properties;
	}

	public static void writeBufferedData(Path file, IOConsumer<DataOutputStream> writer) {
		writeBuffered(file, writer.compose(DataOutputStream::new));
	}

	public static void readBufferedData(Path file, IOConsumer<DataInputStream> reader) {
		readBuffered(file, reader.compose(DataInputStream::new));
	}

	public static <R> R readBufferedData(Path file, IOFunction<DataInputStream, R> reader) {
		return readBuffered(file, reader.compose(DataInputStream::new));
	}

	public static void writeBuffered(Path file, IOConsumer<BufferedOutputStream> writer) {
		write(file, writer.compose(BufferedOutputStream::new));
	}

	public static void readBuffered(Path file, IOConsumer<BufferedInputStream> reader) {
		read(file, reader.compose(BufferedInputStream::new));
	}

	public static <R> R readBuffered(Path file, IOFunction<BufferedInputStream, R> reader) {
		return read(file, reader.compose(BufferedInputStream::new));
	}

	public static void write(Path file, IOConsumer<OutputStream> writer) {
		try {
			Files.createDirectories(file.getParent());
			try (OutputStream out = Files.newOutputStream(file)) {
				writer.accept(out);
			}
		} catch (IOException e) {
			CrashReport crash = new CrashReport("Failed writing to file!", e);
			CrashReportCategory category = crash.addCategory("File Info");
			category.setDetail("File", file);
			throw new ReportedException(crash);
		}
	}

	public static void read(Path file, IOConsumer<InputStream> reader) {
		read(file, in -> {
			reader.accept(in);
			return null;
		});
	}

	public static <R> R read(Path file, IOFunction<InputStream, R> reader) {
		try (InputStream in = Files.newInputStream(file)) {
			return reader.apply(in);
		} catch (IOException e) {
			CrashReport crash = new CrashReport("Failed reading from file!", e);
			CrashReportCategory category = crash.addCategory("File Info");
			category.setDetail("File", file);
			throw new ReportedException(crash);
		}
	}

	public static <K, V> void forEach(Map<K, V> map, IOBiConsumer<K, V> action) throws IOException {
		for (Map.Entry<K, V> entry : map.entrySet()) {
			action.accept(entry.getKey(), entry.getValue());
		}
	}

	public static <T> void forEach(Collection<T> collection, IOConsumer<T> action) throws IOException {
		for (T t : collection) {
			action.accept(t);
		}
	}

}
