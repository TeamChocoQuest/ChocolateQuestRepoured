package team.cqr.cqrepoured.generation.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.world.structure.CQRStructure;

@EventBusSubscriber(modid = CQRepoured.MODID, bus = Bus.MOD)
public class CQRStructureTypes {

	private static final DeferredRegister<StructureType<?>> REGISTER = DeferredRegister.create(Registries.STRUCTURE_TYPE, CQRepoured.MODID);
	public static final RegistryObject<StructureType<CQRStructure>> CQR_STRUCTURE_TYPE = REGISTER.register("cqr_structure", () -> () -> CQRStructure.CODEC);

	@SubscribeEvent
	public static void main(RegisterEvent event) {
		new DeferredRegister.EventDispatcher(REGISTER).handleEvent(event);
	}

}
