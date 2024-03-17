package team.cqr.cqrepoured.generation.util;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import team.cqr.cqrepoured.common.function.IntObj2ObjFunction;
import team.cqr.cqrepoured.common.nbt.NBTUtil;

public abstract class SectionMap<S extends Section<T>, T extends Tag> {

	private final SectionPos center;
	private final Int2ObjectMap<S> sections;

	protected SectionMap(SectionPos center, Int2ObjectMap<S> sections) {
		this.center = center;
		this.sections = sections;
	}

	protected SectionMap(CompoundTag nbt) {
		center = SectionPos.of(nbt.getInt("CenterX"), nbt.getInt("CenterY"), nbt.getInt("CenterZ"));
		sections = NBTUtil.toInt2ObjectMap(nbt.getCompound("Sections"), (IntObj2ObjFunction<T, S>) this::readSectionFromTag);
	}

	public CompoundTag save() {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("CenterX", center.x());
		nbt.putInt("CenterY", center.y());
		nbt.putInt("CenterZ", center.z());
		nbt.put("Sections", NBTUtil.collect(sections, S::save));
		return nbt;
	}

	protected S readSectionFromTag(int index, T sectionNbt) {
		return this.readSectionFromTag(this.sectionPosFromIndex(index), sectionNbt);
	}

	protected abstract S readSectionFromTag(SectionPos pos, T sectionNbt);

	private int index(SectionPos pos) {
		return index(pos.x(), pos.y(), pos.z());
	}

	private int index(BlockPos pos) {
		return indexFromBlock(pos.getX(), pos.getY(), pos.getZ());
	}

	protected int index(int sectionX, int sectionY, int sectionZ) {
		sectionX += 512 - center.x();
		sectionY += 512 - center.y();
		sectionZ += 512 - center.z();
		return sectionX << 20 | sectionY << 10 | sectionZ;
	}

	private int indexFromBlock(int blockX, int blockY, int blockZ) {
		return index(blockX >> 4, blockY >> 4, blockZ >> 4);
	}

	private SectionPos sectionPosFromIndex(int index) {
		int sectionX = (index >> 20);
		int sectionY = ((index >> 10) & 1023);
		int sectionZ = (index & 1023);
		sectionX -= 512 - center.x();
		sectionY -= 512 - center.y();
		sectionZ -= 512 - center.z();
		return SectionPos.of(sectionX, sectionY, sectionZ);
	}

	@Nullable
	public S getSection(SectionPos pos) {
		return sections.get(index(pos));
	}

	@Nullable
	public S getSection(BlockPos pos) {
		return sections.get(index(pos));
	}

	@Nullable
	public S getSection(int sectionX, int sectionY, int sectionZ) {
		return sections.get(index(sectionX, sectionY, sectionZ));
	}

	@Nullable
	public S getSectionFromBlock(int blockX, int blockY, int blockZ) {
		return sections.get(indexFromBlock(blockX, blockY, blockZ));
	}

	public S getOrCreateSection(SectionPos pos) {
		return sections.computeIfAbsent(index(pos), this::createSection);
	}

	public S getOrCreateSection(BlockPos pos) {
		return sections.computeIfAbsent(index(pos), this::createSection);
	}

	public S getOrCreateSection(int sectionX, int sectionY, int sectionZ) {
		return sections.computeIfAbsent(index(sectionX, sectionY, sectionZ), this::createSection);
	}

	public S getOrCreateSectionFromBlock(int blockX, int blockY, int blockZ) {
		return sections.computeIfAbsent(indexFromBlock(blockX, blockY, blockZ), this::createSection);
	}

	protected S createSection(int index) {
		return createSection(sectionPosFromIndex(index));
	}

	protected abstract S createSection(SectionPos pos);

	public SectionPos getCenter() {
		return center;
	}

	public Collection<S> getSections() {
		return Collections.unmodifiableCollection(sections.values());
	}

}
