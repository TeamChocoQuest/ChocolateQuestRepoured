package team.cqr.cqrepoured.util;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class CQRBlockUtil {

	public static final Block[] VANILLA_STONE = {
			Blocks.STONE,
			Blocks.STONEBRICK,
			Blocks.STONE_BRICK_STAIRS,
			Blocks.STONE_SLAB,
			Blocks.STONE_PRESSURE_PLATE,
			Blocks.STONE_SLAB2,
			Blocks.DOUBLE_STONE_SLAB,
			Blocks.DOUBLE_STONE_SLAB2,
			Blocks.COBBLESTONE,
			Blocks.COBBLESTONE_WALL,
			Blocks.MOSSY_COBBLESTONE,
			Blocks.REDSTONE_ORE,
			Blocks.SANDSTONE,
			Blocks.SANDSTONE_STAIRS,
			Blocks.RED_SANDSTONE,
			Blocks.END_STONE,
			Blocks.BEDROCK,
			Blocks.OBSIDIAN,
			Blocks.COAL_ORE,
			Blocks.IRON_ORE,
			Blocks.DIAMOND_ORE,
			Blocks.GOLD_ORE,
			Blocks.LAPIS_ORE,
			Blocks.NETHERRACK };

	public static final Set<Block> VANILLA_STONE_SET = Stream.of(VANILLA_STONE).collect(Collectors.toSet());

	public static final Block[] VANILLA_DIRT = { Blocks.DIRT, Blocks.GRAVEL, Blocks.SOUL_SAND, Blocks.SAND };

	public static final Set<Block> VANILLA_DIRT_SET = Stream.of(VANILLA_DIRT).collect(Collectors.toSet());

	public static final Block[] VANILLA_WOOD = { Blocks.LOG, Blocks.LOG2, Blocks.PLANKS
			// TODO: Add all wood crafted items, but ain't nobody got time for that
	};

	public static final Set<Block> VANILLA_WOOD_SET = Stream.of(VANILLA_WOOD).collect(Collectors.toSet());
}
