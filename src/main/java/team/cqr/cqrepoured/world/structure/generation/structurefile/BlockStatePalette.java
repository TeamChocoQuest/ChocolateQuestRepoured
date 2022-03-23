package team.cqr.cqrepoured.world.structure.generation.structurefile;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ObjectIntIdentityMap;

import javax.annotation.Nullable;
import java.util.Iterator;

public class BlockStatePalette implements Iterable<BlockState> {

	public static final BlockState DEFAULT_BLOCK_STATE = Blocks.AIR.defaultBlockState();
	private final ObjectIntIdentityMap<BlockState> ids = new ObjectIntIdentityMap<>(16);
	private int lastId;

	public BlockStatePalette() {

	}

	public BlockStatePalette(ListNBT nbtList) {
		nbtList.forEach(nbt -> this.idFor(NBTUtil.readBlockState((CompoundNBT) nbt)));
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

	public ListNBT writeToNBT() {
		ListNBT nbtList = new ListNBT();
		this.ids.forEach(state -> nbtList.add(NBTUtil.writeBlockState((BlockState) state)));
		return nbtList;
	}

}
