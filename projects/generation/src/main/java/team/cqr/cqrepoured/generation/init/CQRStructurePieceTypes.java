package team.cqr.cqrepoured.generation.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType.ContextlessType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;

@EventBusSubscriber(modid = CQRepoured.MODID, bus = Bus.MOD)
public class CQRStructurePieceTypes {

	private static final DeferredRegister<StructurePieceType> REGISTER = DeferredRegister.create(Registries.STRUCTURE_PIECE, CQRepoured.MODID);
	public static final RegistryObject<StructurePieceType> CQR_STRUCTURE_PIECE_TYPE = REGISTER.register("cqr_structure_piece",
			() -> (ContextlessType) GeneratableDungeon::new);

	@SubscribeEvent
	public static void main(RegisterEvent event) {
		new DeferredRegister.EventDispatcher(REGISTER).handleEvent(event);
	}

}
