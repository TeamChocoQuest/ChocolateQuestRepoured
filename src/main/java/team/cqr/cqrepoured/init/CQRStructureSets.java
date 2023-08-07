package team.cqr.cqrepoured.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.StructureSets;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRStructurePlacementTypes.EverywhereStructurePlacement;

public class CQRStructureSets {

	public static final ResourceKey<StructureSet> CQR_STRUCTURE_SET = ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(CQRMain.MODID, "cqr_structure_set"));

	/**
	 * TODO call in {@link StructureSets#bootstrap(BootstapContext)} via mixin
	 */
	public static void bootstrap(BootstapContext<StructureSet> context) {
		HolderGetter<Structure> structureLookup = context.lookup(Registries.STRUCTURE);
		context.register(CQR_STRUCTURE_SET, new StructureSet(structureLookup.getOrThrow(CQRStructures.CQR_STRUCTURE), new EverywhereStructurePlacement()));
	}

}
