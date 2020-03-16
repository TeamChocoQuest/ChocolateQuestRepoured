package com.teamcqr.chocolatequestrepoured.util;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class BlockStateGenArray {
    public enum GenerationPhase {
        MAIN,
        POST
    }

    private Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> mainMap = new HashMap<>();
    private Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> postMap = new HashMap<>();

    public BlockStateGenArray() {
    }

    public Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> getMainMap() {
        return mainMap;
    }

    public Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> getPostMap()
    {
        return postMap;
    }

    public boolean add(BlockPos pos, IBlockState blockState, GenerationPhase phase) {
        return addInternal(phase, pos, blockState, false);
    }

    public boolean addOverwrite(BlockPos pos, IBlockState blockState, GenerationPhase phase) {
        return addInternal(phase, pos, blockState, true);
    }

    private boolean addInternal(GenerationPhase phase, BlockPos pos, IBlockState blockState, boolean overwrite) {
        boolean added = false;
        Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> mapToAdd = getMapFromPhase(phase);

        if (overwrite || !mapToAdd.containsKey(pos)) {
            ExtendedBlockStatePart.ExtendedBlockState extState = new ExtendedBlockStatePart.ExtendedBlockState(blockState, new NBTTagCompound());
            mapToAdd.put(pos, extState);
            added = true;
        }

        return added;
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
