package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class BlockInfoSpawner extends BlockInfo {

	public BlockInfoSpawner(BlockPos pos, IBlockState blockstate, @Nullable NBTTagCompound tileentityData) {
		super(pos, blockstate, tileentityData);
	}

	public BlockInfoSpawner(BlockPos pos, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		super(pos, nbtTagIntArray, blockStatePalette, compoundTagList);
	}

	@Override
	public void generate(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, EDungeonMobType dungeonMob, ProtectedRegion protectedRegion) {
		super.generate(world, dungeonPos, dungeonPartPos, settings, dungeonMob, protectedRegion);
		BlockPos transformedPos = dungeonPartPos.add(Template.transformedBlockPos(settings, this.pos));
		TileEntity tileEntity = world.getTileEntity(transformedPos);

		if (tileEntity instanceof TileEntitySpawner) {
			((TileEntitySpawner) tileEntity).setInDungeon(dungeonPos.getX() >> 4, dungeonPos.getZ() >> 4, dungeonMob);
		} else {
			CQRMain.logger.warn("Failed to place spawner at {}", transformedPos);
		}
	}

	@Override
	public int getId() {
		return SPAWNER_INFO_ID;
	}

}
