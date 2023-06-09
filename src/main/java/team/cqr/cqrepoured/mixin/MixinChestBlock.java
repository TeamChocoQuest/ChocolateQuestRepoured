package team.cqr.cqrepoured.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.block.ChestBlock;
import org.spongepowered.asm.mixin.Mixin;
import team.cqr.cqrepoured.block.BlockExporterChest;

@Mixin(ChestBlock.class)
public abstract class MixinChestBlock extends Block {

	public MixinChestBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean is(Block block) {
		if (((Block) (Object) this).getClass() == ChestBlock.class && block instanceof BlockExporterChest) {
			return true;
		}
		return super.is(block);
	}

}
