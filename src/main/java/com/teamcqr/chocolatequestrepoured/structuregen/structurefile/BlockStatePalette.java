package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import java.util.Iterator;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ObjectIntIdentityMap;

public class BlockStatePalette implements Iterable<IBlockState> {

	public static final IBlockState DEFAULT_BLOCK_STATE = Blocks.AIR.getDefaultState();
	private final ObjectIntIdentityMap<IBlockState> ids;
	private int lastId;

	public BlockStatePalette() {
		this.ids = new ObjectIntIdentityMap<>(16);
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

}
