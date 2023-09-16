package team.cqr.cqrepoured.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import team.cqr.cqrepoured.world.structure.CQRStructurePlacement;

public class CQRStructurePlacementTypes {

	public static final StructurePlacementType<CQRStructurePlacement> CQR_STRUCTURE_PLACEMENT_TYPE = Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT,
			"cqr_structure_placement", () -> CQRStructurePlacement.CODEC);

}
