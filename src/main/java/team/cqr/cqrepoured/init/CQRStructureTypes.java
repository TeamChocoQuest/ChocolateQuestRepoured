package team.cqr.cqrepoured.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import team.cqr.cqrepoured.world.structure.StructureCQR;

public class CQRStructureTypes {

	public static final StructureType<StructureCQR> CQR_STRUCTURE_TYPE = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, "cqr_structure_type", () -> StructureCQR.CODEC);

}
