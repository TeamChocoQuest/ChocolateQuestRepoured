package team.cqr.cqrepoured.structuregen.generation.part;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.structuregen.generation.DungeonPlacement;
import team.cqr.cqrepoured.structuregen.generation.GeneratableDungeon;
import team.cqr.cqrepoured.structuregen.generation.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.structuregen.generation.generatable.IGeneratable;
import team.cqr.cqrepoured.structuregen.generation.part.DungeonPart.Registry.ISerializer;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparablePosInfo;
import team.cqr.cqrepoured.structuregen.structurefile.BlockStatePalette;
import team.cqr.cqrepoured.util.BlockPlacingHelper;

public class BlockDungeonPart extends DungeonPart implements IProtectable {

	protected final Queue<GeneratableChunkInfo> chunks;

	protected BlockDungeonPart(Collection<GeneratableChunkInfo> chunks) {
		this.chunks = new ArrayDeque<>(chunks);
	}

	@Override
	public void generate(World world, GeneratableDungeon dungeon) {
		if (!this.chunks.isEmpty()) {
			this.chunks.remove().generate(world, dungeon);
		}
	}

	@Override
	public boolean isGenerated() {
		return this.chunks.isEmpty();
	}

	public Collection<GeneratableChunkInfo> getChunks() {
		return Collections.unmodifiableCollection(this.chunks);
	}

	@Override
	public BlockPos minPos() {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minZ = Integer.MAX_VALUE;
		for (GeneratableChunkInfo chunk : this.chunks) {
			for (GeneratablePosInfo block : chunk.blocks) {
				minX = Math.min(block.getX(), minX);
				minY = Math.min(block.getY(), minY);
				minZ = Math.min(block.getZ(), minZ);
			}
		}
		return new BlockPos(minX, minY, minZ);
	}

	@Override
	public BlockPos maxPos() {
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int maxZ = Integer.MIN_VALUE;
		for (GeneratableChunkInfo chunk : this.chunks) {
			for (GeneratablePosInfo block : chunk.blocks) {
				maxX = Math.max(block.getX(), maxX);
				maxY = Math.max(block.getY(), maxY);
				maxZ = Math.max(block.getZ(), maxZ);
			}
		}
		return new BlockPos(maxX, maxY, maxZ);
	}

	public static class GeneratableChunkInfo implements IGeneratable {

		private final int chunkX;
		private final int chunkY;
		private final int chunkZ;
		private final List<GeneratablePosInfo> blocks;

		protected GeneratableChunkInfo(int chunkX, int chunkY, int chunkZ, Collection<GeneratablePosInfo> blocks) {
			this.chunkX = chunkX;
			this.chunkY = chunkY;
			this.chunkZ = chunkZ;
			this.blocks = new ArrayList<>(blocks);
		}

		@Override
		public void generate(World world, GeneratableDungeon dungeon) {
			BlockPlacingHelper.setBlockStates(world, this.chunkX, this.chunkY, this.chunkZ, dungeon, (world1, chunk, blockStorage, dungeon1) -> {
				boolean flag = false;
				for (GeneratablePosInfo generatable : this.blocks) {
					flag |= generatable.place(world1, chunk, blockStorage, dungeon1);
				}
				if (flag) {
					dungeon1.mark(this.chunkX, this.chunkY, this.chunkZ);
				}
				return flag;
			});
			this.blocks.clear();
		}

		public int getChunkX() {
			return this.chunkX;
		}

		public int getChunkY() {
			return this.chunkY;
		}

		public int getChunkZ() {
			return this.chunkZ;
		}

		public Collection<GeneratablePosInfo> getBlocks() {
			return Collections.unmodifiableCollection(this.blocks);
		}

	}

	public static class Builder implements IDungeonPartBuilder {

		@SuppressWarnings("unused")
		private static final Comparator<GeneratablePosInfo> VANILLA_COMPARATOR = (g1, g2) -> {
			if (g1.hasTileEntity()) {
				if (g2.hasTileEntity()) {
					// return 0;
				} else {
					return g2.hasSpecialShape() ? -1 : 1;
				}
			} else {
				if (g2.hasTileEntity()) {
					return g1.hasSpecialShape() ? 1 : -1;
				} else {
					if (g1.hasSpecialShape()) {
						// return g2.hasSpecialShape() ? 0 : 1;
						if (!g2.hasSpecialShape()) {
							return 1;
						}
					} else {
						// return g2.hasSpecialShape() ? -1 : 0;
						if (g2.hasSpecialShape()) {
							return -1;
						}
					}
				}
			}

			if (g1.getY() < g2.getY()) {
				return -1;
			}
			if (g1.getY() > g2.getY()) {
				return 1;
			}
			if (g1.getX() < g2.getX()) {
				return -1;
			}
			if (g1.getX() > g2.getX()) {
				return 1;
			}
			if (g1.getZ() < g2.getZ()) {
				return -1;
			}
			if (g1.getZ() > g2.getZ()) {
				return 1;
			}
			return 0;
		};

