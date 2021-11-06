package team.cqr.cqrepoured.structuregen.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.util.NBTCollectors;

public class ChunkInfo {

	private final int chunkX;
	private final int chunkZ;
	private short marked;

	private ChunkInfo(int chunkX, int chunkZ) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}

	private ChunkInfo(ChunkInfo other) {
		this.chunkX = other.chunkX;
		this.chunkZ = other.chunkZ;
		this.marked = other.marked;
	}

	public ChunkInfo(NBTTagIntArray nbtIntArray) {
		int[] data = nbtIntArray.getIntArray();
		this.chunkX = data[0];
		this.chunkZ = data[1];
		this.marked = (short) data[2];
	}

	public NBTTagIntArray writeToNBT() {
		return new NBTTagIntArray(new int[] {
				this.chunkX,
				this.chunkZ,
				this.marked });
	}

	public void markAll(ChunkInfo other) {
		this.marked |= other.marked;
	}

	public void mark(int chunkY) {
		this.marked |= 1 << chunkY;
	}

	public int getMarked() {
		return this.marked;
	}

	public boolean isMarked(int chunkY) {
		return ((this.marked >> chunkY) & 1) == 1;
	}

	public boolean anyMarked() {
		return this.marked != 0;
	}

	public int topMarked() {
		if (!this.anyMarked()) {
			return -1;
		}
		return 15 - (Integer.numberOfLeadingZeros(this.marked & 0xFFFF) - 16);
	}

	public int bottomMarked() {
		if (!this.anyMarked()) {
			return -1;
		}
		return Integer.numberOfTrailingZeros(this.marked & 0xFFFF);
	}

	public void forEach(IntConsumer action) {
		if (!this.anyMarked()) {
			return;
		}
		IntStream.rangeClosed(this.bottomMarked(), this.topMarked()).filter(this::isMarked).forEach(action);
	}

	public void forEachReversed(IntConsumer action) {
		if (!this.anyMarked()) {
			return;
		}
		int start = this.bottomMarked();
		int end = this.topMarked();
		IntStream.rangeClosed(start, end).map(y -> start + end - y).filter(this::isMarked).forEach(action);
	}

	public int getChunkX() {
		return this.chunkX;
	}

	public int getChunkZ() {
		return this.chunkZ;
	}

	public static class ChunkInfoMap {

		protected final Long2ObjectMap<ChunkInfo> map = new Long2ObjectOpenHashMap<>();
		protected final List<ChunkInfo> list = new ArrayList<>();
		protected int minChunkX = Integer.MAX_VALUE;
		protected int minChunkY = Integer.MAX_VALUE;
		protected int minChunkZ = Integer.MAX_VALUE;
		protected int maxChunkX = Integer.MIN_VALUE;
		protected int maxChunkY = Integer.MIN_VALUE;
		protected int maxChunkZ = Integer.MIN_VALUE;

		public ChunkInfoMap() {

		}

		public ChunkInfoMap(ChunkInfoMap other) {
			this.markAll(other);
		}

		public ChunkInfoMap(NBTTagCompound compound) {
			compound.getTagList("chunkInfos", Constants.NBT.TAG_INT_ARRAY).forEach(nbt -> {
				ChunkInfo chunkInfo = new ChunkInfo((NBTTagIntArray) nbt);
				this.map.put(this.getKey(chunkInfo.getChunkX(), chunkInfo.getChunkZ()), chunkInfo);
				this.list.add(chunkInfo);
			});
			this.minChunkX = compound.getInteger("minChunkX");
			this.minChunkY = compound.getInteger("minChunkY");
			this.minChunkZ = compound.getInteger("minChunkZ");
			this.maxChunkX = compound.getInteger("maxChunkX");
			this.maxChunkY = compound.getInteger("maxChunkY");
			this.maxChunkZ = compound.getInteger("maxChunkZ");
		}

		public NBTTagCompound writeToNBT() {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setTag("chunkInfos", this.list.stream().map(ChunkInfo::writeToNBT).collect(NBTCollectors.toList()));
			compound.setInteger("minChunkX", this.minChunkX);
			compound.setInteger("minChunkY", this.minChunkY);
			compound.setInteger("minChunkZ", this.minChunkZ);
			compound.setInteger("maxChunkX", this.maxChunkX);
			compound.setInteger("maxChunkY", this.maxChunkY);
			compound.setInteger("maxChunkZ", this.maxChunkZ);
			return compound;
		}

		private long getKey(int chunkX, int chunkZ) {
			return ((chunkZ & 0xFFFFFFFFL) << 32) | (chunkX & 0xFFFFFFFFL);
		}

		@Nullable
		public ChunkInfo get(int chunkX, int chunkZ) {
			return this.map.get(this.getKey(chunkX, chunkZ));
		}

		public boolean isMarked(int chunkX, int chunkY, int chunkZ) {
			long key = this.getKey(chunkX, chunkZ);
			ChunkInfo chunkInfo = this.map.get(key);
			if (chunkInfo == null) {
				return false;
			}
			return chunkInfo.isMarked(chunkY);
		}

		public void mark(int chunkX, int chunkY, int chunkZ) {
			long key = this.getKey(chunkX, chunkZ);
			ChunkInfo chunkInfo = this.map.get(key);
			if (chunkInfo == null) {
				chunkInfo = new ChunkInfo(chunkX, chunkZ);
				this.map.put(key, chunkInfo);
				this.list.add(chunkInfo);
				if (chunkX < this.minChunkX) {
					this.minChunkX = chunkX;
				}
				if (chunkX > this.maxChunkX) {
					this.maxChunkX = chunkX;
				}
				if (chunkZ < this.minChunkZ) {
					this.minChunkZ = chunkZ;
				}
				if (chunkZ > this.maxChunkZ) {
					this.maxChunkZ = chunkZ;
				}
			}
			chunkInfo.mark(chunkY);
			if (chunkY < this.minChunkY) {
				this.minChunkY = chunkY;
			}
			if (chunkY > this.maxChunkY) {
				this.maxChunkY = chunkY;
			}
		}

		public void markAll(ChunkInfoMap other) {
			other.map.long2ObjectEntrySet().forEach(entry -> {
				long key = entry.getLongKey();
				ChunkInfo chunkInfo = this.map.get(key);
				if (chunkInfo == null) {
					chunkInfo = new ChunkInfo(entry.getValue());
					this.map.put(key, chunkInfo);
					this.list.add(chunkInfo);
					if (chunkInfo.getChunkX() < this.minChunkX) {
						this.minChunkX = chunkInfo.getChunkX();
					}
					if (chunkInfo.getChunkX() > this.maxChunkX) {
						this.maxChunkX = chunkInfo.getChunkX();
					}
					if (chunkInfo.getChunkZ() < this.minChunkZ) {
						this.minChunkZ = chunkInfo.getChunkZ();
					}
					if (chunkInfo.getChunkZ() > this.maxChunkZ) {
						this.maxChunkZ = chunkInfo.getChunkZ();
					}
				} else {
					chunkInfo.markAll(chunkInfo);
				}
				if (chunkInfo.bottomMarked() < this.minChunkY) {
					this.minChunkY = chunkInfo.bottomMarked();
				}
				if (chunkInfo.topMarked() > this.maxChunkY) {
					this.maxChunkY = chunkInfo.topMarked();
				}
			});
		}

		public List<ChunkInfo> values() {
			return Collections.unmodifiableList(this.list);
		}

		public void forEach(Consumer<ChunkInfo> action) {
			this.list.forEach(action);
		}

		public boolean isEmpty() {
			return this.list.isEmpty();
		}

		public int size() {
			return this.list.size();
		}

		public ChunkInfo get(int index) {
			return this.list.get(index);
		}

		public int getMinChunkX() {
			return this.minChunkX;
		}

		public int getMinChunkY() {
			return this.minChunkY;
		}

		public int getMinChunkZ() {
			return this.minChunkZ;
		}

		public int getMaxChunkX() {
			return this.maxChunkX;
		}

		public int getMaxChunkY() {
			return this.maxChunkY;
		}

		public int getMaxChunkZ() {
			return this.maxChunkZ;
		}

	}

}
