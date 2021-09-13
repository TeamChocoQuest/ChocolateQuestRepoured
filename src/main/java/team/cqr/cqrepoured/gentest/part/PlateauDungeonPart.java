package team.cqr.cqrepoured.gentest.part;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import team.cqr.cqrepoured.gentest.DungeonPlacement;
import team.cqr.cqrepoured.gentest.GeneratableDungeon;
import team.cqr.cqrepoured.gentest.part.DungeonPart.Registry.ISerializer;
import team.cqr.cqrepoured.util.BlockPlacingHelper;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.Perlin3D;

public class PlateauDungeonPart extends DungeonPart {

	private static final MutableBlockPos MUTABLE = new MutableBlockPos();
	private final long seed;
	private final int startX;
	private final int startZ;
	private final int endX;
	private final int endY;
	private final int endZ;
	private final int wallSize;
	private final IBlockState supportHillBlock;
	private final IBlockState supportHillTopBlock;
	private final Perlin3D perlin1;
	private final Perlin3D perlin2;
	private int chunkX;
	private int chunkZ;
	private int chunkX1;
	private int chunkZ1;

	protected PlateauDungeonPart(long seed, int startX, int startZ, int endX, int endY, int endZ, int wallSize, @Nullable IBlockState supportHillBlock,
			@Nullable IBlockState supportHillTopBlock) {
		this.seed = seed;
		this.startX = startX - wallSize;
		this.startZ = startZ - wallSize;
		this.endX = endX + wallSize;
		this.endY = endY;
		this.endZ = endZ + wallSize;
		this.wallSize = wallSize;
		this.supportHillBlock = supportHillBlock;
		this.supportHillTopBlock = supportHillTopBlock;
		this.perlin1 = new Perlin3D(seed, wallSize);
		this.perlin2 = new Perlin3D(seed, wallSize * 4);
		this.chunkX = this.startX >> 4;
		this.chunkZ = this.startZ >> 4;
		this.chunkX1 = this.startX >> 4;
		this.chunkZ1 = this.startZ >> 4;
	}

