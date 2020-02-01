package com.teamcqr.chocolatequestrepoured.structuregen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.banners.EBanners;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructurePart;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class DungeonGenerationHandler {

	private static final Map<Integer, DungeonGenerationHandler> instances = new HashMap<Integer, DungeonGenerationHandler>();

	private final List<StructurePart> dungeonPartList = new ArrayList<StructurePart>();
	private final World world;
	private final File file;

	public DungeonGenerationHandler(World world) {
		this.world = world;
		int dim = world.provider.getDimension();
		if (dim == 0) {
			this.file = new File(world.getSaveHandler().getWorldDirectory(), "data\\cqr_structure_parts.nbt");
		} else {
			this.file = new File(world.getSaveHandler().getWorldDirectory(), "DIM" + dim + "\\data\\cqr_structure_parts.nbt");
		}
	}

	public static void addCQStructurePart(World world, CQStructurePart part, PlacementSettings settings, BlockPos pos, int dungeonChunkX, int dungeonChunkZ, DungeonBase dungeon) {
		EDungeonMobType dungeonMobType = dungeon.getDungeonMob();
		if (dungeonMobType == EDungeonMobType.DEFAULT) {
			dungeonMobType = EDungeonMobType.getMobTypeDependingOnDistance(dungeonChunkX, dungeonChunkZ);
		}
		boolean replaceBanners = dungeon.replaceBanners();
		EBanners dungeonBanner = dungeonMobType.getBanner();
		boolean hasShield = dungeon.isProtectedFromModifications();

		DungeonGenerationHandler instance = getInstance(world);
		instance.dungeonPartList.add(new StructurePart(part, settings, pos, dungeonChunkX, dungeonChunkZ, dungeonMobType, replaceBanners, dungeonBanner, hasShield));
	}

	public static void addCQStructurePart(World world, CQStructurePart part, PlacementSettings settings, BlockPos pos, int dungeonChunkX, int dungeonChunkZ, EDungeonMobType dungeonMobType, boolean replaceBanners, EBanners dungeonBanner,
			boolean hasShield) {
		DungeonGenerationHandler instance = getInstance(world);
		instance.dungeonPartList.add(new StructurePart(part, settings, pos, dungeonChunkX, dungeonChunkZ, dungeonMobType, replaceBanners, dungeonBanner, hasShield));
	}

	private static DungeonGenerationHandler getInstance(World world) {
		int dim = world.provider.getDimension();
		if (!instances.containsKey(dim)) {
			createInstance(world);
		}
		return instances.get(dim);
	}

	private static void createInstance(World world) {
		int dim = world.provider.getDimension();
		if (!instances.containsKey(dim)) {
			instances.put(dim, new DungeonGenerationHandler(world));
		}
	}

	private static void deleteInstance(World world) {
		int dim = world.provider.getDimension();
		instances.remove(dim);
	}

	public static void handleWorldLoad(World world) {
		if (!world.isRemote) {
			DungeonGenerationHandler.createInstance(world);
			DungeonGenerationHandler.getInstance(world).loadData();
		}
	}

	public static void handleWorldSave(World world) {
		if (!world.isRemote) {
			DungeonGenerationHandler.getInstance(world).saveData();
		}
	}

	public static void handleWorldUnload(World world) {
		if (!world.isRemote) {
			DungeonGenerationHandler.deleteInstance(world);
		}
	}

	public static void handleWorldTick(World world) {
		if (!world.isRemote) {
			DungeonGenerationHandler.getInstance(world).tick();
		}
	}

	private void loadData() {
		if (this.file.exists()) {
			try {
				InputStream inputStream = new FileInputStream(this.file);
				NBTTagCompound compound = CompressedStreamTools.readCompressed(inputStream);
				this.dungeonPartList.clear();
				this.dungeonPartList.addAll(this.readList(compound.getTagList("parts", 10)));
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		CQRMain.logger.info("Loaded " + this.dungeonPartList.size() + " parts to generate");
	}

	private void saveData() {
		if (!this.file.exists()) {
			try {
				this.file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			OutputStream outputStream = new FileOutputStream(this.file);
			NBTTagCompound compound = new NBTTagCompound();
			compound.setTag("parts", this.writeList(this.dungeonPartList));
			CompressedStreamTools.writeCompressed(compound, outputStream);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		CQRMain.logger.info("Saved " + this.dungeonPartList.size() + " parts to generate");
	}

	private void tick() {
		if (!this.dungeonPartList.isEmpty()) {
			List<Integer> toRemove = new ArrayList<Integer>();

			if (this.world.getTotalWorldTime() % CQRConfig.advanced.dungeonGenerationFrequencyInLoaded == 0) {
				for (int i = 0; i < this.dungeonPartList.size(); i++) {
					StructurePart structurePart = this.dungeonPartList.get(i);
					CQStructurePart part = structurePart.getPart();
					PlacementSettings settings = structurePart.getSettings();
					BlockPos pos = structurePart.getPos();

					if (DungeonGenerationHandler.isAreaLoaded(this.world, pos, part, settings.getRotation())) {
						part.addBlocksToWorld(this.world, pos, settings, structurePart.getDungeonChunkX(), structurePart.getDungeonChunkZ(), structurePart.getDungeonMobType(), structurePart.isReplaceBanners(),
								structurePart.getDungeonBanner(), structurePart.isHasShield());
						toRemove.add(i);
						if (toRemove.size() == CQRConfig.advanced.dungeonGenerationCountInLoaded) {
							break;
						}
					}
				}
			}

			if (this.world.getTotalWorldTime() % CQRConfig.advanced.dungeonGenerationFrequencyInUnloaded == 0) {
				if (toRemove.isEmpty()) {
					for (int i = 0; i < this.dungeonPartList.size(); i++) {
						StructurePart structurePart = this.dungeonPartList.get(i);
						CQStructurePart part = structurePart.getPart();
						PlacementSettings settings = structurePart.getSettings();
						BlockPos pos = structurePart.getPos();

						part.addBlocksToWorld(this.world, pos, settings, structurePart.getDungeonChunkX(), structurePart.getDungeonChunkZ(), structurePart.getDungeonMobType(), structurePart.isReplaceBanners(),
								structurePart.getDungeonBanner(), structurePart.isHasShield());
						toRemove.add(i);
						if (toRemove.size() == CQRConfig.advanced.dungeonGenerationCountInUnloaded) {
							break;
						}
					}
				}
			}

			if (toRemove.size() > 0) {
				CQRMain.logger.info("Forced/Post generating " + toRemove.size() + " structure parts. " + (this.dungeonPartList.size() - toRemove.size()) + " parts left.");
			}

			for (int i = 0; i < toRemove.size(); i++) {
				this.dungeonPartList.remove((int) toRemove.get(i) - i);
			}
		}
	}

	private List<StructurePart> readList(NBTTagList nbtTagList) {
		List<StructurePart> list = new ArrayList<StructurePart>();
		for (int i = 0; i < nbtTagList.tagCount(); i++) {
			list.add(new StructurePart(nbtTagList.getCompoundTagAt(i)));
		}
		return list;
	}

	private NBTTagList writeList(List<StructurePart> list) {
		NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < list.size(); i++) {
			nbtTagList.appendTag(list.get(i).writeToNBT());
		}
		return nbtTagList;
	}

	public static boolean isAreaLoaded(World world, BlockPos pos, CQStructurePart part, Rotation rot) {
		BlockPos size = part.getSize().rotate(rot);
		if (!world.isBlockLoaded(pos)) {
			return false;
		}
		if (!world.isBlockLoaded(pos.add(size.getX(), 0, 0))) {
			return false;
		}
		if (!world.isBlockLoaded(pos.add(0, 0, size.getZ()))) {
			return false;
		}
		if (!world.isBlockLoaded(pos.add(size.getX(), 0, size.getZ()))) {
			return false;
		}
		return true;
	}

}
