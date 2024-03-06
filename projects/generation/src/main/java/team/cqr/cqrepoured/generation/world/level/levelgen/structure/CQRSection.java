package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainer.Strategy;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.nbt.NBTUtil;
import team.cqr.cqrepoured.common.primitive.IntUtil;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity.EntityContainer;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity.EntityFactory;

@SuppressWarnings("deprecation")
public class CQRSection {

	// TODO copied from ChunkSerializer, does this work?
	private static final Codec<PalettedContainer<BlockState>> BLOCK_STATE_CODEC = PalettedContainer.codecRW(Block.BLOCK_STATE_REGISTRY, BlockState.CODEC, PalettedContainer.Strategy.SECTION_STATES, Blocks.AIR.defaultBlockState());

	private final SectionPos sectionPos;
	private final PalettedContainer<BlockState> blocks;
	private final Int2ObjectMap<BlockEntity> blockEntities;
	private final List<EntityContainer> entities;

	public CQRSection(SectionPos sectionPos) {
		this.sectionPos = sectionPos;
		// TODO this probably doesn't support null as default value
		this.blocks = new PalettedContainer<>(Block.BLOCK_STATE_REGISTRY, null, Strategy.SECTION_STATES);
		this.blockEntities = new Int2ObjectOpenHashMap<>();
		this.entities = new ArrayList<>();
	}

	public CQRSection(CompoundTag nbt) {
		this.sectionPos = SectionPos.of(nbt.getInt("X"), nbt.getInt("Y"), nbt.getInt("Z"));
		this.blocks = BLOCK_STATE_CODEC.parse(NbtOps.INSTANCE, nbt.get("BlockStates")).promotePartial(CQRepoured.LOGGER::error).getOrThrow(false, CQRepoured.LOGGER::error);
		this.blockEntities = NBTUtil.<CompoundTag, BlockEntity>toInt2ObjectMap(nbt.getCompound("BlockEntities"), (index, blockEntityNbt) -> {
			return BlockEntity.loadStatic(getPos(sectionPos, index), this.getBlockState(index), blockEntityNbt);
		});
		this.entities = NBTUtil.stream(nbt.get("Entities"), CompoundTag.TYPE).map(EntityContainer::new).collect(Collectors.toList());
	}

	public SectionPos getPos() {
		return this.sectionPos;
	}

	public CompoundTag save() {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("X", this.sectionPos.x());
		nbt.putInt("Y", this.sectionPos.y());
		nbt.putInt("Z", this.sectionPos.z());
		nbt.put("BlockStates", BLOCK_STATE_CODEC.encodeStart(NbtOps.INSTANCE, this.blocks).getOrThrow(false, CQRepoured.LOGGER::error));
		nbt.put("BlockEntities", NBTUtil.collect(this.blockEntities, blockEntity -> blockEntity.saveWithFullMetadata()));
		nbt.put("Entities", this.entities.stream().map(EntityContainer::getEntityNbt).filter(Objects::nonNull).collect(NBTUtil.toList()));
		return nbt;
	}

	public void generate(WorldGenLevel level, EntityFactory entityFactory, @Nonnull List<StructureProcessor> processors) {
		DiscreteVoxelShape voxelShapePart = new BitSetDiscreteVoxelShape(16, 16, 16);

		this.placeBlocks(level, voxelShapePart, processors);
		this.updateShapeAtEdge(level, voxelShapePart);
		this.updateFromNeighbourShapes(level, voxelShapePart);
		this.setBlockEntitiesChanged(level);
		this.addEntities(level, entityFactory);
	}

	private void placeBlocks(WorldGenLevel level, DiscreteVoxelShape voxelShapePart, @Nonnull List<StructureProcessor> processors) {
		MutableBlockPos mutablePos = new MutableBlockPos();

		for (int i = 0; i < 4096; i++) {
			BlockState state = this.getBlockState(i);
			if (state == null) {
				continue;
			}

			int x = x(i);
			int y = y(i);
			int z = z(i);
			setPos(mutablePos, this.sectionPos, x, y, z);

			if (!processors.isEmpty()) {
				// TODO check if this works as intended as this differs a lot from StructureTemplate#processBlockInfos
				BlockEntity blockEntity = this.blockEntities.get(i);
				StructureBlockInfo blockInfo = new StructureBlockInfo(mutablePos, state, blockEntity != null ? blockEntity.saveWithFullMetadata() : null);
				StructureBlockInfo blockInfo1 = new StructureBlockInfo(mutablePos, state, blockInfo.nbt() != null ? blockInfo.nbt().copy() : null);
				StructurePlaceSettings placeSettings = new StructurePlaceSettings();
				for (StructureProcessor processor : processors) {
					blockInfo1 = processor.process(level, mutablePos, mutablePos, blockInfo, blockInfo1, placeSettings, null);
				}
				state = blockInfo1.state();
				blockEntity.load(blockInfo1.nbt());
			}

			if (level.setBlock(mutablePos, state, 0)) {
				voxelShapePart.fill(x, y, z);

				BlockEntity tileEntity = this.blockEntities.get(i);
				if (tileEntity != null) {
					level.getChunk(mutablePos).setBlockEntity(tileEntity);
				}
			}
		}
	}

