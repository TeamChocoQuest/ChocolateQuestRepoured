package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainer.Strategy;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.nbt.NBTUtil;
import team.cqr.cqrepoured.common.primitive.IntUtil;
import team.cqr.cqrepoured.generation.init.CQRGenerationBlocks;
import team.cqr.cqrepoured.generation.util.Section;
import team.cqr.cqrepoured.generation.util.SectionUtil;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity.EntityFactory;

@SuppressWarnings("deprecation")
public class StructureSection extends Section<CompoundTag> {

	private static final Codec<PalettedContainer<BlockState>> BLOCK_STATE_CODEC = PalettedContainer.codecRW(Block.BLOCK_STATE_REGISTRY, BlockState.CODEC, PalettedContainer.Strategy.SECTION_STATES, CQRGenerationBlocks.PLACEHOLDER.get().defaultBlockState());

	private final PalettedContainer<BlockState> blocks;
	private final Int2ObjectMap<CompoundTag> blockEntities;
	private final List<CompoundTag> entities;

	public StructureSection(SectionPos sectionPos) {
		super(sectionPos);
		this.blocks = new PalettedContainer<>(Block.BLOCK_STATE_REGISTRY, CQRGenerationBlocks.PLACEHOLDER.get().defaultBlockState(), Strategy.SECTION_STATES);
		this.blockEntities = new Int2ObjectOpenHashMap<>();
		this.entities = new ArrayList<>();
	}

	public StructureSection(SectionPos sectionPos, CompoundTag nbt) {
		super(sectionPos);
		this.blocks = BLOCK_STATE_CODEC.parse(NbtOps.INSTANCE, nbt.get("BlockStates")).promotePartial(CQRepoured.LOGGER::error).getOrThrow(false, CQRepoured.LOGGER::error);
		this.blockEntities = NBTUtil.toInt2ObjectMap(nbt.getCompound("BlockEntities"), Function.identity());
		this.entities = NBTUtil.stream(nbt.get("Entities"), CompoundTag.TYPE).collect(Collectors.toList());
	}

	@Override
	public CompoundTag save() {
		CompoundTag nbt = new CompoundTag();
		nbt.put("BlockStates", BLOCK_STATE_CODEC.encodeStart(NbtOps.INSTANCE, this.blocks).getOrThrow(false, CQRepoured.LOGGER::error));
		nbt.put("BlockEntities", NBTUtil.collect(this.blockEntities, Function.identity()));
		nbt.put("Entities", this.entities.stream().collect(NBTUtil.toList()));
		return nbt;
	}

	public void generate(WorldGenLevel level, EntityFactory entityFactory) {
		DiscreteVoxelShape voxelShapePart = new BitSetDiscreteVoxelShape(16, 16, 16);

		this.placeBlocks(level, voxelShapePart);
		this.updateShapeAtEdge(level, voxelShapePart);
		this.updateFromNeighbourShapes(level, voxelShapePart);
		this.setBlockEntitiesChanged(level);
		this.addEntities(level, entityFactory);
	}

	private void placeBlocks(WorldGenLevel level, DiscreteVoxelShape voxelShapePart) {
		MutableBlockPos mutablePos = new MutableBlockPos();

		for (int i = 0; i < 4096; i++) {
			BlockState state = this.getBlockState(i);
			if (state == null) {
				continue;
			}

			int x = SectionUtil.x(i);
			int y = SectionUtil.y(i);
			int z = SectionUtil.z(i);
			SectionUtil.setPos(mutablePos, this.getPos(), x, y, z);

			if (level.setBlock(mutablePos, state, 0)) {
				voxelShapePart.fill(x, y, z);

				CompoundTag blockEntityTag = this.blockEntities.get(i);
				if (blockEntityTag != null) {
					BlockEntity blockEntity = level.getBlockEntity(mutablePos);
					if (blockEntity != null) {
						blockEntity.load(blockEntityTag);
					}
				}
			}
		}
	}

	private void updateShapeAtEdge(WorldGenLevel level, DiscreteVoxelShape voxelShapePart) {
		MutableBlockPos mutablePos = new MutableBlockPos();
		MutableBlockPos mutablePosNeighbour = new MutableBlockPos();

		voxelShapePart.forAllFaces((direction, x, y, z) -> {
			SectionUtil.setPos(mutablePos, this.getPos(), x, y, z);
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

			SectionUtil.setPos(mutablePos, this.getPos(), x, y, z);
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
			SectionUtil.setPos(mutablePos, this.getPos(), entry.getIntKey());
			BlockEntity blockEntity = level.getBlockEntity(mutablePos);
			if (blockEntity != null) {
				blockEntity.setChanged();
			}
		});
	}

	private void addEntities(WorldGenLevel level, EntityFactory entityFactory) {
		this.entities.stream().map(entityFactory::createEntity).forEach(level::addFreshEntity);
	}

	@Nullable
	public BlockState getBlockState(BlockPos pos) {
		return this.getBlockState(SectionUtil.index(pos));
	}

	@Nullable
	private BlockState getBlockState(int index) {
		BlockState state = this.blocks.get(index);
		return state != CQRGenerationBlocks.PLACEHOLDER.get().defaultBlockState() ? state : null;
	}

	public void setBlockState(BlockPos pos, @Nullable BlockState state, @Nullable CompoundTag blockEntityTag) {
		this.setBlockState(SectionUtil.index(pos), state, blockEntityTag);
	}

	private void setBlockState(int index, @Nullable BlockState state, @Nullable CompoundTag blockEntityTag) {
		if (state == null) {
			state = CQRGenerationBlocks.PLACEHOLDER.get().defaultBlockState();
		}

		this.blocks.set(index, state);
		if (state != null && state.hasBlockEntity() && blockEntityTag != null) {
			this.blockEntities.put(index, blockEntityTag);
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
	public CompoundTag getBlockEntity(BlockPos pos) {
		return this.blockEntities.get(SectionUtil.index(pos));
	}

	public void addEntity(CompoundTag entityTag) {
		this.entities.add(entityTag);
	}

}