	@Override
	public void generate(World world, GeneratableDungeon dungeon) {
		if (this.chunkX <= this.endX >> 4) {
			Chunk chunk = world.getChunk(this.chunkX, this.chunkZ);

			for (int x = 0; x < 16; x++) {
				if ((this.chunkX << 4) + x < this.startX || (this.chunkX << 4) + x > this.endX) {
					continue;
				}
				for (int z = 0; z < 16; z++) {
					if ((this.chunkZ << 4) + z < this.startZ || (this.chunkZ << 4) + z > this.endZ) {
						continue;
					}

					MUTABLE.setPos((this.chunkX << 4) + x, chunk.getTopFilledSegment() + 15, (this.chunkZ << 4) + z);
					IBlockState state1 = this.supportHillBlock;
					IBlockState state2 = this.supportHillTopBlock;
					if (state1 == null || state2 == null) {
						Biome biome = world.getBiome(MUTABLE.setPos(this.chunkX << 4, 0, this.chunkZ << 4));
						if (state1 == null) {
							state1 = biome.fillerBlock;
						}
						if (state2 == null) {
							state2 = biome.topBlock;
						}
					}
					this.setYtoHeight(world, MUTABLE.setPos((this.chunkX << 4) + x, this.endY + 1, (this.chunkZ << 4) + z));
					int posY = MUTABLE.getY();
					int i = Math.max((this.endY - 1) - posY, 1);

					while (MUTABLE.getY() < this.endY) {
						if (MUTABLE.getX() >= this.startX + this.wallSize && MUTABLE.getX() <= this.endX - this.wallSize
								&& MUTABLE.getZ() >= this.startZ + this.wallSize && MUTABLE.getZ() <= this.endZ - this.wallSize) {
							BlockPlacingHelper.setBlockState(world, MUTABLE, state1, null, 16, false);
							dungeon.mark(MUTABLE.getX() >> 4, MUTABLE.getY() >> 4, MUTABLE.getZ() >> 4);
						} else {
							float noiseVar = (MUTABLE.getY() - (this.endY - 1)) / (i * 1.5F);

							noiseVar += Math.max((this.wallSize - (MUTABLE.getX() - this.startX)) / 8.0F, 0.0F);
							noiseVar += Math.max((this.wallSize - ((this.endX + 1) - MUTABLE.getX())) / 8.0F, 0.0F);

							noiseVar += Math.max((this.wallSize - (MUTABLE.getZ() - this.startZ)) / 8.0F, 0.0F);
							noiseVar += Math.max((this.wallSize - ((this.endZ + 1) - MUTABLE.getZ())) / 8.0F, 0.0F);

							if (noiseVar / 3.0D + (MUTABLE.getY() - posY) / i * 0.25D >= 0.5D) {
								break;
							}

							double value = (this.perlin1.getNoiseAt(MUTABLE.getX(), MUTABLE.getY(), MUTABLE.getZ())
									+ this.perlin2.getNoiseAt(MUTABLE.getX(), MUTABLE.getY(), MUTABLE.getZ()) + noiseVar) / 3.0D
									+ (MUTABLE.getY() - posY) / i * 0.25D;

							if (value < 0.5D) {
								BlockPlacingHelper.setBlockState(world, MUTABLE, state1, null, 16, false);
								dungeon.mark(MUTABLE.getX() >> 4, MUTABLE.getY() >> 4, MUTABLE.getZ() >> 4);
							} else {
								break;
							}
						}
						MUTABLE.setY(MUTABLE.getY() + 1);
					}

					if (MUTABLE.getY() <= this.endY) {
						BlockPlacingHelper.setBlockState(world, MUTABLE, state2, null, 16, false);
						dungeon.mark(MUTABLE.getX() >> 4, MUTABLE.getY() >> 4, MUTABLE.getZ() >> 4);
					}
				}
			}

			this.chunkZ++;
			if (this.chunkZ > this.endZ >> 4) {
				this.chunkZ = this.startZ >> 4;
				this.chunkX++;
			}
		} else if (this.chunkX1 <= this.endX >> 4) {
			Chunk chunk = world.getChunk(this.chunkX1, this.chunkZ1);

			for (int x = 0; x < 16; x++) {
				if ((this.chunkX1 << 4) + x < this.startX || (this.chunkX1 << 4) + x > this.endX) {
					continue;
				}
				for (int z = 0; z < 16; z++) {
					if ((this.chunkZ1 << 4) + z < this.startZ || (this.chunkZ1 << 4) + z > this.endZ) {
						continue;
					}

					MUTABLE.setPos((chunkX1 << 4) + x, this.endY, (chunkZ1 << 4) + z);
					while (MUTABLE.getY() > 0 && chunk.getBlockState(MUTABLE).getBlock() == Blocks.AIR) {
						MUTABLE.setY(MUTABLE.getY() - 1);
					}
					MUTABLE.setY(MUTABLE.getY() + 1);
					if (chunk.getBlockState(MUTABLE).getBlock() == Blocks.AIR) {
						Biome biome = world.getBiome(MUTABLE);
						if (DungeonGenUtils.percentageRandom(biome.decorator.grassPerChunk / 512.0D)) {
							biome.getRandomWorldGenForGrass(world.rand).generate(world, world.rand, MUTABLE);
						}
					}
				}
			}

			this.chunkZ1++;
			if (this.chunkZ1 > this.endZ >> 4) {
				this.chunkZ1 = this.startZ >> 4;
				this.chunkX1++;
			}
		}
	}

	private void setYtoHeight(World world, MutableBlockPos pos) {
		Chunk chunk = world.getChunk(pos);

		while (pos.getY() >= 0) {
			IBlockState state = chunk.getBlockState(pos);
			Material material = state.getMaterial();
			Block block = state.getBlock();

			if (material.blocksMovement() && !block.isLeaves(state, world, pos) && !block.isFoliage(world, pos)) {
				break;
			}

			pos.setY(pos.getY() - 1);
		}

		pos.setY(pos.getY() + 1);
	}

