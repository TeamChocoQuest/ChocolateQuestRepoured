package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
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
import team.cqr.cqrepoured.common.nbt.NBTUtil;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity.EntityFactory;

public class CQRLevel {

	private final SectionPos center;
	private final long seed;
	private final Int2ObjectMap<CQRSection> sections;

	public CQRLevel(SectionPos center, long seed) {
		this.center = center;
		this.seed = seed;
		this.sections = new Int2ObjectOpenHashMap<>();
	}

	public CQRLevel(CompoundTag nbt) {
		this.center = SectionPos.of(nbt.getInt("CenterX"), nbt.getInt("CenterY"), nbt.getInt("CenterZ"));
		this.seed = nbt.getLong("Seed");
		this.sections = NBTUtil.toInt2ObjectMap(nbt.getCompound("Sections"), (CompoundTag sectionNbt) -> new CQRSection(sectionNbt));
	}

	public CompoundTag save() {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("CenterX", this.center.x());
		nbt.putInt("CenterY", this.center.y());
		nbt.putInt("CenterZ", this.center.z());
		nbt.putLong("Seed", this.seed);
		nbt.put("Sections", NBTUtil.collect(this.sections, CQRSection::save));
		return nbt;
	}

	public void generate(WorldGenLevel level, BoundingBox box, EntityFactory entityFactory) {
		SectionPos.betweenClosedStream(box.minX() >> 4, box.minY() >> 4, box.minZ() >> 4, box.maxX() >> 4, box.maxY() >> 4, box.maxZ() >> 4)
				.map(sectionPos -> this.sections.get(this.index(sectionPos)))
				.filter(Objects::nonNull)
				.forEach(section -> section.generate(level, entityFactory));
	}

	public SectionPos getCenter() {
		return center;
	}

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

	@Nullable
	private CQRSection getSection(BlockPos pos) {
		return this.sections.get(this.index(pos));
	}

	private CQRSection getOrCreateSection(BlockPos pos) {
		return this.sections.computeIfAbsent(this.index(pos), k -> new CQRSection(SectionPos.of(pos)));
	}

	@Nullable
	public BlockState getBlockState(BlockPos pos) {
		CQRSection section = this.getSection(pos);
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
		CQRSection section = this.getSection(pos);
		return section != null ? section.getFluidState(pos) : null;
	}

	@Nullable
	public CompoundTag getBlockEntity(BlockPos pos) {
		CQRSection section = this.getSection(pos);
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
		this.getOrCreateSection(new BlockPos(x, y, z)).addEntity(entityTag);
	}

	public Collection<CQRSection> getSections() {
		return Collections.unmodifiableCollection(this.sections.values());
	}

}
