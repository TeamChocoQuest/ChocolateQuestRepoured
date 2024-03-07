package team.cqr.cqrepoured.generation.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.common.CQRepoured;

@EventBusSubscriber(modid = CQRepoured.MODID, bus = Bus.MOD)
public class CQRBlocks {

	private static final DeferredRegister<Block> REGISTER = DeferredRegister.create(Registries.BLOCK, CQRepoured.MODID);
	public static final RegistryObject<Block> PLACEHOLDER = REGISTER.register("placeholder", () -> new Block(BlockBehaviour.Properties.of()));

	@SubscribeEvent
	public static void main(RegisterEvent event) {
		new DeferredRegister.EventDispatcher(REGISTER).handleEvent(event);
	}

}
