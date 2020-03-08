package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import javafx.beans.property.Property;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

public class DecoBlockRotating extends  DecoBlockBase {
    protected PropertyDirection property;
    protected final EnumFacing DEFAULT_SIDE = EnumFacing.NORTH;
    protected EnumFacing initialFacing;

    protected DecoBlockRotating(int x, int y, int z, Block block, PropertyDirection property, EnumFacing initialFacing) {
        super(x, y, z, block);
        this.property = property;
        this.initialFacing = initialFacing;
    }

    protected DecoBlockRotating(Vec3i offset, Block block, PropertyDirection property, EnumFacing initialFacing) {
        super(offset, block);
        this.property = property;
        this.initialFacing = initialFacing;
    }

    protected IBlockState getState(EnumFacing side) {
        int rotations = DungeonGenUtils.getCWRotationsBetween(DEFAULT_SIDE, side);
        return block.getDefaultState().withProperty(property, DungeonGenUtils.rotateFacingNTimesAboutY(initialFacing, rotations));
    }
}
