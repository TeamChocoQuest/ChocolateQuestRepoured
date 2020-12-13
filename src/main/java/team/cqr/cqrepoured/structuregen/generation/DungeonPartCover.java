package team.cqr.cqrepoured.structuregen.generation;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class DungeonPartCover extends AbstractDungeonPart {

	private IBlockState coverBlock;
	private int x1;
	private int z1;

	public DungeonPartCover(World world, DungeonGenerator dungeonGenerator) {
		this(world, dungeonGenerator, 0, 0, 0, 0, Blocks.AIR.getDefaultState());
	}

	public DungeonPartCover(World world, DungeonGenerator dungeonGenerator, int startX, int startZ, int endX, int endZ, IBlockState coverBlock) {
		super(world, dungeonGenerator, new BlockPos(Math.min(startX, endX), dungeonGenerator.getPos().getY(), Math.min(startZ, endZ)));
		this.maxPos = new BlockPos(Math.max(startX, endX), dungeonGenerator.getPos().getY(), Math.max(startZ, endZ));
		this.x1 = this.minPos.getX();
		this.z1 = this.minPos.getZ();
		this.coverBlock = coverBlock;
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = super.writeToNBT();
		compound.setString("coverBlock", this.coverBlock.getBlock().getRegistryName().toString());
		compound.setInteger("coverBlockMeta", this.coverBlock.getBlock().getMetaFromState(this.coverBlock));
		compound.setInteger("x1", this.x1);
		compound.setInteger("z1", this.z1);
		return compound;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("coverBlock")));
		if (b != null) {
			this.coverBlock = b.getStateFromMeta(compound.getInteger("coverBlockMeta"));
		} else {
			this.coverBlock = Blocks.AIR.getDefaultState();
		}
		this.x1 = compound.getInteger("x1");
		this.z1 = compound.getInteger("z1");
	}

	@Override
	public String getId() {
		return DUNGEON_PART_COVER_ID;
	}

	@Override
	public void generateNext() {
		if (this.x1 <= this.maxPos.getX()) {
			BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(this.x1, 255, this.z1);

			if (this.world.getBlockState(mutablePos).getBlock() == Blocks.AIR) {
				mutablePos.setY(mutablePos.getY() - 1);

				while (mutablePos.getY() > 0) {
					IBlockState state = this.world.getBlockState(mutablePos);

					if (state.getBlock() == Blocks.AIR) {
						mutablePos.setY(mutablePos.getY() - 1);
					} else {
						if (state.getBlock() != this.coverBlock) {
							mutablePos.setY(mutablePos.getY() + 1);
							this.world.setBlockState(mutablePos, this.coverBlock, 18);
						}
						break;
					}
				}
			}

			this.z1++;
			if (this.z1 > this.maxPos.getZ()) {
				this.z1 = this.minPos.getZ();
				this.x1++;
			}
		}
	}

	@Override
	public boolean isGenerated() {
		return this.x1 > this.maxPos.getX();
	}

}
