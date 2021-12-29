package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.Collection;
import java.util.Collections;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

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

		public Collection<ChunkInfo> values() {
			return Collections.unmodifiableCollection(this.map.values());
		}

		public boolean isEmpty() {
			return this.map.isEmpty();
		}

		public int size() {
			return this.map.size();
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
