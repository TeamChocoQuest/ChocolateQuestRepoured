package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.SectionPos;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShapePart;
import net.minecraft.util.palette.IPalette;
import net.minecraft.util.palette.IdentityPalette;
import net.minecraft.world.ISeedReader;
import net.minecraftforge.common.util.Constants.NBT;
import team.cqr.cqrepoured.util.IntUtil;
import team.cqr.cqrepoured.util.NBTCollectors;
import team.cqr.cqrepoured.util.NBTHelper;
import team.cqr.cqrepoured.util.palette.PalettedContainer;

@SuppressWarnings("deprecation")
public class CQRSection implements ICQRSection {

	private static final IPalette<BlockState> GLOBAL_BLOCKSTATE_PALETTE = new IdentityPalette<>(Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState());

	private final CQRLevel level;
	private final SectionPos sectionPos;
	private final PalettedContainer<BlockState> blocks;
	private final Int2ObjectMap<TileEntity> blockEntities;
	private final List<EntityContainer> entities;

	public CQRSection(CQRLevel level, SectionPos sectionPos) {
		this.level = level;
		this.sectionPos = sectionPos;
		this.blocks = new PalettedContainer<>(GLOBAL_BLOCKSTATE_PALETTE, Block.BLOCK_STATE_REGISTRY, NBTUtil::readBlockState, NBTUtil::writeBlockState);
		this.blockEntities = new Int2ObjectOpenHashMap<>();
		this.entities = new ArrayList<>();
	}

	public CQRSection(CQRLevel level, CompoundNBT nbt) {
		this.level = level;
		this.sectionPos = SectionPos.of(nbt.getInt("X"), nbt.getInt("Y"), nbt.getInt("Z"));
		this.blocks = new PalettedContainer<>(GLOBAL_BLOCKSTATE_PALETTE, Block.BLOCK_STATE_REGISTRY, NBTUtil::readBlockState, NBTUtil::writeBlockState);
		this.blocks.read(nbt.getList("Palette", NBT.TAG_COMPOUND), nbt.getLongArray("BlockStates"));
		this.blockEntities = NBTCollectors.<CompoundNBT, TileEntity>toInt2ObjectMap(nbt.getCompound("BlockEntities"), (index, blockEntityNbt) -> {
			return TileEntity.loadStatic(this.getBlockState(index), blockEntityNbt);
		});
		this.entities = NBTHelper.<CompoundNBT>stream(nbt, "Entities").map(EntityContainer::new).collect(Collectors.toList());
	}

	public SectionPos getPos() {
		return this.sectionPos;
	}

	public CompoundNBT save() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("X", this.sectionPos.x());
		nbt.putInt("Y", this.sectionPos.y());
		nbt.putInt("Z", this.sectionPos.z());
		this.blocks.write(nbt, "Palette", "BlockStates");
		nbt.put("BlockEntities", NBTCollectors.toCompound(this.blockEntities, blockEntity -> blockEntity.save(new CompoundNBT())));
		nbt.put("Entities", this.entities.stream().map(EntityContainer::getEntityNbt).filter(Objects::nonNull).collect(NBTCollectors.toList()));
		return nbt;
	}

	public void generate(ISeedReader level, IEntityFactory entityFactory) {
		VoxelShapePart voxelShapePart = new BitSetVoxelShapePart(16, 16, 16);

		this.placeBlocks(level, voxelShapePart);
		this.updateShapeAtEdge(level, voxelShapePart);
		this.updateFromNeighbourShapes(level, voxelShapePart);
		this.setBlockEntitiesChanged(level);
		this.addEntities(level, entityFactory);
	}

	private void placeBlocks(ISeedReader level, VoxelShapePart voxelShapePart) {
		Mutable mutablePos = new Mutable();

		for (int i = 0; i < 4096; i++) {
			BlockState state = this.getBlockState(i);
			if (state == null) {
				continue;
			}

			int x = x(i);
			int y = y(i);
			int z = z(i);
			setPos(mutablePos, this.sectionPos, x, y, z);
			if (level.setBlock(mutablePos, state, 0)) {
				voxelShapePart.setFull(x, y, z, true, true);

				TileEntity tileEntity = this.blockEntities.get(i);
				if (tileEntity != null) {
					level.getChunk(mutablePos).setBlockEntity(mutablePos, tileEntity);
				}
			}
		}
	}

	private void updateShapeAtEdge(ISeedReader level, VoxelShapePart voxelShapePart) {
		Mutable mutablePos = new Mutable();
		Mutable mutablePosNeighbour = new Mutable();

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

	private void updateFromNeighbourShapes(ISeedReader level, VoxelShapePart voxelShapePart) {
		Mutable mutablePos = new Mutable();

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

	private void setBlockEntitiesChanged(ISeedReader level) {
		Mutable mutablePos = new Mutable();

		this.blockEntities.int2ObjectEntrySet().forEach(entry -> {
			setPos(mutablePos, this.sectionPos, entry.getIntKey());
			TileEntity blockEntity = level.getBlockEntity(mutablePos);
			if (blockEntity != null) {
				blockEntity.setChanged();
			}
		});
	}

	private void addEntities(ISeedReader level, IEntityFactory entityFactory) {
		this.entities.stream().map(entityContainer -> entityContainer.getEntity(entityFactory)).forEach(level::addFreshEntity);
	}

	private static Mutable setPos(Mutable mutablePos, SectionPos sectionPos, int index) {
		return setPos(mutablePos, sectionPos, x(index), y(index), z(index));
	}

	public static Mutable setPos(Mutable mutablePos, SectionPos sectionPos, int x, int y, int z) {
		return mutablePos.set(sectionPos.minBlockX() | x, sectionPos.minBlockY() | y, sectionPos.minBlockZ() | z);
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

	@Override
	@Nullable
	public BlockState getBlockState(BlockPos pos) {
		return this.getBlockState(index(pos));
	}

	@Nullable
	private BlockState getBlockState(int index) {
		return this.blocks.get(index);
	}

	@Override
	public void setBlockState(BlockPos pos, @Nullable BlockState state, @Nullable Consumer<TileEntity> blockEntityCallback) {
		this.setBlockState(index(pos), state, blockEntityCallback);
	}

	private void setBlockState(int index, @Nullable BlockState state, @Nullable Consumer<TileEntity> blockEntityCallback) {
		this.blocks.set(index, state);
		if (state != null && state.hasTileEntity()) {
			TileEntity blockEntity = state.createTileEntity(this.level.asBlockReader());
			this.blockEntities.put(index, blockEntity);
			if (blockEntityCallback != null) {
				blockEntityCallback.accept(blockEntity);
			}
		} else {
			this.blockEntities.remove(index);
		}
	}

	@Override
	@Nullable
	public FluidState getFluidState(BlockPos pos) {
		BlockState blockState = this.getBlockState(pos);
		return blockState != null ? blockState.getFluidState() : null;
	}

	@Override
	@Nullable
	public TileEntity getBlockEntity(BlockPos pos) {
		return this.blockEntities.get(index(pos));
	}

	@Override
	public void addEntity(Entity entity) {
		this.entities.add(new EntityContainer(entity));
	}

}
