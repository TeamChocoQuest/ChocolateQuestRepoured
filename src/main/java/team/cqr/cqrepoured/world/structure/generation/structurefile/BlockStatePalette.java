package team.cqr.cqrepoured.world.structure.generation.structurefile;

import java.util.Iterator;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ObjectIntIdentityMap;

public class BlockStatePalette implements Iterable<IBlockState> {

	public static final IBlockState DEFAULT_BLOCK_STATE = Blocks.AIR.getDefaultState();
	private final ObjectIntIdentityMap<IBlockState> ids = new ObjectIntIdentityMap<>(16);
	private int lastId;

	public BlockStatePalette() {

	}

	public BlockStatePalette(NBTTagList nbtList) {
		nbtList.forEach(nbt -> this.idFor(NBTUtil.readBlockState((NBTTagCompound) nbt)));
	}

	public int idFor(IBlockState state) {
		int i = this.ids.get(state);

		if (i == -1) {
			i = this.lastId++;
			this.ids.put(state, i);
		}

		return i;
	}

	@Nullable
	public IBlockState stateFor(int id) {
		IBlockState iblockstate = this.ids.getByValue(id);
		return iblockstate == null ? DEFAULT_BLOCK_STATE : iblockstate;
	}

	@Override
	public Iterator<IBlockState> iterator() {
		return this.ids.iterator();
	}

	public void addMapping(IBlockState state, int id) {
		this.ids.put(state, id);
	}

	public int size() {
		return this.ids.size();
	}

	public NBTTagList writeToNBT() {
		NBTTagList nbtList = new NBTTagList();
		this.ids.forEach(state -> nbtList.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), (IBlockState) state)));
		return nbtList;
	}

}
