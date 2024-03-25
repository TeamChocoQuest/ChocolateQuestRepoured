package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockInfoFactories {

	private static final Map<Class<? extends Block>, IBlockInfoFactory<?>> FACTORIES = new Object2ObjectOpenHashMap<>();

	public static void register(Class<? extends Block> blockClass, IBlockInfoFactory<?> factory) {
		if (FACTORIES.containsKey(blockClass)) {
			throw new IllegalArgumentException("Duplicate BlockInfoFactory for class: " + blockClass.getSimpleName());
		}
		FACTORIES.put(blockClass, factory);
	}

	public static <T extends BlockEntity> IBlockInfo create(Level level, BlockPos pos, BlockState blockState) {
		return getFactoryOrThrow(blockState).create(level, pos, blockState);
	}

	private static <T extends BlockEntity> IBlockInfoFactory<T> getFactoryOrThrow(BlockState blockState) {
		return getFactoryOrThrow(blockState.getBlock());
	}

	private static <T extends BlockEntity> IBlockInfoFactory<T> getFactoryOrThrow(Block block) {
		return getFactoryOrThrow(block.getClass());
	}

	private static <T extends BlockEntity> IBlockInfoFactory<T> getFactoryOrThrow(Class<? extends Block> blockClass) {
		IBlockInfoFactory<T> factory = getFactory(blockClass);
		if (factory == null) {
			throw new IllegalArgumentException("No BlockInfoFactory registered for class: " + blockClass.getSimpleName());
		}
		return factory;
	}

	@SuppressWarnings("unchecked")
	private static <T extends BlockEntity> IBlockInfoFactory<T> getFactory(Class<? extends Block> blockClass) {
		IBlockInfoFactory<T> factory = (IBlockInfoFactory<T>) FACTORIES.get(blockClass);
		if (factory == null && blockClass != Block.class) {
			factory = getFactory((Class<? extends Block>) blockClass.getSuperclass());
			FACTORIES.put(blockClass, factory);
		}
		return factory;
	}

}
