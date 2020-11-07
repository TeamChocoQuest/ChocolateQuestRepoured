package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import java.util.List;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitant;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
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

	public BlockInfoSpawner(int x, int y, int z, IBlockState blockstate, @Nullable NBTTagCompound tileentityData) {
		super(x, y, z, blockstate, tileentityData);
	}

	public BlockInfoSpawner(BlockPos pos, IBlockState blockstate, Entity... entities) {
		super(pos, blockstate, getNBTTagCompoundFromEntityList(entities));
	}

	public BlockInfoSpawner(int x, int y, int z, IBlockState blockstate, Entity... entities) {
		super(x, y, z, blockstate, getNBTTagCompoundFromEntityList(entities));
	}

	public BlockInfoSpawner(BlockPos pos, IBlockState blockstate, List<Entity> entityList) {
		super(pos, blockstate, getNBTTagCompoundFromEntityList(entityList.toArray(new Entity[entityList.size()])));
	}

	public BlockInfoSpawner(int x, int y, int z, IBlockState blockstate, List<Entity> entityList) {
		super(x, y, z, blockstate, getNBTTagCompoundFromEntityList(entityList.toArray(new Entity[entityList.size()])));
	}

	public BlockInfoSpawner(BlockPos pos, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		super(pos, nbtTagIntArray, blockStatePalette, compoundTagList);
	}

	public BlockInfoSpawner(int x, int y, int z, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		super(x, y, z, nbtTagIntArray, blockStatePalette, compoundTagList);
	}

	@Override
	public void generate(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion) {
		super.generate(world, dungeonPos, dungeonPartPos, settings, dungeonMob, protectedRegion);
		BlockPos transformedPos = dungeonPartPos.add(Template.transformedBlockPos(settings, this.getPos()));
		TileEntity tileEntity = world.getTileEntity(transformedPos);

		if (tileEntity instanceof TileEntitySpawner) {
			((TileEntitySpawner) tileEntity).setInDungeon(dungeonPos.getX() >> 4, dungeonPos.getZ() >> 4, dungeonMob.getName());
		} else {
			CQRMain.logger.warn("Failed to place spawner at {}", transformedPos);
		}
	}

	@Override
	public int getId() {
		return SPAWNER_INFO_ID;
	}

	private static NBTTagCompound getNBTTagCompoundFromEntityList(Entity... entities) {
		TileEntitySpawner tileEntitySpawner = new TileEntitySpawner();
		for (int i = 0; i < entities.length && i < tileEntitySpawner.inventory.getSlots(); i++) {
			if (entities[i] != null) {
				tileEntitySpawner.inventory.setStackInSlot(i, SpawnerFactory.getSoulBottleItemStackForEntity(entities[i]));
			}
		}
		return tileEntitySpawner.writeToNBT(new NBTTagCompound());
	}

}
