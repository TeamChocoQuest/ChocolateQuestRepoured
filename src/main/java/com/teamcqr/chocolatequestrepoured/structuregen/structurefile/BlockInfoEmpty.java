package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitant;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class BlockInfoEmpty extends AbstractBlockInfo {

	public BlockInfoEmpty(int x, int y, int z) {
		super(x, y, z);
	}

	public BlockInfoEmpty(BlockPos pos) {
		super(pos);
	}

	@Override
	public void generateAt(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion, BlockPos pos) {

	}

	@Override
	public byte getId() {
		return EMPTY_INFO_ID;
	}

	@Override
	protected void writeToByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {

	}

	@Override
	protected void readFromByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {

	}

}
