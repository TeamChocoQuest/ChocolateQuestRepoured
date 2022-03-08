package team.cqr.cqrepoured.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.Difficulty;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.network.datasync.DataEntryBoolean;
import team.cqr.cqrepoured.network.datasync.DataEntryInt;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitantManager;

public class TileEntitySpawner extends TileEntity implements ITileEntitySyncable {

	private static final Random RANDOM = new Random();
	public final ItemStackHandler inventory = new ItemStackHandler(9) {
		@Override
		protected void onContentsChanged(int slot) {
			if (TileEntitySpawner.this.level != null && !TileEntitySpawner.this.level.isClientSide) {
				TileEntitySpawner.this.setChanged();
			}
		}
	};

	private final TileEntityDataManager dataManager = new TileEntityDataManager(this);

	private final DataEntryBoolean vanillaSpawner = new DataEntryBoolean("vanillaSpawner", false, true);
	private final DataEntryInt minSpawnDelay = new DataEntryInt("minSpawnDelay", 200, true);
	private final DataEntryInt maxSpawnDelay = new DataEntryInt("maxSpawnDelay", 800, true);
	private final DataEntryInt spawnCount = new DataEntryInt("spawnCount", 4, true);
	private final DataEntryInt maxNearbyEntities = new DataEntryInt("maxNearbyEntities", 6, true);
	private final DataEntryInt activatingRangeFromPlayer = new DataEntryInt("activatingRangeFromPlayer", 16, true);
	private final DataEntryInt spawnRange = new DataEntryInt("spawnRange", 4, true);

	public TileEntitySpawner() {
		super(CQRBlockEntities.SPAWNER.get());
		this.dataManager.register(this.vanillaSpawner);
		this.dataManager.register(this.minSpawnDelay);
		this.dataManager.register(this.maxSpawnDelay);
		this.dataManager.register(this.spawnCount);
		this.dataManager.register(this.maxNearbyEntities);
		this.dataManager.register(this.activatingRangeFromPlayer);
		this.dataManager.register(this.spawnRange);
	}

	@Override
	public TileEntityDataManager getDataManager() {
		return this.dataManager;
	}

	//No longer needed?
	/*@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory : super.getCapability(capability, facing);
	}*/

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.put("inventory", this.inventory.serializeNBT());
		this.dataManager.write(compound);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.inventory.deserializeNBT(compound.getCompound("inventory"));
		this.dataManager.read(compound);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.worldPosition, 0, this.dataManager.write(new CompoundNBT()));
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.save(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.dataManager.read(pkt.getTag());
	}

	/*Gone?
	@Nullable
	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent(I18n.format("tile.spawner.name"));
	}
	*/

	@Override
	public void tick() {
		if (!this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL && this.isNonCreativePlayerInRange(CQRConfig.general.spawnerActivationDistance)) {
			this.turnBackIntoEntity();
		} else {
			this.getDataManager().checkIfDirtyAndSync();
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

			List<CompoundNBT> entitiesToSpawn = new ArrayList<>();

			for (int i = 0; i < this.inventory.getSlots(); i++) {
				ItemStack stack = this.inventory.getStackInSlot(i);

				if (!stack.isEmpty() && stack.hasTag()) {
					CompoundNBT nbt = stack.getTag().getCompound("EntityIn");

					while (!stack.isEmpty()) {
						entitiesToSpawn.add(nbt);
						stack.shrink(1);
					}
				}
			}

			entitiesToSpawn.forEach(this::spawnEntityFromNBT);
		}
	}

	protected Entity spawnEntityFromNBT(CompoundNBT entityTag) {
		if (entityTag.isEmpty()) {
			return null;
		}

		/*
		 * entityTag.removeTag("UUIDLeast"); entityTag.removeTag("UUIDMost"); entityTag.removeTag("Pos");
		 */

		// compatibility with old spawners
		if (entityTag.getString("id").equals(CQRMain.MODID + ":dummy")) {
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

			ListNBT passengers = entityTag.getList("Passengers", Constants.NBT.TAG_COMPOUND);
			for (INBT passengerNBT : passengers) {
				Entity passenger = this.spawnEntityFromNBT((CompoundNBT) passengerNBT);
				passenger.startRiding(entity);
			}
		}

		return entity;
	}

	protected boolean isNonCreativePlayerInRange(double range) {
		if (range > 0.0D) {
			double d = range * range;
			for (PlayerEntity player : this.level.players()) {
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

}
