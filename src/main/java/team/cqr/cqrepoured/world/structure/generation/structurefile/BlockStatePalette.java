package team.cqr.cqrepoured.world.structure.generation.structurefile;

import java.util.Iterator;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ObjectIntIdentityMap;

public class BlockStatePalette implements Iterable<BlockState> {

	public static final BlockState DEFAULT_BLOCK_STATE = Blocks.AIR.getDefaultState();
	private final ObjectIntIdentityMap<BlockState> ids = new ObjectIntIdentityMap<>(16);
	private int lastId;

	public BlockStatePalette() {

	}

	public BlockStatePalette(ListNBT nbtList) {
		nbtList.forEach(nbt -> this.idFor(NBTUtil.readBlockState((CompoundNBT) nbt)));
	}

	public int idFor(BlockState state) {
		int i = this.ids.get(state);

		if (i == -1) {
			i = this.lastId++;
			this.ids.put(state, i);
		}

		return i;
	}

	@Nullable
	public BlockState stateFor(int id) {
		BlockState iblockstate = this.ids.getByValue(id);
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

	public ListNBT writeToNBT() {
		ListNBT nbtList = new ListNBT();
		this.ids.forEach(state -> nbtList.appendTag(NBTUtil.writeBlockState(new CompoundNBT(), (BlockState) state)));
		return nbtList;
	}

}
