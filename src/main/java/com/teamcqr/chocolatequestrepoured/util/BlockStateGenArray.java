package com.teamcqr.chocolatequestrepoured.util;

import java.util.HashMap;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.EntityDataPart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStateGenArray {
    public enum GenerationPhase {
        MAIN,
        POST
    }

    private Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> mainMap = new HashMap<>();
    private Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> postMap = new HashMap<>();
    private Map<BlockPos, EntityDataPart.ExtendedEntityData> entityMap = new HashMap<>();

    public BlockStateGenArray() {
    }

    public Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> getMainMap() {
        return mainMap;
    }

    public Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> getPostMap()
    {
        return postMap;
    }

    public Map<BlockPos, EntityDataPart.ExtendedEntityData> getEntityMap()
    {
        return entityMap;
    }

    public boolean addChestWithLootTable(World world, BlockPos pos, EnumFacing facing, ResourceLocation lootTable, GenerationPhase phase) {
        if (lootTable != null) {
            Block chestBlock = Blocks.CHEST;
            IBlockState state = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, facing);
            TileEntityChest chest = (TileEntityChest) chestBlock.createTileEntity(world, state);
            if (chest != null) {
                ResourceLocation resLoc = null;
                try {
                    resLoc = lootTable;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (resLoc != null) {
                    long seed = WorldDungeonGenerator.getSeed(world, pos.getX() + pos.getY(), pos.getZ() + pos.getY());
                    chest.setLootTable(resLoc, seed);
                }
                NBTTagCompound nbt = chest.writeToNBT(new NBTTagCompound());
                return this.addBlockState(pos, state, nbt, phase);
            }
        } else {
            System.out.format("Tried to place a chest with a null loot table");
        }

        return false;
    }

    public void addBlockStateMap(Map<BlockPos, IBlockState> map, GenerationPhase phase) {
        for (BlockPos pos : map.keySet()) {
            addBlockState(pos, map.get(pos), phase);
        }
    }

    public void forceAddBlockStateMap(Map<BlockPos, IBlockState> map, GenerationPhase phase) {
        for (BlockPos pos : map.keySet()) {
            forceAddBlockState(pos, map.get(pos), phase);
        }
    }

    public boolean addBlockState(BlockPos pos, IBlockState blockState, GenerationPhase phase) {
        ExtendedBlockStatePart.ExtendedBlockState extState = new ExtendedBlockStatePart.ExtendedBlockState(blockState, new NBTTagCompound());
        return addInternal(phase, pos, extState, false);
    }

    public boolean forceAddBlockState(BlockPos pos, IBlockState blockState, GenerationPhase phase) {
        ExtendedBlockStatePart.ExtendedBlockState extState = new ExtendedBlockStatePart.ExtendedBlockState(blockState, new NBTTagCompound());
        return addInternal(phase, pos, extState, true);
    }

    public boolean addBlockState(BlockPos pos, IBlockState blockState, NBTTagCompound nbt, GenerationPhase phase) {
        ExtendedBlockStatePart.ExtendedBlockState extState = new ExtendedBlockStatePart.ExtendedBlockState(blockState, nbt);
        return addInternal(phase, pos, extState, false);
    }

    public boolean forceAddBlockState(BlockPos pos, IBlockState blockState, NBTTagCompound nbt, GenerationPhase phase) {
        ExtendedBlockStatePart.ExtendedBlockState extState = new ExtendedBlockStatePart.ExtendedBlockState(blockState, nbt);
        return addInternal(phase, pos, extState, true);
    }

    public boolean addEntity(BlockPos pos, ResourceLocation resLoc, NBTTagCompound nbt) {
        EntityDataPart.ExtendedEntityData extEntity = new EntityDataPart.ExtendedEntityData(resLoc, nbt);
        return addInternal(pos, extEntity);
    }

    private boolean addInternal(GenerationPhase phase, BlockPos pos, ExtendedBlockStatePart.ExtendedBlockState extState, boolean overwrite) {
        boolean added = false;
        Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> mapToAdd = getMapFromPhase(phase);

        if (overwrite || !mapToAdd.containsKey(pos)) {
            mapToAdd.put(pos, extState);
            added = true;
        }

        return added;
    }

    private boolean addInternal(BlockPos pos, EntityDataPart.ExtendedEntityData extData) {
        entityMap.put(pos, extData);
        return true;
    }

    private Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> getMapFromPhase(GenerationPhase phase) {
        switch (phase) {
            case POST:
                return postMap;
            case MAIN:
            default:
                return mainMap;

        }
    }
}
