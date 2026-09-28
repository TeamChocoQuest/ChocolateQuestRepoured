package team.cqr.cqrepoured.world.structure.generation.structurefile;

import java.util.stream.IntStream;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import team.cqr.cqrepoured.util.NBTCollectors;

public class BlockStatePalette {

	public static class Write {

		private final Object2IntMap<IBlockState> states = new Object2IntLinkedOpenHashMap<>();
		private int lastId;

		public Write() {
			this.states.defaultReturnValue(-1);
		}

		public int idFor(IBlockState state) {
			int i = this.states.getInt(state);

			if (i == -1) {
				i = this.lastId++;
				this.states.put(state, i);
			}

			return i;
		}

		public NBTTagList writeToNBT() {
			return this.states.keySet()
					.stream()
					.map(state -> NBTUtil.writeBlockState(new NBTTagCompound(), state))
					.collect(NBTCollectors.toList());
		}

	}

	public static class Read {

		private static final IBlockState DEFAULT_BLOCK_STATE = Blocks.AIR.getDefaultState();
		private final IBlockState[] states;

		public Read(NBTTagList nbtList) {
			this.states = IntStream.range(0, nbtList.tagCount())
					.mapToObj(nbtList::getCompoundTagAt)
					.map(NBTUtil::readBlockState)
					.toArray(IBlockState[]::new);
		}

		public IBlockState stateFor(int id) {
			return id >= 0 && id < this.states.length ? this.states[id] : DEFAULT_BLOCK_STATE;
		}

	}

}
