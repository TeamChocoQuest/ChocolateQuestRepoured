package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Structure {

	private final World world;
	private final List<List<? extends IStructure>> list = new LinkedList<>();
	private int tick;
	private int startX = Integer.MAX_VALUE;
	private int startY = Integer.MAX_VALUE;
	private int startZ = Integer.MAX_VALUE;
	private int endX = Integer.MIN_VALUE;
	private int endY = Integer.MIN_VALUE;
	private int endZ = Integer.MIN_VALUE;

	public Structure(World world, NBTTagCompound compound) {
		this.world = world;
		this.readFromNBT(compound);
	}

	public Structure(World world) {
		this.world = world;
	}

	public void addList(List<? extends IStructure> list) {
		if (list != null && !list.isEmpty()) {
			this.list.add(list);
			for (IStructure istructure : list) {
				BlockPos startPos = istructure.getPos();
				BlockPos endPos = startPos.add(istructure.getSize());
				if (startPos.getX() < startX) {
					startX = startPos.getX();
				} else if (endPos.getX() > endX) {
					endX = endPos.getX();
				}
				if (startPos.getY() < startY) {
					startY = startPos.getY();
				} else if (endPos.getY() > endY) {
					endY = endPos.getY();
				}
				if (startPos.getZ() < startZ) {
					startZ = startPos.getZ();
				} else if (endPos.getZ() > endZ) {
					endZ = endPos.getZ();
				}
			}
		} else {
			CQRMain.logger.warn("Tried to add null or an empty list to structure.");
		}
	}

	private IStructure createFromNBT(NBTTagCompound compound) {
		String name = compound.getString("id");

		switch (name) {
		case "structurePart":
			return new StructurePart(compound);
		case "extendedBlockStatePart":
			return new ExtendedBlockStatePart(compound);
		case "supportHillPart":
			return new SupportHillPart(compound);
		case "randomBlobPart":
			return new RandomBlobPart(compound);
		case "lightPart":
			return new LightPart(compound);
		default:
			return null;
		}
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		NBTTagList nbtTagList1 = new NBTTagList();
		for (List<? extends IStructure> partList : this.list) {
			NBTTagList nbtTagList2 = new NBTTagList();
			for (IStructure istructure : partList) {
				nbtTagList2.appendTag(istructure.writeToNBT());
			}
			nbtTagList1.appendTag(nbtTagList2);
		}
		compound.setTag("list", nbtTagList1);

		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		this.list.clear();
		NBTTagList nbtTagList1 = compound.getTagList("list", 9);
		for (int i = 0; i < nbtTagList1.tagCount(); i++) {
			NBTTagList nbtTagList2 = (NBTTagList) nbtTagList1.get(i);
			List<IStructure> partList = new ArrayList<>(nbtTagList2.tagCount());
			for (int j = 0; j < nbtTagList2.tagCount(); j++) {
				IStructure part = this.createFromNBT(nbtTagList2.getCompoundTagAt(j));
				if (part != null) {
					partList.add(part);
				}
			}
			this.list.add(partList);
		}
	}

	public void tick(World world) {
		this.tick++;

		if (!this.list.isEmpty()) {
			List<? extends IStructure> partList = this.list.get(0);
			List<Integer> toRemove = new ArrayList<>();

			if (this.tick % CQRConfig.advanced.dungeonGenerationFrequencyInLoaded == 0) {
				for (int i = 0; i < partList.size(); i++) {
					IStructure istructure = partList.get(i);

					if (istructure.canGenerate(world)) {
						istructure.generate(world);
						toRemove.add(i);
						if (toRemove.size() == CQRConfig.advanced.dungeonGenerationCountInLoaded) {
							break;
						}
					}
				}
			}

			if (toRemove.isEmpty() && this.tick % CQRConfig.advanced.dungeonGenerationFrequencyInUnloaded == 0) {
				for (int i = 0; i < partList.size(); i++) {
					IStructure istructure = partList.get(i);

					istructure.generate(world);
					toRemove.add(i);
					if (toRemove.size() == CQRConfig.advanced.dungeonGenerationCountInUnloaded) {
						break;
					}
				}
			}

			for (int i = 0; i < toRemove.size(); i++) {
				partList.remove(toRemove.get(i) - i);
			}

			if (partList.isEmpty()) {
				this.list.remove(0);

				if (this.list.size() == 1) {
					CQRMain.logger.info("Started calculating light.");
					for (int x = this.startX; x < this.endX + 16; x += 16) {
						for (int z = this.startZ; z < this.endZ + 16; z += 16) {
							world.getChunkFromChunkCoords(x >> 4, z >> 4).generateSkylightMap();
						}
					}
					CQRMain.logger.info("Finished calculating light.");
				}
			}
		}
	}

	public boolean isGenerated() {
		return this.list.isEmpty();
	}

	public void addLightParts() {
		List<LightPart> lightParts = new ArrayList<>();
		int partSize = 24;
		for (int y = this.startY; y <= this.endY; y += partSize) {
			for (int x = this.startX; x <= this.endX; x += partSize) {
				for (int z = this.startZ; z <= this.endZ; z += partSize) {
					BlockPos pos = new BlockPos(x, y, z);
					lightParts.add(new LightPart(pos, pos.add(partSize - 1, partSize - 1, partSize - 1)));
				}
			}
		}
		this.list.add(lightParts);
		CQRMain.logger.info(lightParts.size() + " light parts");
	}

}
