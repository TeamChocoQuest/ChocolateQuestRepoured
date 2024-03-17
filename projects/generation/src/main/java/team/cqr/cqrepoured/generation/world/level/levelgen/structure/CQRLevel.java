package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.util.Objects;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.FluidState;
import team.cqr.cqrepoured.generation.util.SectionMap;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity.EntityFactory;

public class CQRLevel extends SectionMap<StructureSection, CompoundTag> {

	private final long seed;

	public CQRLevel(SectionPos center, long seed) {
		super(center, new Int2ObjectOpenHashMap<>());
		this.seed = seed;
	}

	public CQRLevel(CompoundTag nbt) {
		super(nbt);
		this.seed = nbt.getLong("Seed");
	}

	@Override
	public CompoundTag save() {
		CompoundTag nbt = new CompoundTag();
		nbt.putLong("Seed", this.seed);
		return nbt;
	}

	@Override
	protected StructureSection readSectionFromTag(SectionPos pos, CompoundTag sectionNbt) {
		return new StructureSection(pos, sectionNbt);
	}

	@Override
	protected StructureSection createSection(SectionPos pos) {
		return new StructureSection(pos);
	}

	public void generate(WorldGenLevel level, BoundingBox box, EntityFactory entityFactory) {
		SectionPos.betweenClosedStream(box.minX() >> 4, box.minY() >> 4, box.minZ() >> 4, box.maxX() >> 4, box.maxY() >> 4, box.maxZ() >> 4)
				.map(this::getSection)
				.filter(Objects::nonNull)
				.forEach(section -> section.generate(level, entityFactory));
	}

	public long getSeed() {
		return this.seed;
	}

	@Nullable
	public BlockState getBlockState(BlockPos pos) {
		StructureSection section = this.getSection(pos);
		return section != null ? section.getBlockState(pos) : null;
	}

	public void setBlockState(BlockPos pos, @Nullable BlockState state) {
		this.setBlockState(pos, state, null);
	}

	public void setBlockState(BlockPos pos, @Nullable BlockState state, @Nullable CompoundTag blockEntityTag) {
		this.getOrCreateSection(pos).setBlockState(pos, state, blockEntityTag);
	}

	@Nullable
	public FluidState getFluidState(BlockPos pos) {
		StructureSection section = this.getSection(pos);
		return section != null ? section.getFluidState(pos) : null;
	}

	@Nullable
	public CompoundTag getBlockEntity(BlockPos pos) {
		StructureSection section = this.getSection(pos);
		return section != null ? section.getBlockEntity(pos) : null;
	}

	public void addEntity(Entity entity) {
		this.addEntity(EntityFactory.save(entity));
	}

	public void addEntity(CompoundTag entityTag) {
		ListTag posTag = entityTag.getList("Pos", Tag.TAG_DOUBLE);
		int x = Mth.floor(posTag.getDouble(0));
		int y = Mth.floor(posTag.getDouble(1));
		int z = Mth.floor(posTag.getDouble(2));
		this.getOrCreateSectionFromBlock(x, y, z).addEntity(entityTag);
	}

}
