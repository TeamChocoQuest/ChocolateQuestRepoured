package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class Structure {

	private final World world;
	private List<List<? extends IStructure>> list = new ArrayList<>();
	private int tick;

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
		} else {
			CQRMain.logger.info("tried to add null");
		}
	}

	private IStructure createFromNBT(NBTTagCompound compound) {
		String name = compound.getString("id");
		if (name.equals("supportHillPart")) {
			return new SupportHillPart(compound);
		} else if (name.equals("structurePart")) {
			return new StructurePart(compound);
		} else if (name.equals("blockStatePart")) {
			return new BlockStatePart(compound);
		} else if (name.equals("blockPart")) {
			return new BlockPart(compound);
		} else if (name.equals("randomBlobPart")) {
			return new RandomBlobPart(compound);
		}
		return null;
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
		NBTTagList nbtTagList1 = compound.getTagList("list", 9);
		this.list = new ArrayList<>(nbtTagList1.tagCount());
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
			}
		}
	}

	public boolean isGenerated() {
		return this.list.isEmpty();
	}

}
