package team.cqr.cqrepoured.generation.world.level.levelgen.structure.templatesystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.nbt.NBTUtil;
import team.cqr.cqrepoured.common.primitive.IntUtil;
import team.cqr.cqrepoured.generation.util.BlockStatePaletteUtil;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRStructurePiece;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.BlockInfoFactories;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.BlockInfoSerializers;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity.PreparableEntityInfo;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant.DungeonInhabitant;

public class CQRStructureTemplate extends StructureTemplate {

	private static final String VERSION = "3.0.0";

	private static final String CQR_VERSION_TAG = "cqr_template_version";
	private static final String CQR_BLOCKS_TAG = "cqr_blocks";
	private static final String CQR_BLOCK_STATES_TAG = "cqr_block_states";
	private static final String CQR_ENTITIES_TAG = "cqr_entities";
	private static final String CQR_UNPROTECTED_BLOCKS_TAG = "cqr_unprotected_blocks";

	private static final List<Predicate<BlockState>> IGNORED_UNBREAKABLE_BLOCK_PREDICATES = new ArrayList<>();
	private static final Set<ResourceLocation> WHITELISTED_ENTITY_TYPES = new HashSet<>();

	private List<PreparablePosInfo> blocks = List.of();
	private List<PreparableEntityInfo> entities = List.of();
	private List<BlockPos> unprotectedBlocks = List.of();

	@Deprecated
	@Override
	public void fillFromWorld(Level pLevel, BlockPos pPos, Vec3i pSize, boolean pWithEntities, Block pToIgnore) {
		CQRepoured.LOGGER.warn("CQR structure templates should not be filled from world like vanilla structure templates!");
		this.blocks.clear();
		this.entities.clear();
		this.unprotectedBlocks.clear();
		super.fillFromWorld(pLevel, pPos, pSize, pWithEntities, pToIgnore);
	}

	@Deprecated
	@Override
	public boolean placeInWorld(ServerLevelAccessor pServerLevel, BlockPos pOffset, BlockPos pPos, StructurePlaceSettings pSettings, RandomSource pRandom,
			int pFlags) {
		CQRepoured.LOGGER.warn("CQR structure templates should not be placed in world like vanilla structure templates!");
		return super.placeInWorld(pServerLevel, pOffset, pPos, pSettings, pRandom, pFlags);
	}

	@Override
	public void load(HolderGetter<Block> pBlockGetter, CompoundTag pTag) {
		super.load(pBlockGetter, pTag);

		ByteArrayDataInput in = ByteStreams.newDataInput(pTag.getByteArray(CQR_BLOCKS_TAG));
		SimplePalette palette = BlockStatePaletteUtil.readSimplePalette(pBlockGetter, pTag.getList(CQR_BLOCK_STATES_TAG, Tag.TAG_COMPOUND));
		this.blocks = IntStream.range(0, this.size.getX() * this.size.getY() * this.size.getZ())
				.mapToObj(i -> BlockInfoSerializers.read(in, palette))
				.toList();

		this.entities = pTag.getList(CQR_ENTITIES_TAG, Tag.TAG_COMPOUND)
				.stream()
				.map(CompoundTag.class::cast)
				.map(PreparableEntityInfo::new)
				.toList();
		this.unprotectedBlocks = IntUtil.streamXYZ(pTag.getIntArray(CQR_UNPROTECTED_BLOCKS_TAG), BlockPos::new)
				.toList();
	}

	@Override
	public CompoundTag save(CompoundTag pTag) {
		pTag = super.save(pTag);

		pTag.putString(CQR_VERSION_TAG, VERSION);

		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		SimplePalette palette = new SimplePalette();
		this.blocks.forEach(blockInfo -> BlockInfoSerializers.write(blockInfo, out, palette));
		pTag.putByteArray(CQR_BLOCKS_TAG, out.toByteArray());
		pTag.put(CQR_BLOCK_STATES_TAG, BlockStatePaletteUtil.writeSimplePalette(palette));

		pTag.put(CQR_ENTITIES_TAG, this.entities.stream()
				.map(PreparableEntityInfo::getEntityData)
				.collect(NBTUtil.toList()));
		pTag.putIntArray(CQR_UNPROTECTED_BLOCKS_TAG, this.unprotectedBlocks.stream()
				.flatMapToInt(pos -> IntStream.of(pos.getX(), pos.getY(), pos.getZ()))
				.toArray());

		return pTag;
	}

	private static BlockPos setPos(MutableBlockPos pos, int index, int x, int y, int z, int sizeY, int sizeZ) {
		int dz = index % sizeZ;
		int i = index / sizeZ;
		int dy = i % sizeY;
		int dx = i / sizeY;
		return pos.set(x + dx, y + dy, z + dz);
	}

	public static CQRStructureTemplate createFromWorld(Level level, BoundingBox boundingBox, boolean useEntityWhitelist, Collection<BlockPos> unprotectedBlocks,
			String author) {
		CQRStructureTemplate structure = new CQRStructureTemplate();
		structure.author = author;
		structure.takeBlocksAndEntitiesFromWorld(level, boundingBox, useEntityWhitelist, unprotectedBlocks);
		return structure;
	}

	private void takeBlocksAndEntitiesFromWorld(Level level, BoundingBox boundingBox, boolean useEntityWhitelist, Collection<BlockPos> unprotectedBlocks) {
		this.palettes.clear();
		this.entityInfoList.clear();
		this.size = boundingBox.getLength()
				.offset(1, 1, 1);

		this.takeBlocksFromWorld(level, boundingBox);
		this.takeEntitiesFromWorld(level, boundingBox, useEntityWhitelist);

		this.unprotectedBlocks = unprotectedBlocks.stream()
				.filter(boundingBox::isInside)
				.distinct()
				.toList();
	}