		private static final Comparator<GeneratablePosInfo> CQR_COMPARATOR = (g1, g2) -> {
			if (g1.getChunkY() < g2.getChunkY()) {
				return -1;
			}
			if (g1.getChunkY() > g2.getChunkY()) {
				return 1;
			}
			if (g1.getChunkX() < g2.getChunkX()) {
				return -1;
			}
			if (g1.getChunkX() > g2.getChunkX()) {
				return 1;
			}
			if (g1.getChunkZ() < g2.getChunkZ()) {
				return -1;
			}
			if (g1.getChunkZ() > g2.getChunkZ()) {
				return 1;
			}

			if (g1.getY() < g2.getY()) {
				return -1;
			}
			if (g1.getY() > g2.getY()) {
				return 1;
			}
			if (g1.getX() < g2.getX()) {
				return -1;
			}
			if (g1.getX() > g2.getX()) {
				return 1;
			}
			if (g1.getZ() < g2.getZ()) {
				return -1;
			}
			if (g1.getZ() > g2.getZ()) {
				return 1;
			}
			return 0;
		};

		private final List<PreparablePosInfo> blocks = new ArrayList<>();

		public Builder add(PreparablePosInfo block) {
			this.blocks.add(block);
			return this;
		}

		public Builder addAll(Collection<? extends PreparablePosInfo> blocks) {
			this.blocks.addAll(blocks);
			return this;
		}

		@Override
		public BlockDungeonPart build(World world, DungeonPlacement placement) {
			List<GeneratablePosInfo> list = this.blocks.stream().map(preparable -> preparable.prepare(world, placement)).filter(Objects::nonNull)
					.collect(Collectors.toList());
			list.sort(CQR_COMPARATOR);
			List<GeneratableChunkInfo> list1 = new ArrayList<>();

			for (int i = 0; i < list.size(); i++) {
				List<GeneratablePosInfo> list2 = new ArrayList<>();
				GeneratablePosInfo first = list.get(i);
				list2.add(first);

				while (i < list.size() - 1) {
					GeneratablePosInfo next = list.get(i + 1);
					if (first.getChunkX() != next.getChunkX() || first.getChunkY() != next.getChunkY() || first.getChunkZ() != next.getChunkZ()) {
						break;
					}
					list2.add(next);
					i++;
				}

				list1.add(new GeneratableChunkInfo(first.getChunkX(), first.getChunkY(), first.getChunkZ(), list2));
			}

			return new BlockDungeonPart(list1);
		}

	}

	public static class Serializer implements ISerializer<BlockDungeonPart> {

		@Override
		public NBTTagCompound write(BlockDungeonPart part, NBTTagCompound compound) {
			ByteBuf buf = Unpooled.buffer();
			BlockStatePalette palette = new BlockStatePalette();
			NBTTagList compoundList = new NBTTagList();
			buf.writeInt(part.chunks.size());
			part.chunks.forEach(chunkInfo -> {
				buf.writeInt(chunkInfo.chunkX);
				buf.writeInt(chunkInfo.chunkY);
				buf.writeInt(chunkInfo.chunkZ);
				GeneratablePosInfo[][][] arr = new GeneratablePosInfo[16][16][16];
				chunkInfo.blocks.forEach(posInfo -> arr[posInfo.getX() & 15][posInfo.getY() & 15][posInfo.getZ() & 15] = posInfo);
				for (int y = 0; y < 16; y++) {
					for (int x = 0; x < 16; x++) {
						for (int z = 0; z < 16; z++) {
							if (arr[x][y][z] == null) {
								buf.writeByte(-1);
							} else {
								GeneratablePosInfo.Registry.write(arr[x][y][z], buf, palette, compoundList);
							}
						}
					}
				}
			});
			compound.setByteArray("blocks", Arrays.copyOf(buf.array(), buf.writerIndex()));
			compound.setTag("palette", palette.writeToNBT());
			compound.setTag("compoundList", compoundList);
			return compound;
		}

		@Override
		public BlockDungeonPart read(World world, NBTTagCompound compound) {
			ByteBuf buf = Unpooled.wrappedBuffer(compound.getByteArray("blocks"));
			BlockStatePalette palette = new BlockStatePalette(compound.getTagList("palette", Constants.NBT.TAG_COMPOUND));
			NBTTagList compoundList = compound.getTagList("compoundList", Constants.NBT.TAG_COMPOUND);
			List<GeneratableChunkInfo> chunks = new ArrayList<>();
			for (int i = buf.readInt(); i > 0; i--) {
				int chunkX = buf.readInt();
				int chunkY = buf.readInt();
				int chunkZ = buf.readInt();
				List<GeneratablePosInfo> blocks = new ArrayList<>();
				for (int y = 0; y < 16; y++) {
					for (int x = 0; x < 16; x++) {
						for (int z = 0; z < 16; z++) {
							if (buf.getByte(buf.readerIndex()) == -1) {
								buf.readByte();
								continue;
							}
							blocks.add(GeneratablePosInfo.Registry.read(world, (chunkX << 4) + x, (chunkY << 4) + y, (chunkZ << 4) + z, buf, palette,
									compoundList));
						}
					}
				}
				chunks.add(new GeneratableChunkInfo(chunkX, chunkY, chunkZ, blocks));
			}
			return new BlockDungeonPart(chunks);
		}

	}

}
