package team.cqr.cqrepoured.gentest.part;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import team.cqr.cqrepoured.gentest.DungeonPlacement;
import team.cqr.cqrepoured.gentest.GeneratableDungeon;
import team.cqr.cqrepoured.gentest.part.DungeonPart.Registry.ISerializer;
import team.cqr.cqrepoured.util.BlockPlacingHelper;

public class CoverDungeonPart extends DungeonPart {

	private static final MutableBlockPos MUTABLE = new MutableBlockPos();
	private final IBlockState coverBlock;
	private final int startX;
	private final int startZ;
	private final int endX;
	private final int endZ;
	private int chunkX;
	private int chunkZ;

	protected CoverDungeonPart(int startX, int startZ, int endX, int endZ, IBlockState coverBlock) {
		this.coverBlock = coverBlock;
		this.startX = startX;
		this.startZ = startZ;
		this.endX = endX;
		this.endZ = endZ;
		this.chunkX = startX >> 4;
		this.chunkZ = startZ >> 4;
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
					while (MUTABLE.getY() >= 0) {
						IBlockState state = chunk.getBlockState(MUTABLE);
						if (state.getBlock() == Blocks.AIR) {
							MUTABLE.setY(MUTABLE.getY() - 1);
						} else {
							if (state.getBlock() != this.coverBlock.getBlock()) {
								MUTABLE.setY(MUTABLE.getY() + 1);
								BlockPlacingHelper.setBlockState(world, MUTABLE, this.coverBlock, null, 16, false);
								dungeon.mark(MUTABLE.getX() >> 4, MUTABLE.getY() >> 4, MUTABLE.getZ() >> 4);
							}
							break;
						}
					}
				}
			}

			this.chunkZ++;
			if (this.chunkZ > this.endZ >> 4) {
				this.chunkZ = this.startZ >> 4;
				this.chunkX++;
			}
		}
	}

	@Override
	public boolean isGenerated() {
		return this.chunkX > this.endX >> 4;
	}

	public IBlockState getCoverBlock() {
		return this.coverBlock;
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

	public int getEndZ() {
		return this.endZ;
	}

	public static class Builder implements IDungeonPartBuilder {

		private final int startX;
		private final int startZ;
		private final int endX;
		private final int endZ;
		private final IBlockState coverBlock;

		public Builder(int startX, int startZ, int endX, int endZ, IBlockState coverBlock) {
			this.startX = Math.min(startX, endX);
			this.startZ = Math.min(startZ, endZ);
			this.endX = Math.max(startX, endX);
			this.endZ = Math.max(startZ, endZ);
			this.coverBlock = coverBlock;
		}

		@Override
		public CoverDungeonPart build(World world, DungeonPlacement placement) {
			return new CoverDungeonPart(this.startX, this.startZ, this.endX, this.endZ, this.coverBlock);
		}

	}

	public static class Serializer implements ISerializer<CoverDungeonPart> {

		@Override
		public NBTTagCompound write(CoverDungeonPart part, NBTTagCompound compound) {
			compound.setInteger("startX", part.startX);
			compound.setInteger("startZ", part.startZ);
			compound.setInteger("endX", part.endX);
			compound.setInteger("endZ", part.endZ);
			compound.setTag("coverBlock", NBTUtil.writeBlockState(new NBTTagCompound(), part.coverBlock));
			compound.setInteger("chunkX", part.chunkX);
			compound.setInteger("chunkZ", part.chunkZ);
			return compound;
		}

		@Override
		public CoverDungeonPart read(World world, NBTTagCompound compound) {
			int startX = compound.getInteger("startX");
			int startZ = compound.getInteger("startZ");
			int endX = compound.getInteger("endX");
			int endZ = compound.getInteger("endZ");
			IBlockState coverBlock = NBTUtil.readBlockState(compound.getCompoundTag("coverBlock"));
			int chunkX = compound.getInteger("chunkX");
			int chunkZ = compound.getInteger("chunkZ");
			CoverDungeonPart part = new CoverDungeonPart(startX, startZ, endX, endZ, coverBlock);
			part.chunkX = chunkX;
			part.chunkZ = chunkZ;
			return part;
		}

	}

}
