package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.SectionPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import team.cqr.cqrepoured.util.NBTCollectors;

public class CQRLevel implements ICQRLevel, IBlockReader {

	private static final ICQRSection EMPTY = new ICQRSection() {

		@Override
		public BlockState getBlockState(BlockPos pos) {
			return Blocks.AIR.defaultBlockState();
		}

		@Override
		public void setBlockState(BlockPos pos, BlockState state, Consumer<TileEntity> blockEntityCallback) {

		}

		@Override
		public FluidState getFluidState(BlockPos pos) {
			return Fluids.EMPTY.defaultFluidState();
		}

		@Override
		@Nullable
		public TileEntity getBlockEntity(BlockPos pos) {
			return null;
		}

		@Override
		public void addEntity(Entity entity) {

		}

	};

	private final SectionPos center;
	private final long seed;
	private final Int2ObjectMap<CQRSection> sections;

	public CQRLevel(SectionPos center, long seed) {
		this.center = center;
		this.seed = seed;
		this.sections = new Int2ObjectOpenHashMap<>();
	}

	public CQRLevel(CompoundNBT nbt) {
		this.center = SectionPos.of(nbt.getInt("CenterX"), nbt.getInt("CenterY"), nbt.getInt("CenterZ"));
		this.seed = nbt.getLong("Seed");
		this.sections = NBTCollectors.toInt2ObjectMap(nbt.getCompound("Sections"), (CompoundNBT sectionNbt) -> new CQRSection(this, sectionNbt));
	}

	public CompoundNBT save() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("CenterX", this.center.getX());
		nbt.putInt("CenterY", this.center.getY());
		nbt.putInt("CenterZ", this.center.getZ());
		nbt.putLong("Seed", this.seed);
		nbt.put("Sections", NBTCollectors.toCompound(this.sections, CQRSection::save));
		return nbt;
	}

	public void generate(ISeedReader pLevel, MutableBoundingBox pBox, IEntityFactory entityFactory) {
		SectionPos.betweenClosedStream(pBox.x0 >> 4, pBox.y0 >> 4, pBox.z0 >> 4, pBox.x1 >> 4, pBox.y1 >> 4, pBox.z1 >> 4).forEach(sectionPos -> {
			CQRSection section = this.sections.get(index(sectionPos));
			if (section != null) {
				section.generate(pLevel, sectionPos, entityFactory);
			}
		});
	}

	@Override
	public long getSeed() {
		return this.seed;
	}

	private int index(SectionPos pos) {
		int x = pos.x() - this.center.x() + 512;
		int y = pos.y() - this.center.y() + 512;
		int z = pos.z() - this.center.z() + 512;
		return x << 20 | y << 10 | z;
	}

	private int index(BlockPos pos) {
		return this.index(pos.getX(), pos.getY(), pos.getZ());
	}

	private int index(int x, int y, int z) {
		x = (x >> 4) - this.center.x() + 512;
		y = (y >> 4) - this.center.y() + 512;
		z = (z >> 4) - this.center.z() + 512;
		return x << 20 | y << 10 | z;
	}

	private int index(Vector3d vec) {
		return this.index(vec.x(), vec.y(), vec.z());
	}

	private int index(double x, double y, double z) {
		return this.index(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
	}

	private ICQRSection getSection(int index) {
		ICQRSection section = this.sections.get(index);
		return section != null ? section : EMPTY;
	}

	private ICQRSection getOrCreateSection(int index) {
		return this.sections.computeIfAbsent(index, k -> new CQRSection(this));
	}

	@Override
	public BlockState getBlockState(BlockPos pos) {
		BlockState state = this.getSection(this.index(pos)).getBlockState(pos);
		return state != null ? state : Blocks.AIR.defaultBlockState();
	}

	@Override
	public void setBlockState(BlockPos pos, @Nullable BlockState state, @Nullable Consumer<TileEntity> blockEntityCallback) {
		this.getOrCreateSection(this.index(pos)).setBlockState(pos, state, blockEntityCallback);
	}

	@Override
	public FluidState getFluidState(BlockPos pos) {
		FluidState state = this.getSection(this.index(pos)).getFluidState(pos);
		return state != null ? state : Fluids.EMPTY.defaultFluidState();
	}

	@Override
	@Nullable
	public TileEntity getBlockEntity(BlockPos pos) {
		return this.getSection(this.index(pos)).getBlockEntity(pos);
	}

	@Override
	public void addEntity(Entity entity) {
		this.getOrCreateSection(this.index(entity.position())).addEntity(entity);
	}

}
