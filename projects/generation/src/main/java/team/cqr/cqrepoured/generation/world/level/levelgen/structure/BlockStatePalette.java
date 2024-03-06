package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.util.Iterator;

import javax.annotation.Nullable;

import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;
import team.cqr.cqrepoured.util.ObjectIdentityMap;

/**
 * Use {@link SimplePalette} instead.
 */
@Deprecated
public class BlockStatePalette implements Iterable<BlockState> {

	public static final BlockState DEFAULT_BLOCK_STATE = Blocks.AIR.defaultBlockState();
	private final ObjectIdentityMap<BlockState> ids = new ObjectIdentityMap<>(16);
	private int lastId;

	public BlockStatePalette() {

	}

	public BlockStatePalette(HolderGetter<Block> holder, ListTag nbtList) {
		nbtList.forEach(nbt -> this.idFor(NbtUtils.readBlockState(holder, (CompoundTag) nbt)));
	}

	public int idFor(BlockState state) {
		int i = this.ids.get(state);

		if (i == -1) {
			i = this.lastId++;
			this.ids.put(state, i);;
		}

		return i;
	}

	@Nullable
	public BlockState stateFor(int id) {
		BlockState iblockstate = this.ids.getById(id);
		return iblockstate == null ? DEFAULT_BLOCK_STATE : iblockstate;
	}

	@Override
	public Iterator<BlockState> iterator() {
		return this.ids.iterator();
	}

	public void addMapping(BlockState state, int id) {
		this.ids.put(state, id);
	}

	public int size() {
		return this.ids.size();
	}

	public ListTag writeToNBT() {
		ListTag nbtList = new ListTag();
		this.ids.forEach(state -> nbtList.add(NbtUtils.writeBlockState((BlockState) state)));
		return nbtList;
	}

}
