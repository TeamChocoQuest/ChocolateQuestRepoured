package team.cqr.cqrepoured.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant.DungeonInhabitant;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant.DungeonInhabitantManager;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;
import team.cqr.cqrepoured.network.datasync.DataEntryBoolean;
import team.cqr.cqrepoured.network.datasync.DataEntryInt;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;

public class TileEntitySpawner extends BlockEntityContainer implements ITileEntitySyncable<TileEntitySpawner> {

	private static final Random RANDOM = new Random();

	private final TileEntityDataManager dataManager = new TileEntityDataManager(this);

	private final DataEntryBoolean vanillaSpawner = new DataEntryBoolean("vanillaSpawner", false, true);
	private final DataEntryInt minSpawnDelay = new DataEntryInt("MinSpawnDelay", 200, true);
	private final DataEntryInt maxSpawnDelay = new DataEntryInt("MaxSpawnDelay", 800, true);
	private final DataEntryInt spawnCount = new DataEntryInt("SpawnCount", 4, true);
	private final DataEntryInt maxNearbyEntities = new DataEntryInt("MaxNearbyEntities", 6, true);
	private final DataEntryInt activatingRangeFromPlayer = new DataEntryInt("RequiredPlayerRange", 16, true);
	private final DataEntryInt spawnRange = new DataEntryInt("SpawnRange", 4, true);

	public TileEntitySpawner(BlockPos pos, BlockState state) {
		super(CQRBlockEntities.SPAWNER.get(), 9, pos, state);
		this.dataManager.register(this.vanillaSpawner);
		this.dataManager.register(this.minSpawnDelay);
		this.dataManager.register(this.maxSpawnDelay);
		this.dataManager.register(this.spawnCount);
		this.dataManager.register(this.maxNearbyEntities);
		this.dataManager.register(this.activatingRangeFromPlayer);
		this.dataManager.register(this.spawnRange);
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		this.dataManager.write(compound);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		this.dataManager.read(pTag);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithId();
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.dataManager.read(pkt.getTag());
	}
	
	@Override
	public void tick(Level pLevel, BlockPos pPos, BlockState pState, TileEntitySpawner pBlockEntity) {
		if (!pLevel.isClientSide && pLevel.getDifficulty() != Difficulty.PEACEFUL && pBlockEntity.isNonCreativePlayerInRange(CQRConfig.SERVER_CONFIG.general.spawnerActivationDistance.get())) {
			pBlockEntity.turnBackIntoEntity();
		} else {
			ITileEntitySyncable.super.tick(pLevel, pPos, pState, pBlockEntity);
		}
	}

	public void forceTurnBackIntoEntity() {
		if (!this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
			this.turnBackIntoEntity();
		}
	}

	protected void turnBackIntoEntity() {
		if (!this.level.isClientSide) {
			this.level.destroyBlock(this.worldPosition, false);

			List<CompoundTag> entitiesToSpawn = new ArrayList<>();

			for (int i = 0; i < this.inventory.getContainerSize(); i++) {
				ItemStack stack = this.inventory.getItem(i);

				if (!stack.isEmpty() && stack.hasTag()) {
					CompoundTag nbt = stack.getTag().getCompound("EntityIn");

					while (!stack.isEmpty()) {
						entitiesToSpawn.add(nbt);
						stack.shrink(1);
					}
				}
			}

			entitiesToSpawn.forEach(this::spawnEntityFromNBT);
		}
	}

	protected Entity spawnEntityFromNBT(CompoundTag entityTag) {
		if (entityTag.isEmpty()) {
			return null;
		}

		/*
		 * entityTag.removeTag("UUIDLeast"); entityTag.removeTag("UUIDMost"); entityTag.removeTag("Pos");
		 */

		// compatibility with old spawners
		if (entityTag.getString("id").equals(CQRConstants.MODID + ":dummy")) {
			DungeonInhabitant mobType = DungeonInhabitantManager.instance().getInhabitantByDistance(this.level, this.worldPosition.getX(), this.worldPosition.getZ());
			entityTag.putString("id", mobType.getEntityID().toString());
		}

		Entity entity = EntityList.createEntityFromNBT(entityTag, this.level);

		if (entity != null) {
			double offset = entity.getBbWidth() < 0.96F ? 0.5D - entity.getBbWidth() * 0.5D : 0.02D;
			double x = this.worldPosition.getX() + 0.5D + (RANDOM.nextDouble() - RANDOM.nextDouble()) * offset;
			double y = this.worldPosition.getY();
			double z = this.worldPosition.getZ() + 0.5D + (RANDOM.nextDouble() - RANDOM.nextDouble()) * offset;
			entity.setPos(x, y, z);

			this.level.addFreshEntity(entity);

			ListTag passengers = entityTag.getList("Passengers", Tag.TAG_COMPOUND);
			for (Tag passengerNBT : passengers) {
				Entity passenger = this.spawnEntityFromNBT((CompoundTag) passengerNBT);
				passenger.startRiding(entity);
			}
		}

		return entity;
	}

	protected boolean isNonCreativePlayerInRange(double range) {
		if (range > 0.0D) {
			double d = range * range;
			for (Player player : this.level.players()) {
				if (!player.isCreative() && !player.isSpectator() && player.distanceToSqr(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ()) < d) {
					return true;
				}
			}
		}
		return false;
	}

	public void setVanillaSpawner(int minSpawnDelay, int maxSpawnDelay, int spawnCount, int maxNearbyEntities, int activatingRangeFromPlayer, int spawnRange) {
		this.vanillaSpawner.set(true);
		this.minSpawnDelay.set(minSpawnDelay);
		this.maxSpawnDelay.set(maxSpawnDelay);
		this.spawnCount.set(spawnCount);
		this.maxNearbyEntities.set(maxNearbyEntities);
		this.activatingRangeFromPlayer.set(activatingRangeFromPlayer);
		this.spawnRange.set(spawnRange);
	}

	public void setCQRSpawner() {
		this.vanillaSpawner.set(false);
	}

	public boolean isVanillaSpawner() {
		return this.vanillaSpawner.getBoolean();
	}

	public int getMinSpawnDelay() {
		return this.minSpawnDelay.getInt();
	}

	public int getMaxSpawnDelay() {
		return this.maxSpawnDelay.getInt();
	}

	public int getSpawnCount() {
		return this.spawnCount.getInt();
	}

	public int getMaxNearbyEntities() {
		return this.maxNearbyEntities.getInt();
	}

	public int getActivatingRangeFromPlayer() {
		return this.activatingRangeFromPlayer.getInt();
	}

	public int getSpawnRange() {
		return this.spawnRange.getInt();
	}

	@Override
	protected Component getDefaultName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
		return CQRContainerTypes.SPAWNER.get().create(pContainerId, pInventory);
	}

	@Override
	public TileEntityDataManager getDataManager() {
		return this.dataManager;
	}

}
