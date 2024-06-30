package team.cqr.cqrepoured.generation.util;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import de.dertoaster.multihitboxlib.util.LazyLoadField;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ListBasedBlock {
	
	protected HolderLookup<Block> holderLookup;
	protected List<String> entries;
	protected final BlockState fallBack;
	protected final LazyLoadField<BlockState> blockState = new LazyLoadField<>(this::blockStateLoad);
	
	public static final Codec<ListBasedBlock> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.STRING.listOf().fieldOf("blocks").forGetter(obj -> obj.entries),
				BlockState.CODEC.optionalFieldOf("fallback", Blocks.AIR.defaultBlockState()).forGetter(obj -> obj.fallBack)
		).apply(instance, ListBasedBlock::new);
	});
	
	private ListBasedBlock(List<String> entries, BlockState fallBack) {
		this.entries = entries;
		this.fallBack = fallBack;
	}
	
	protected BlockState blockStateLoad() {
		if (this.holderLookup != null) {
			for (String s : this.entries) {
				try {
					BlockStateParser.BlockResult blockresult = BlockStateParser.parseForBlock(this.holderLookup, s, true);
					if (blockresult != null && blockresult.blockState() != null) {
						return blockresult.blockState();
					}
				} catch(Exception ex) {
					continue;
				}
			}
		}
		return this.fallBack;
	}
	
	public BlockState getBlock(HolderLookup<Block> holderLookup) {
		if (this.holderLookup != holderLookup) {
			this.holderLookup = holderLookup;
			this.blockState.reset();
		}
		return this.blockState.get();
	}

}