	private void updateShapeAtEdge(WorldGenLevel level, DiscreteVoxelShape voxelShapePart) {
		MutableBlockPos mutablePos = new MutableBlockPos();
		MutableBlockPos mutablePosNeighbour = new MutableBlockPos();

		voxelShapePart.forAllFaces((direction, x, y, z) -> {
			setPos(mutablePos, this.sectionPos, x, y, z);
			mutablePosNeighbour.setWithOffset(mutablePos, direction);
			BlockState state = level.getBlockState(mutablePos);
			BlockState stateNeighbour = level.getBlockState(mutablePosNeighbour);

			BlockState updatedState = state.updateShape(direction, stateNeighbour, level, mutablePos, mutablePosNeighbour);
			if (state != updatedState) {
				level.setBlock(mutablePos, updatedState, 0);
			}

			BlockState updatedStateNeighbour = stateNeighbour.updateShape(direction.getOpposite(), updatedState, level, mutablePosNeighbour, mutablePos);
			if (stateNeighbour != updatedStateNeighbour) {
				level.setBlock(mutablePosNeighbour, updatedStateNeighbour, 0);
			}
		});
	}

	private void updateFromNeighbourShapes(WorldGenLevel level, DiscreteVoxelShape voxelShapePart) {
		MutableBlockPos mutablePos = new MutableBlockPos();

		IntUtil.forEachSectionCoord((x, y, z) -> {
			if (!voxelShapePart.isFull(x, y, z)) {
				return;
			}

			setPos(mutablePos, this.sectionPos, x, y, z);
			BlockState state = level.getBlockState(mutablePos);
			BlockState updatedState = Block.updateFromNeighbourShapes(state, level, mutablePos);
			if (state != updatedState) {
				level.setBlock(mutablePos, updatedState, 16);
			}

			level.blockUpdated(mutablePos, updatedState.getBlock());
		});
	}

	private void setBlockEntitiesChanged(WorldGenLevel level) {
		MutableBlockPos mutablePos = new MutableBlockPos();

		this.blockEntities.int2ObjectEntrySet().forEach(entry -> {
			setPos(mutablePos, this.sectionPos, entry.getIntKey());
			BlockEntity blockEntity = level.getBlockEntity(mutablePos);
			if (blockEntity != null) {
				blockEntity.setChanged();
			}
		});
	}

	private void addEntities(WorldGenLevel level, EntityFactory entityFactory) {
		this.entities.stream().map(entityContainer -> entityContainer.getEntity(entityFactory)).forEach(level::addFreshEntity);
	}

	private static MutableBlockPos setPos(MutableBlockPos mutablePos, SectionPos sectionPos, int index) {
		return setPos(mutablePos, sectionPos, x(index), y(index), z(index));
	}

	public static MutableBlockPos setPos(MutableBlockPos mutablePos, SectionPos sectionPos, int x, int y, int z) {
		return mutablePos.set(sectionPos.minBlockX() | x, sectionPos.minBlockY() | y, sectionPos.minBlockZ() | z);
	}

	private static BlockPos getPos(SectionPos sectionPos, int index) {
		return new BlockPos(sectionPos.minBlockX() | x(index), sectionPos.minBlockY() | y(index), sectionPos.minBlockZ() | z(index));
	}

	private static int index(BlockPos pos) {
		return index(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
	}

	private static int index(int x, int y, int z) {
		return y << 8 | z << 4 | x;
	}

	private static int x(int i) {
		return i & 15;
	}

	private static int y(int i) {
		return i >> 8;
	}

	private static int z(int i) {
		return (i >> 4) & 15;
	}

	@Nullable
	public BlockState getBlockState(BlockPos pos) {
		return this.getBlockState(index(pos));
	}

	@Nullable
	private BlockState getBlockState(int index) {
		return this.blocks.get(index);
	}

	public void setBlockState(BlockPos pos, @Nullable BlockState state, @Nullable Consumer<BlockEntity> blockEntityCallback) {
		this.setBlockState(index(pos), state, blockEntityCallback);
	}

	private void setBlockState(int index, @Nullable BlockState state, @Nullable Consumer<BlockEntity> blockEntityCallback) {
		this.blocks.set(index, state);
		if (state != null && state.hasBlockEntity()) {
			BlockEntity blockEntity = ((EntityBlock) state.getBlock()).newBlockEntity(getPos(sectionPos, index), state);
			this.blockEntities.put(index, blockEntity);
			if (blockEntityCallback != null) {
				blockEntityCallback.accept(blockEntity);
			}
		} else {
			this.blockEntities.remove(index);
		}
	}

	@Nullable
	public FluidState getFluidState(BlockPos pos) {
		BlockState blockState = this.getBlockState(pos);
		return blockState != null ? blockState.getFluidState() : null;
	}

	@Nullable
	public BlockEntity getBlockEntity(BlockPos pos) {
		return this.blockEntities.get(index(pos));
	}

	public void addEntity(Entity entity) {
		this.entities.add(new EntityContainer(entity));
	}

}
