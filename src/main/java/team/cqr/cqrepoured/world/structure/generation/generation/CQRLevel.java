package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.Collection;
import java.util.Collections;
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
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import team.cqr.cqrepoured.util.NBTCollectors;

public class CQRLevel implements ICQRLevel {

	private static final ICQRSection EMPTY = new ICQRSection() {

		@Override
		public BlockState getBlockState(BlockPos pos) {
			return null;
		}

		@Override
		public void setBlockState(BlockPos pos, BlockState state, Consumer<TileEntity> blockEntityCallback) {

		}

		@Override
		public FluidState getFluidState(BlockPos pos) {
			return null;
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
	private final IBlockReader blockReader = new IBlockReader() {
		@Override
		public FluidState getFluidState(BlockPos pos) {
			FluidState fluidState = this.getFluidState(pos);
			return fluidState != null ? fluidState : Fluids.EMPTY.defaultFluidState();
		}

		@Override
		public BlockState getBlockState(BlockPos pos) {
			BlockState blockState = this.getBlockState(pos);
			return blockState != null ? blockState : Blocks.AIR.defaultBlockState();
		}

		@Override
		public TileEntity getBlockEntity(BlockPos pos) {
			return this.getBlockEntity(pos);
		}
	};

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
		nbt.putInt("CenterX", this.center.x());
		nbt.putInt("CenterY", this.center.y());
		nbt.putInt("CenterZ", this.center.z());
		nbt.putLong("Seed", this.seed);
		nbt.put("Sections", NBTCollectors.toCompound(this.sections, CQRSection::save));
		return nbt;
	}

	public void generate(ISeedReader level, MutableBoundingBox box, IEntityFactory entityFactory) {
		SectionPos.betweenClosedStream(box.x0 >> 4, box.y0 >> 4, box.z0 >> 4, box.x1 >> 4, box.y1 >> 4, box.z1 >> 4).forEach(sectionPos -> {
			CQRSection section = this.sections.get(this.index(sectionPos));
			if (section != null) {
				section.generate(level, entityFactory);
			}
		});
	}

	@Override
	public long getSeed() {
		return this.seed;
	}

	private int index(SectionPos pos) {
		return this.index(pos.x(), pos.y(), pos.z());
	}

	private int index(BlockPos pos) {
		return this.indexFromBlock(pos.getX(), pos.getY(), pos.getZ());
	}

	private int index(int sectionX, int sectionY, int sectionZ) {
		sectionX += 512 - this.center.x();
		sectionY += 512 - this.center.y();
		sectionZ += 512 - this.center.z();
		return sectionX << 20 | sectionY << 10 | sectionZ;
	}

	private int indexFromBlock(int blockX, int blockY, int blockZ) {
		return this.index(blockX >> 4, blockY >> 4, blockZ >> 4);
	}

	public ICQRSection getSection(SectionPos pos) {
		ICQRSection section = this.sections.get(this.index(pos));
		return section != null ? section : EMPTY;
	}

	public ICQRSection getSection(BlockPos pos) {
		ICQRSection section = this.sections.get(this.index(pos));
		return section != null ? section : EMPTY;
	}

	public ICQRSection getOrCreateSection(SectionPos pos) {
		return this.sections.computeIfAbsent(this.index(pos), k -> new CQRSection(this, pos));
	}

	public ICQRSection getOrCreateSection(BlockPos pos) {
		return this.sections.computeIfAbsent(this.index(pos), k -> new CQRSection(this, SectionPos.of(pos)));
	}

	@Override
	@Nullable
	public BlockState getBlockState(BlockPos pos) {
		return this.getSection(pos).getBlockState(pos);
	}

	@Override
	public void setBlockState(BlockPos pos, @Nullable BlockState state, @Nullable Consumer<TileEntity> blockEntityCallback) {
		this.getOrCreateSection(pos).setBlockState(pos, state, blockEntityCallback);
	}

	@Override
	@Nullable
	public FluidState getFluidState(BlockPos pos) {
		return this.getSection(pos).getFluidState(pos);
	}

	@Override
	@Nullable
	public TileEntity getBlockEntity(BlockPos pos) {
		return this.getSection(pos).getBlockEntity(pos);
	}

	@Override
	public void addEntity(Entity entity) {
		this.getOrCreateSection(entity.blockPosition()).addEntity(entity);
	}

	public Collection<CQRSection> getSections() {
		return Collections.unmodifiableCollection(this.sections.values());
	}

	public IBlockReader asBlockReader() {
		return this.blockReader;
	}

}
