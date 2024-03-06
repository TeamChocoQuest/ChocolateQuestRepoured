package team.cqr.cqrepoured.generation.init;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryBuilder;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.generator.StructureGeneratorType;

@EventBusSubscriber(modid = CQRepoured.MODID, bus = Bus.MOD)
public class CQRStructureGeneratorTypes {

	private static final ResourceKey<Registry<StructureGeneratorType<?>>> REGISTRY_KEY = ResourceKey
			.createRegistryKey(new ResourceLocation(CQRepoured.MODID, "structure_generator"));
	private static final DeferredRegister<StructureGeneratorType<?>> DEFERRED_REGISTER = DeferredRegister.create(REGISTRY_KEY, CQRepoured.MODID);
	public static final Supplier<IForgeRegistry<StructureGeneratorType<?>>> REGISTRY = DEFERRED_REGISTER
			.makeRegistry(() -> new RegistryBuilder<StructureGeneratorType<?>>().disableSaving()
					.disableSync());

	@SubscribeEvent
	public static void main(NewRegistryEvent event) {
		DEFERRED_REGISTER.createRegistry(event);
	}

	@SubscribeEvent
	public static void main(RegisterEvent event) {
		new DeferredRegister.EventDispatcher(DEFERRED_REGISTER).handleEvent(event);
	}

}
