package team.cqr.cqrepoured.blocks.init;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.blocks.block.BossBlock;
import team.cqr.cqrepoured.blocks.block.CQRTNTBlock;
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
	
	public static final RegistryObject<BossBlock> BOSS = CQRServices.BLOCK.registerBlock("boss", () -> new BossBlock(
			BlockBehaviour.Properties.of()
				.mapColor(MapColor.METAL)
				.instrument(NoteBlockInstrument.IRON_XYLOPHONE)
				.sound(SoundType.METAL)
				.strength(-1.0F, 3600000.0F)
				.noLootTable()
				.isValidSpawn((p_50779_, p_50780_, p_50781_, p_50782_) -> {return false;})
				.noOcclusion()
	)); 
	
	public static final RegistryObject<CQRTNTBlock> CQR_TNT = CQRServices.BLOCK.registerBlock("tnt", () -> new CQRTNTBlock(
			Properties.copy(Blocks.TNT)
	));
	
	
}
