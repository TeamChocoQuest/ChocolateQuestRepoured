package team.cqr.cqrepoured.blocks.init;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.blocks.block.SpikeBlock;
import team.cqr.cqrepoured.common.services.CQRServices;

public class CQRBlocksBlocks {

	public static final RegistryObject<SpikeBlock> SPIKES = CQRServices.BLOCK.registerBlock("spikes", () -> new SpikeBlock(
			BlockBehaviour.Properties.of()
				.mapColor(MapColor.METAL)
				.instrument(NoteBlockInstrument.IRON_XYLOPHONE)
				.requiresCorrectToolForDrops()
				.strength(5, 6)
				.sound(SoundType.METAL)
	)); 
	
}