	@Override
	public boolean isGenerated() {
		return this.chunkX1 > this.endX >> 4;
	}

	public int getStartX() {
		return this.startX;
	}

	public int getStartZ() {
		return this.startZ;
	}

	public int getEndX() {
		return this.endX;
	}

	public int getEndY() {
		return this.endY;
	}

	public int getEndZ() {
		return this.endZ;
	}

	public static class Builder implements IDungeonPartBuilder {

		private final int startX;
		private final int startZ;
		private final int endX;
		private final int endY;
		private final int endZ;
		private final int wallSize;
		private IBlockState supportHillBlock;
		private IBlockState supportHillTopBlock;

		public Builder(int startX, int startZ, int endX, int endY, int endZ, int wallSize) {
			this.startX = Math.min(startX, endX);
			this.startZ = Math.min(startZ, endZ);
			this.endX = Math.max(startX, endX);
			this.endY = endY;
			this.endZ = Math.max(startZ, endZ);
			this.wallSize = wallSize;
		}

		public Builder setSupportHillBlock(@Nullable IBlockState state) {
			this.supportHillBlock = state;
			return this;
		}

		public Builder setSupportHillTopBlock(@Nullable IBlockState state) {
			this.supportHillTopBlock = state;
			return this;
		}

		@Override
		public PlateauDungeonPart build(World world, DungeonPlacement placement) {
			return new PlateauDungeonPart(world.getSeed(), this.startX, this.startZ, this.endX, this.endY, this.endZ, this.wallSize, this.supportHillBlock,
					this.supportHillTopBlock);
		}

	}

	public static class Serializer implements ISerializer<PlateauDungeonPart> {

		@Override
		public NBTTagCompound write(PlateauDungeonPart part, NBTTagCompound compound) {
			compound.setLong("seed", part.seed);
			compound.setInteger("startX", part.startX);
			compound.setInteger("startZ", part.startZ);
			compound.setInteger("endX", part.endX);
			compound.setInteger("endY", part.endY);
			compound.setInteger("endZ", part.endZ);
			compound.setInteger("wallSize", part.wallSize);
			compound.setTag("supportHillBlock", NBTUtil.writeBlockState(new NBTTagCompound(), part.supportHillBlock));
			compound.setTag("supportHillTopBlock", NBTUtil.writeBlockState(new NBTTagCompound(), part.supportHillTopBlock));
			compound.setInteger("chunkX", part.chunkX);
			compound.setInteger("chunkZ", part.chunkZ);
			compound.setInteger("chunkX1", part.chunkX1);
			compound.setInteger("chunkZ1", part.chunkZ1);
			return compound;
		}

		@Override
		public PlateauDungeonPart read(World world, NBTTagCompound compound) {
			long seed = compound.getLong("seed");
			int startX = compound.getInteger("startX");
			int startZ = compound.getInteger("startZ");
			int endX = compound.getInteger("endX");
			int endY = compound.getInteger("endY");
			int endZ = compound.getInteger("endZ");
			int wallSize = compound.getInteger("wallSize");
			IBlockState supportHillBlock = NBTUtil.readBlockState(compound.getCompoundTag("supportHillBlock"));
			IBlockState supportHillTopBlock = NBTUtil.readBlockState(compound.getCompoundTag("supportHillTopBlock"));
			int chunkX = compound.getInteger("chunkX");
			int chunkZ = compound.getInteger("chunkZ");
			int chunkX1 = compound.getInteger("chunkX1");
			int chunkZ1 = compound.getInteger("chunkZ1");
			PlateauDungeonPart part = new PlateauDungeonPart(seed, startX, startZ, endX, endY, endZ, wallSize, supportHillBlock, supportHillTopBlock);
			part.chunkX = chunkX;
			part.chunkZ = chunkZ;
			part.chunkX1 = chunkX1;
			part.chunkZ1 = chunkZ1;
			return part;
		}

	}

}
