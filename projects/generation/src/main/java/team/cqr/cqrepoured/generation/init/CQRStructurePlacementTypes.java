package team.cqr.cqrepoured.generation.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.placement.CQRStructurePlacement;

@EventBusSubscriber(modid = CQRepoured.MODID, bus = Bus.MOD)
public class CQRStructurePlacementTypes {

	private static final DeferredRegister<StructurePlacementType<?>> REGISTER = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, CQRepoured.MODID);
	public static final RegistryObject<StructurePlacementType<CQRStructurePlacement>> CQR_STRUCTURE_PLACEMENT_TYPE = REGISTER
			.register("cqr_structure_placement", () -> () -> CQRStructurePlacement.CODEC);

	@SubscribeEvent
	public static void main(RegisterEvent event) {
		new DeferredRegister.EventDispatcher(REGISTER).handleEvent(event);
	}

}
