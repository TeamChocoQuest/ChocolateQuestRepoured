package com.teamcqr.chocolatequestrepoured.structureprot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketAddProtectedRegion;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketDeleteProtectedRegion;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ProtectedRegionManager {

	private static final ProtectedRegionManager CLIENT_INSTANCE = new ProtectedRegionManager(null);
	private static final Map<World, ProtectedRegionManager> INSTANCES = Collections.synchronizedMap(new HashMap<>());
	private final Map<UUID, ProtectedRegion> protectedRegions = Collections.synchronizedMap(new HashMap<>());
	private final World world;
	private final File folder;

	public ProtectedRegionManager(World world) {
		this.world = world;
		if (world != null) {
			int dim = world.provider.getDimension();
			if (dim == 0) {
				this.folder = new File(world.getSaveHandler().getWorldDirectory(), "data/CQR/protected_regions");
			} else {
				this.folder = new File(world.getSaveHandler().getWorldDirectory(), "DIM" + dim + "/data/CQR/protected_regions");
			}
		} else {
			this.folder = null;
		}
	}

	@Nullable
	public static ProtectedRegionManager getInstance(World world) {
		if (!world.isRemote) {
			return ProtectedRegionManager.INSTANCES.get(world);
		}
		return ProtectedRegionManager.CLIENT_INSTANCE;
	}

	public static void handleWorldLoad(World world) {
		if (!world.isRemote && !INSTANCES.containsKey(world)) {
			INSTANCES.put(world, new ProtectedRegionManager(world));
			INSTANCES.get(world).loadData();
		}
	}

	public static void handleWorldSave(World world) {
		if (!world.isRemote && INSTANCES.containsKey(world)) {
			INSTANCES.get(world).saveData();
		}
	}

	public static void handleWorldUnload(World world) {
		if (!world.isRemote && INSTANCES.containsKey(world)) {
			INSTANCES.get(world).saveData();
			INSTANCES.remove(world);
		}
	}

	@Nullable
	public ProtectedRegion getProtectedRegion(UUID uuid) {
		return this.protectedRegions.get(uuid);
	}

	public void addProtectedRegion(ProtectedRegion protectedRegion) {
		if (protectedRegion != null) {
			if (this.protectedRegions.containsKey(protectedRegion.getUuid())) {
				CQRMain.logger.warn("Protected region with uuid {} already exists.", protectedRegion.getUuid());
			} else {
				this.protectedRegions.put(protectedRegion.getUuid(), protectedRegion);

				if (this.world != null && !this.world.isRemote) {
					CQRMain.NETWORK.sendToDimension(new SPacketAddProtectedRegion(protectedRegion), this.world.provider.getDimension());
				}
			}
		}
	}

	public void removeProtectedRegion(ProtectedRegion protectedRegion) {
		this.removeProtectedRegion(protectedRegion.getUuid());
	}

	public void removeProtectedRegion(UUID uuid) {
		if (this.protectedRegions.containsKey(uuid)) {
			this.protectedRegions.remove(uuid);

			if (this.world != null && !this.world.isRemote) {
				CQRMain.NETWORK.sendToDimension(new SPacketDeleteProtectedRegion(uuid), this.world.provider.getDimension());
			}
		}
	}

	public List<ProtectedRegion> getProtectedRegions() {
		return new ArrayList<>(this.protectedRegions.values());
	}

	public void clearProtectedRegions() {
		this.protectedRegions.clear();
	}

	private void saveData() {
		if (!this.world.isRemote) {
			if (!this.folder.exists()) {
				this.folder.mkdirs();
			}
			for (File file : FileUtils.listFiles(this.folder, new String[] { "nbt" }, false)) {
				file.delete();
			}
			for (ProtectedRegion protectedRegion : this.protectedRegions.values()) {
				this.createFileFromProtectedRegion(this.folder, protectedRegion);
			}
		}
	}

	private void createFileFromProtectedRegion(File folder, ProtectedRegion protectedRegion) {
		File file = new File(folder, protectedRegion.getUuid().toString() + ".nbt");
		try {
			if (!file.exists() && !file.createNewFile()) {
				throw new FileNotFoundException();
			}
			try (OutputStream outputStream = new FileOutputStream(file)) {
				CompressedStreamTools.writeCompressed(protectedRegion.writeToNBT(), outputStream);
			}
		} catch (IOException e) {
			CQRMain.logger.info(String.format("Failed to save protected region to file: %s", file.getName()), e);
		}
	}

	private void loadData() {
		if (!this.world.isRemote) {
			if (!this.folder.exists()) {
				this.folder.mkdirs();
			}
			this.protectedRegions.clear();
			for (File file : FileUtils.listFiles(this.folder, new String[] { "nbt" }, false)) {
				this.createProtectedRegionFromFile(file);
			}
			List<UUID> keys = new ArrayList<>(this.protectedRegions.keySet());
			for (UUID key : keys) {
				ProtectedRegion protectedRegion = this.protectedRegions.get(key);
				if (protectedRegion == null || !protectedRegion.isValid()) {
					this.protectedRegions.remove(key);
				}
			}
		}
	}

	private void createProtectedRegionFromFile(File file) {
		try (InputStream inputStream = new FileInputStream(file)) {
			NBTTagCompound compound = CompressedStreamTools.readCompressed(inputStream);
			ProtectedRegion protectedRegion = new ProtectedRegion(this.world, compound);
			if (!this.protectedRegions.containsKey(protectedRegion.getUuid())) {
				this.protectedRegions.put(protectedRegion.getUuid(), protectedRegion);
			}
		} catch (IOException e) {
			CQRMain.logger.info(String.format("Failed to load protected region from file: %s", file.getName()), e);
		}
	}

	public List<ProtectedRegion> getProtectedRegionsAt(BlockPos pos) {
		List<ProtectedRegion> list = new ArrayList<>();
		for (ProtectedRegion protectedRegion : this.protectedRegions.values()) {
			if (protectedRegion.isInsideProtectedRegion(pos)) {
				list.add(protectedRegion);
			}
		}
		return list;
	}

}
