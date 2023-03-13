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
import net.minecraft.util.math.SectionPos;
import net.minecraft.util.palette.IPalette;
import net.minecraft.util.palette.IdentityPalette;
import net.minecraft.world.ISeedReader;
import net.minecraftforge.common.util.Constants.NBT;
import team.cqr.cqrepoured.util.NBTCollectors;
import team.cqr.cqrepoured.util.NBTHelper;
import team.cqr.cqrepoured.util.palette.PalettedContainer;

@SuppressWarnings("deprecation")
public class CQRSection implements ICQRSection {

	private static final IPalette<BlockState> GLOBAL_BLOCKSTATE_PALETTE = new IdentityPalette<>(Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState());

	private final CQRLevel level;
	private final PalettedContainer<BlockState> blocks;
	private final Int2ObjectMap<TileEntity> blockEntities;
	private final List<EntityContainer> entities;

	public CQRSection(CQRLevel level) {
		this.level = level;
		this.blocks = new PalettedContainer<>(GLOBAL_BLOCKSTATE_PALETTE, Block.BLOCK_STATE_REGISTRY, NBTUtil::readBlockState, NBTUtil::writeBlockState);
		this.blockEntities = new Int2ObjectOpenHashMap<>();
		this.entities = new ArrayList<>();
	}

	public CQRSection(CQRLevel level, CompoundNBT nbt) {
		this.level = level;
		this.blocks = new PalettedContainer<>(GLOBAL_BLOCKSTATE_PALETTE, Block.BLOCK_STATE_REGISTRY, NBTUtil::readBlockState, NBTUtil::writeBlockState);
		this.blocks.read(nbt.getList("Palette", NBT.TAG_COMPOUND), nbt.getLongArray("BlockStates"));
		this.blockEntities = NBTCollectors.<CompoundNBT, TileEntity>toInt2ObjectMap(nbt.getCompound("BlockEntities"), (index, blockEntityNbt) -> {
			return TileEntity.loadStatic(this.getBlockState(index), blockEntityNbt);
		});
		this.entities = NBTHelper.<CompoundNBT>stream(nbt, "Entities").map(EntityContainer::new).collect(Collectors.toList());
	}

	public CompoundNBT save() {
		CompoundNBT nbt = new CompoundNBT();
		this.blocks.write(nbt, "Palette", "BlockStates");
		nbt.put("BlockEntities", NBTCollectors.toCompound(this.blockEntities, blockEntity -> blockEntity.save(new CompoundNBT())));
		nbt.put("Entities", this.entities.stream().map(EntityContainer::getEntityNbt).filter(Objects::nonNull).collect(NBTCollectors.toList()));
		return nbt;
	}

	public void generate(ISeedReader pLevel, SectionPos sectionPos, IEntityFactory entityFactory) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (short i = 0; i < 4096; i++) {
			BlockState state = this.getBlockState(i);
			if (state == null) {
				continue;
			}

			mutable.set(
					(sectionPos.x() << 4) | (i & 15),
					(sectionPos.y() << 4) | ((i >> 8) & 15),
					(sectionPos.z() << 4) | ((i >> 4) & 15));
			pLevel.setBlock(mutable, state, 0);
			TileEntity tileEntity = this.blockEntities.get(i);
			if (tileEntity != null) {
				pLevel.getChunk(mutable).setBlockEntity(mutable, tileEntity);
			}
		}

		this.entities.stream().map(entityContainer -> entityContainer.getEntity(entityFactory)).forEach(pLevel::addFreshEntity);
	}

	private static int index(BlockPos pos) {
		return PalettedContainer.getIndex(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
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
			TileEntity blockEntity = state.createTileEntity(this.level);
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
