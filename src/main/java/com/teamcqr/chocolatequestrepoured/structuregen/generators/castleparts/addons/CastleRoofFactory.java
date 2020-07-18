package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.RandomCastleConfigOptions;

import net.minecraft.util.math.BlockPos;

public class CastleRoofFactory {
    public static CastleAddonRoofBase createRoof(RandomCastleConfigOptions.RoofType type, BlockPos startPos, int sizeX, int sizeZ) {
        switch (type) {
            case TWO_SIDED:
                return new CastleAddonRoofTwoSided(startPos, sizeX, sizeZ);
            case FOUR_SIDED:
            default:
                return new CastleAddonRoofFourSided(startPos, sizeX, sizeZ);
        }
    }
}
