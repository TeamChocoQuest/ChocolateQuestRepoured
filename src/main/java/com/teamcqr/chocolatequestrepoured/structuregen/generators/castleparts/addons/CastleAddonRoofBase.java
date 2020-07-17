package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons;

import net.minecraft.util.math.BlockPos;

public abstract class CastleAddonRoofBase implements ICastleAddon {

	final protected BlockPos startPos;
	final protected int sizeX;
	final protected int sizeZ;

	public CastleAddonRoofBase(BlockPos startPos, int sizeX, int sizeZ) {
		this.startPos = startPos;
		this.sizeX = sizeX;
		this.sizeZ = sizeZ;
	}
}
