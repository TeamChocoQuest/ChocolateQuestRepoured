package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitant;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.AbstractBlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockStatePalette;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class DungeonPartBlock extends AbstractDungeonPart {

	protected final Queue<AbstractBlockInfo> blockInfoQueue = new ArrayDeque<>();
	protected PlacementSettings settings;
	protected DungeonInhabitant dungeonMobType;
	protected boolean keepOrder;

	public DungeonPartBlock(World world, DungeonGenerator dungeonGenerator) {
		this(world, dungeonGenerator, BlockPos.ORIGIN, Collections.emptyList(), new PlacementSettings(), DungeonInhabitantManager.DEFAULT_DUNGEON_INHABITANT, false);
	}

	public DungeonPartBlock(World world, DungeonGenerator dungeonGenerator, BlockPos partPos, Collection<AbstractBlockInfo> blocks, PlacementSettings settings, DungeonInhabitant dungeonMobType) {
		this(world, dungeonGenerator, partPos, blocks, settings, dungeonMobType, false);
	}

	/**
	 * @param keepOrder Set to true when you want to keep order of the passed collection or when you want to generate just a few blocks which are far away from each
	 *                  other.
	 */
	public DungeonPartBlock(World world, DungeonGenerator dungeonGenerator, BlockPos partPos, Collection<AbstractBlockInfo> blocks, PlacementSettings settings, DungeonInhabitant dungeonMobType, boolean keepOrder) {
		super(world, dungeonGenerator, partPos);
		this.settings = settings;
		this.dungeonMobType = dungeonMobType;
		this.keepOrder = keepOrder;

		if (!blocks.isEmpty()) {
			List<AbstractBlockInfo> list = Arrays.asList(blocks.stream().filter(Objects::nonNull).toArray(AbstractBlockInfo[]::new));
			if (!list.isEmpty()) {
				if (!keepOrder) {
					list.sort(CQStructure.SORT_FOR_GENERATION);
				}
				this.blockInfoQueue.addAll(list);
				this.calculateMinMaxPos();
			}
		}
	}

	protected void calculateMinMaxPos() {
		if (this.blockInfoQueue.isEmpty()) {
			return;
		}
		AbstractBlockInfo firstBlockInfo = this.blockInfoQueue.iterator().next();
		this.minPos = this.partPos.add(Template.transformedBlockPos(this.settings, firstBlockInfo.getPos()));
		this.maxPos = this.minPos;

		BlockPos.MutableBlockPos p1 = new BlockPos.MutableBlockPos(this.minPos);
		BlockPos.MutableBlockPos p2 = new BlockPos.MutableBlockPos(this.minPos);
		BlockPos.MutableBlockPos p3 = new BlockPos.MutableBlockPos(this.minPos);
		for (AbstractBlockInfo blockInfo : this.blockInfoQueue) {
			this.transformedXYZasMutablePos(this.partPos, blockInfo.getX(), blockInfo.getY(), blockInfo.getZ(), this.settings.getMirror(), this.settings.getRotation(), p1);
			p2.setPos(Math.min(p2.getX(), p1.getX()), Math.min(p2.getY(), p1.getY()), Math.min(p2.getZ(), p1.getZ()));
			p3.setPos(Math.max(p3.getX(), p1.getX()), Math.max(p3.getY(), p1.getY()), Math.max(p3.getZ(), p1.getZ()));
		}
		this.updateMinAndMaxPos(p2);
		this.updateMinAndMaxPos(p3);
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = super.writeToNBT();

		compound.setInteger("mirror", this.settings.getMirror().ordinal());
		compound.setInteger("rotation", this.settings.getRotation().ordinal());
		compound.setString("mob", this.dungeonMobType.getName());
		compound.setBoolean("keepOrder", this.keepOrder);

		BlockPos offset = this.getMinPos(this.blockInfoQueue);
		BlockPos size = this.getMaxPos(this.blockInfoQueue).subtract(offset).add(1, 1, 1);
		compound.setTag("offset", DungeonGenUtils.writePosToList(offset));
		compound.setTag("size", DungeonGenUtils.writePosToList(size));
		BlockStatePalette blockStatePalette = new BlockStatePalette();
		NBTTagList compoundTagList = new NBTTagList();

		// Save normal blocks
		if (!this.keepOrder) {
			AbstractBlockInfo[][][] array = new AbstractBlockInfo[size.getX()][size.getY()][size.getZ()];
			for (AbstractBlockInfo blockInfo : this.blockInfoQueue) {
				array[blockInfo.getX() - offset.getX()][blockInfo.getY() - offset.getY()][blockInfo.getZ() - offset.getZ()] = blockInfo;
			}
			ByteBuf buf = Unpooled.buffer(size.getX() * size.getY() * size.getZ());
			buf.writeInt(this.blockInfoQueue.size());
			for (int x = 0; x < array.length; x++) {
				for (int y = 0; y < array[x].length; y++) {
					for (int z = 0; z < array[x][y].length; z++) {
						AbstractBlockInfo blockInfo = array[x][y][z];
						if (blockInfo != null) {
							blockInfo.writeToByteBuf(buf, blockStatePalette, compoundTagList);
						} else {
							buf.writeByte(255);
						}
					}
				}
			}
			compound.setTag("blockInfoList", new NBTTagByteArray(Arrays.copyOf(buf.array(), buf.writerIndex())));
		} else {
			ByteBuf buf = Unpooled.buffer(this.blockInfoQueue.size() * 8);
			buf.writeInt(this.blockInfoQueue.size());
			for (AbstractBlockInfo blockInfo : this.blockInfoQueue) {
				ByteBufUtils.writeVarInt(buf, blockInfo.getX() - offset.getX(), 5);
				ByteBufUtils.writeVarInt(buf, blockInfo.getY() - offset.getY(), 5);
				ByteBufUtils.writeVarInt(buf, blockInfo.getZ() - offset.getZ(), 5);
				blockInfo.writeToByteBuf(buf, blockStatePalette, compoundTagList);
			}
			compound.setTag("blockInfoList", new NBTTagByteArray(Arrays.copyOf(buf.array(), buf.writerIndex())));
		}

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

		this.blockInfoQueue.clear();
		this.settings = new PlacementSettings();
		this.settings.setMirror(Mirror.values()[compound.getInteger("mirror")]);
		this.settings.setRotation(Rotation.values()[compound.getInteger("rotation")]);
		this.dungeonMobType = DungeonInhabitantManager.instance().getInhabitant(compound.getString("mob"));
		this.keepOrder = compound.getBoolean("keepOrder");

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
		if (!this.keepOrder) {
			ByteBuf buf = Unpooled.wrappedBuffer(compound.getByteArray("blockInfoList"));
			List<AbstractBlockInfo> list = new ArrayList<>(buf.readInt());
			for (int x = 0; x < size.getX(); x++) {
				for (int y = 0; y < size.getY(); y++) {
					for (int z = 0; z < size.getZ(); z++) {
						AbstractBlockInfo blockInfo = AbstractBlockInfo.create(x + offset.getX(), y + offset.getY(), z + offset.getZ(), buf, blockStatePalette, compoundTagList);
						if (blockInfo != null) {
							list.add(blockInfo);
						}
					}
				}
			}
			list.sort(CQStructure.SORT_FOR_GENERATION);
			this.blockInfoQueue.addAll(list);
		} else {
			ByteBuf buf = Unpooled.wrappedBuffer(compound.getByteArray("blockInfoList"));
			int i = buf.readInt();
			for (int j = 0; j < i; j++) {
				int x = ByteBufUtils.readVarInt(buf, 5) + offset.getX();
				int y = ByteBufUtils.readVarInt(buf, 5) + offset.getY();
				int z = ByteBufUtils.readVarInt(buf, 5) + offset.getZ();
				AbstractBlockInfo blockInfo = AbstractBlockInfo.create(x, y, z, buf, blockStatePalette, compoundTagList);
				if (blockInfo != null) {
					this.blockInfoQueue.add(blockInfo);
				}
			}
		}
	}

	@Override
	public String getId() {
		return DUNGEON_PART_BLOCK_ID;
	}

	@Override
	public void generateNext() {
		if (!this.blockInfoQueue.isEmpty()) {
			this.blockInfoQueue.poll().generate(this.world, this.dungeonGenerator.getPos(), this.partPos, this.settings, this.dungeonMobType, this.dungeonGenerator.getProtectedRegion());
		}
	}

	@Override
	public boolean isGenerated() {
		return this.blockInfoQueue.isEmpty();
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
