package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

public class DecoBlockRotating extends DecoBlockBase {
	protected PropertyDirection directionProperty;
	protected final EnumFacing DEFAULT_SIDE = EnumFacing.NORTH;
	protected EnumFacing initialFacing;

	protected DecoBlockRotating(int x, int y, int z, IBlockState initialState, PropertyDirection directionProperty, EnumFacing initialFacing, BlockStateGenArray.GenerationPhase generationPhase) {
		super(x, y, z, initialState, generationPhase);
		this.directionProperty = directionProperty;
		this.initialFacing = initialFacing;
	}

	protected DecoBlockRotating(Vec3i offset, IBlockState initialState, PropertyDirection directionProperty, EnumFacing initialFacing, BlockStateGenArray.GenerationPhase generationPhase) {
		super(offset, initialState, generationPhase);
		this.directionProperty = directionProperty;
		this.initialFacing = initialFacing;
	}

	@Override
	protected IBlockState getState(EnumFacing side) {
		int rotations = DungeonGenUtils.getCWRotationsBetween(DEFAULT_SIDE, side);
		return blockState.withProperty(directionProperty, DungeonGenUtils.rotateFacingNTimesAboutY(initialFacing, rotations));
	}
}