	private void takeBlocksFromWorld(Level level, BoundingBox boundingBox) {
		this.blocks = CQRStructureTemplate.streamPositions(boundingBox)
				.map(pos -> {
					BlockState state = level.getBlockState(pos);

					if (!CQRStructureTemplate.isIgnoredUnbreakableBlock(state) && state.getDestroySpeed(level, pos) < 0.0F) {
						CQRepoured.LOGGER.warn("Exporting unbreakable block: {} from {}", state, pos);
					}

					return BlockInfoFactories.create(level, pos, state);
				})
				.toList();
	}

	private static Stream<BlockPos> streamPositions(BoundingBox boundingBox) {
		MutableBlockPos pos = new MutableBlockPos();
		return IntStream.range(0, boundingBox.getXSpan() * boundingBox.getYSpan() * boundingBox.getZSpan())
				.mapToObj(i -> CQRStructureTemplate.setPos(pos, i, boundingBox));
	}

	private static BlockPos setPos(MutableBlockPos pos, int index, BoundingBox boundingBox) {
		return CQRStructureTemplate.setPos(pos, index, boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.getYSpan(),
				boundingBox.getZSpan());
	}

	private static boolean isIgnoredUnbreakableBlock(BlockState blockState) {
		return IGNORED_UNBREAKABLE_BLOCK_PREDICATES.stream()
				.anyMatch(predicate -> predicate.test(blockState));
	}

	public static void addIgnoredUnbreakableBlockPredicate(Predicate<BlockState> predicate) {
		IGNORED_UNBREAKABLE_BLOCK_PREDICATES.add(predicate);
	}

	public static void clearIgnoredUnbreakableBlockPredicates() {
		IGNORED_UNBREAKABLE_BLOCK_PREDICATES.clear();
	}

	private void takeEntitiesFromWorld(Level level, BoundingBox boundingBox, boolean useEntityWhitelist) {
		this.entities = CQRStructureTemplate.streamEntities(level, boundingBox)
				.filter(((Predicate<Entity>) Player.class::isInstance).negate())
				.filter(entity -> {
					if (useEntityWhitelist && !CQRStructureTemplate.isWhitelistedEntity(entity)) {
						CQRepoured.LOGGER.info("Skipping entity: {}", entity);
						return false;
					}

					return true;
				})
				.map(entity -> new PreparableEntityInfo(entity, boundingBox))
				.toList();
	}

	private static Stream<Entity> streamEntities(Level level, BoundingBox boundingBox) {
		AABB aabb = new AABB(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX() + 1, boundingBox.maxY() + 1,
				boundingBox.maxZ() + 1);
		return level.getEntitiesOfClass(Entity.class, aabb)
				.stream();
	}

	private static boolean isWhitelistedEntity(Entity entity) {
		return WHITELISTED_ENTITY_TYPES.contains(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
	}

	public static void addWhitelistedEntityType(ResourceLocation entityTypeName) {
		WHITELISTED_ENTITY_TYPES.add(entityTypeName);
	}

	public static void clearWhitelistedEntityTypes() {
		WHITELISTED_ENTITY_TYPES.clear();
	}

	public void addAll(CQRStructurePiece.Builder builder, BlockPos pos, Offset offset) {
		this.addAll(builder, builder.getPlacement(offset.apply(pos, this, Mirror.NONE, Rotation.NONE)));
	}

	public void addAll(CQRStructurePiece.Builder builder, BlockPos pos, Offset offset, Mirror mirror, Rotation rotation) {
		this.addAll(builder, builder.getPlacement(offset.apply(pos, this, mirror, rotation), mirror, rotation));
	}

	public void addAll(CQRStructurePiece.Builder builder, BlockPos pos, Offset offset, DungeonInhabitant inhabitant) {
		this.addAll(builder, builder.getPlacement(offset.apply(pos, this, Mirror.NONE, Rotation.NONE), inhabitant));
	}

	public void addAll(CQRStructurePiece.Builder builder, BlockPos pos, Offset offset, Mirror mirror, Rotation rotation, DungeonInhabitant inhabitant) {
		this.addAll(builder, builder.getPlacement(offset.apply(pos, this, mirror, rotation), mirror, rotation, inhabitant));
	}

	public void addAll(CQRStructurePiece.Builder builder, DungeonPlacement placement) {
		this.forEachBlockInfo((pos, blockInfo) -> blockInfo.prepare(builder.level(), pos, placement));
		this.entities.forEach(entityInfo -> entityInfo.prepare(builder.level(), placement));
		builder.protectedRegionBuilder()
				.ifPresent(protectedRegionBuilder -> this.unprotectedBlocks.forEach(protectedRegionBuilder::excludePos));
	}

	private void forEachBlockInfo(BiConsumer<BlockPos, PreparablePosInfo> action) {
		MutableBlockPos pos = new MutableBlockPos();
		for (int i = 0; i < this.blocks.size(); i++) {
			action.accept(this.setPos(pos, i), this.blocks.get(i));
		}
	}

	private BlockPos setPos(MutableBlockPos pos, int index) {
		return CQRStructureTemplate.setPos(pos, index, 0, 0, 0, this.size.getY(), this.size.getZ());
	}

}
