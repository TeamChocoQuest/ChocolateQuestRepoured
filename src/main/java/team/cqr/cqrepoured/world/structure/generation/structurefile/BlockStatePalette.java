package team.cqr.cqrepoured.world.structure.generation.structurefile;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.ObjectIntIdentityMap;

import javax.annotation.Nullable;
import java.util.Iterator;

public class BlockStatePalette implements Iterable<BlockState> {

	public static final BlockState DEFAULT_BLOCK_STATE = Blocks.AIR.defaultBlockState();
	private final ObjectIntIdentityMap<BlockState> ids = new ObjectIntIdentityMap<>(16);
	private int lastId;

	public BlockStatePalette() {

	}

	public BlockStatePalette(ListTag nbtList) {
		nbtList.forEach(nbt -> this.idFor(NbtUtils.readBlockState((CompoundTag) nbt)));
	}

	public int idFor(BlockState state) {
		int i = this.ids.getId(state);

		if (i == -1) {
			i = this.lastId++;
			this.ids.addMapping(state, i);;
		}

		return i;
	}

	@Nullable
	public BlockState stateFor(int id) {
		BlockState iblockstate = this.ids.byId(id);
		return iblockstate == null ? DEFAULT_BLOCK_STATE : iblockstate;
	}

	@Override
	public Iterator<BlockState> iterator() {
		return this.ids.iterator();
	}

	public void addMapping(BlockState state, int id) {
		this.ids.addMapping(state, id);
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
