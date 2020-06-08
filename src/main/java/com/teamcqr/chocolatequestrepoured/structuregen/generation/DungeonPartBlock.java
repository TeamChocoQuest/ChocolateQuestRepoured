package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.AbstractBlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockStatePalette;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.util.Constants;

public class DungeonPartBlock extends AbstractDungeonPart {

	protected final Deque<AbstractBlockInfo> blockInfoList = new LinkedList<>();
	protected PlacementSettings settings;
	protected EDungeonMobType dungeonMobType;

	public DungeonPartBlock(World world, DungeonGenerator dungeonGenerator) {
		this(world, dungeonGenerator, BlockPos.ORIGIN, Collections.emptyList(), new PlacementSettings(), EDungeonMobType.DEFAULT);
	}

	public DungeonPartBlock(World world, DungeonGenerator dungeonGenerator, BlockPos partPos, Collection<AbstractBlockInfo> blocks, PlacementSettings settings, EDungeonMobType dungeonMobType) {
		super(world, dungeonGenerator, partPos);
		for (AbstractBlockInfo blockInfo : blocks) {
			if (blockInfo != null) {
				this.updateMinAndMaxPos(partPos.add(Template.transformedBlockPos(settings, blockInfo.getPos())));
				this.blockInfoList.add(blockInfo);
			}
		}
		this.settings = settings;
		this.dungeonMobType = dungeonMobType;
		if (this.dungeonMobType == EDungeonMobType.DEFAULT) {
			this.dungeonMobType = EDungeonMobType.getMobTypeDependingOnDistance(world, partPos.getX(), partPos.getZ());
			CQRMain.logger.warn("Created dungeon part block with mob type default at {}", partPos);
		}
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = super.writeToNBT();

		compound.setInteger("mirror", this.settings.getMirror().ordinal());
		compound.setInteger("rotation", this.settings.getRotation().ordinal());
		compound.setInteger("mob", this.dungeonMobType.ordinal());

		BlockPos offset = this.getMinPos(this.blockInfoList);
		BlockPos size = this.getMaxPos(this.blockInfoList).subtract(offset).add(1, 1, 1);
		compound.setTag("offset", DungeonGenUtils.writePosToList(offset));
		compound.setTag("size", DungeonGenUtils.writePosToList(size));
		BlockStatePalette blockStatePalette = new BlockStatePalette();
		NBTTagList compoundTagList = new NBTTagList();

		// Save normal blocks
		NBTTagList nbtTagList1 = new NBTTagList();
		NBTTagIntArray emptyNbtTagIntArray = new NBTTagIntArray(new int[0]);
		for (int i = 0; i < size.getX() * size.getY() * size.getZ(); i++) {
			nbtTagList1.appendTag(emptyNbtTagIntArray);
		}
		for (AbstractBlockInfo blockInfo : this.blockInfoList) {
			BlockPos pos = blockInfo.getPos().subtract(offset);
			int index = pos.getX() + pos.getY() * size.getX() + pos.getZ() * size.getX() * size.getY();
			nbtTagList1.set(index, blockInfo.writeToNBT(blockStatePalette, compoundTagList));
		}
		compound.setTag("blockInfoList", nbtTagList1);

		// Save block states
		NBTTagList nbtTagList4 = new NBTTagList();
		for (IBlockState state : blockStatePalette) {
			nbtTagList4.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), state));
		}
		compound.setTag("palette", nbtTagList4);

		// Save compound tags
		compound.setTag("compoundTagList", compoundTagList);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		this.blockInfoList.clear();
		this.settings = new PlacementSettings();
		this.settings.setMirror(Mirror.values()[compound.getInteger("mirror")]);
		this.settings.setRotation(Rotation.values()[compound.getInteger("rotation")]);
		this.dungeonMobType = EDungeonMobType.values()[compound.getInteger("mob")];

		BlockPos offset = DungeonGenUtils.readPosFromList(compound.getTagList("offset", Constants.NBT.TAG_INT));
		BlockPos size = DungeonGenUtils.readPosFromList(compound.getTagList("size", Constants.NBT.TAG_INT));
		BlockStatePalette blockStatePalette = new BlockStatePalette();

		// Load compound tags
		NBTTagList compoundTagList = compound.getTagList("compoundTagList", Constants.NBT.TAG_COMPOUND);

		// Load block states
		int blockStateIndex = 0;
		for (NBTBase nbt : compound.getTagList("palette", Constants.NBT.TAG_COMPOUND)) {
			blockStatePalette.addMapping(NBTUtil.readBlockState((NBTTagCompound) nbt), blockStateIndex++);
		}

		// Load normal blocks
		int x = 0;
		int y = 0;
		int z = 0;
		for (NBTBase nbt : compound.getTagList("blockInfoList", Constants.NBT.TAG_INT_ARRAY)) {
			AbstractBlockInfo blockInfo = AbstractBlockInfo.create(offset.add(x, y, z), (NBTTagIntArray) nbt, blockStatePalette, compoundTagList);
			if (blockInfo != null) {
				this.blockInfoList.add(blockInfo);
			}
			if (x < size.getX() - 1) {
				x++;
			} else if (y < size.getY() - 1) {
				x = 0;
				y++;
			} else if (z < size.getZ() - 1) {
				x = 0;
				y = 0;
				z++;
			}
		}
	}

	@Override
	public String getId() {
		return DUNGEON_PART_BLOCK_ID;
	}

	@Override
	public void generateNext() {
		if (!this.blockInfoList.isEmpty()) {
			AbstractBlockInfo blockInfo = this.blockInfoList.removeFirst();
			blockInfo.generate(this.world, this.dungeonGenerator.getPos(), this.partPos, this.settings, this.dungeonMobType, this.dungeonGenerator.getProtectedRegion());
		}
	}

	@Override
	public boolean isGenerated() {
		return this.blockInfoList.isEmpty();
	}

	private BlockPos getMinPos(Collection<AbstractBlockInfo> collection) {
		if (collection.isEmpty()) {
			return BlockPos.ORIGIN;
		}
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minZ = Integer.MAX_VALUE;
		for (AbstractBlockInfo blockInfo : collection) {
			BlockPos pos = blockInfo.getPos();
			if (pos.getX() < minX) {
				minX = pos.getX();
			}
			if (pos.getY() < minY) {
				minY = pos.getY();
			}
			if (pos.getZ() < minZ) {
				minZ = pos.getZ();
			}
		}
		return new BlockPos(minX, minY, minZ);
	}

	private BlockPos getMaxPos(Collection<AbstractBlockInfo> collection) {
		if (collection.isEmpty()) {
			return BlockPos.ORIGIN;
		}
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int maxZ = Integer.MIN_VALUE;
		for (AbstractBlockInfo blockInfo : collection) {
			BlockPos pos = blockInfo.getPos();
			if (pos.getX() > maxX) {
				maxX = pos.getX();
			}
			if (pos.getY() > maxY) {
				maxY = pos.getY();
			}
			if (pos.getZ() > maxZ) {
				maxZ = pos.getZ();
			}
		}
		return new BlockPos(maxX, maxY, maxZ);
	}

}
