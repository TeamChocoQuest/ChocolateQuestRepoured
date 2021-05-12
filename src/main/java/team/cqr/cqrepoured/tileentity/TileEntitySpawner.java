package team.cqr.cqrepoured.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.network.datasync.DataEntryBoolean;
import team.cqr.cqrepoured.network.datasync.DataEntryInt;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.structureprot.ProtectedRegionHelper;
import team.cqr.cqrepoured.util.Reference;

public class TileEntitySpawner extends TileEntity implements ITileEntitySyncable {

	private static final Random RANDOM = new Random();
	public final ItemStackHandler inventory = new ItemStackHandler(9) {
		@Override
		protected void onContentsChanged(int slot) {
			if (TileEntitySpawner.this.world != null && !TileEntitySpawner.this.world.isRemote) {
				TileEntitySpawner.this.markDirty();
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

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory : super.getCapability(capability, facing);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("inventory", this.inventory.serializeNBT());
		this.dataManager.write(compound);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		this.dataManager.read(compound);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.dataManager.write(new NBTTagCompound()));
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.dataManager.read(pkt.getNbtCompound());
	}

	@Nullable
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(I18n.format("tile.spawner.name"));
	}

	@Override
	public void update() {
		if (!this.world.isRemote && this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.isNonCreativePlayerInRange(CQRConfig.general.spawnerActivationDistance)) {
			this.turnBackIntoEntity();
		} else {
			this.getDataManager().checkIfDirtyAndSync();
		}
	}

	public void forceTurnBackIntoEntity() {
		if (!this.world.isRemote && this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
			this.turnBackIntoEntity();
		}
	}

	protected void turnBackIntoEntity() {
		if (!this.world.isRemote) {
			this.world.setBlockToAir(this.pos);

			List<NBTTagCompound> entitiesToSpawn = new ArrayList<>();

			for (int i = 0; i < this.inventory.getSlots(); i++) {
				ItemStack stack = this.inventory.getStackInSlot(i);

				if (!stack.isEmpty() && stack.hasTagCompound()) {
					NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("EntityIn");

					while (!stack.isEmpty()) {
						entitiesToSpawn.add(nbt);
						stack.shrink(1);
					}
				}
			}

			if (!entitiesToSpawn.isEmpty()) {
				// Calculate additional entities
				if (CQRConfig.advanced.scaleEntitiesOnPlayerCount) {
					int playerCount = ProtectedRegionHelper.getEntitiesInProtectedRegionAt(EntityPlayer.class, this.pos, this.world).size();
					playerCount--;
					if (playerCount > 0) {
						int additionalEntities = (int) Math.round(entitiesToSpawn.size() * (playerCount * CQRConfig.advanced.entityCountGrowPerPlayer));
						List<NBTTagCompound> additionals = new ArrayList<>(additionalEntities);
						for (int i = 0; i < additionalEntities; i++) {
							additionals.add(entitiesToSpawn.get(TileEntitySpawner.RANDOM.nextInt(entitiesToSpawn.size())));
						}
						if (!additionals.isEmpty()) {
							entitiesToSpawn.addAll(additionals);
						}
					}
				}
			}

			// Now, spawn them all
			entitiesToSpawn.forEach((NBTTagCompound nbt) -> spawnEntityFromNBT(nbt));
		}
	}

	protected Entity spawnEntityFromNBT(NBTTagCompound entityTag) {
		if (entityTag.isEmpty()) {
			return null;
		}

		/*
		 * entityTag.removeTag("UUIDLeast"); entityTag.removeTag("UUIDMost"); entityTag.removeTag("Pos");
		 */

		// compatibility with old spawners
		if (entityTag.getString("id").equals(Reference.MODID + ":dummy")) {
			DungeonInhabitant mobType = DungeonInhabitantManager.instance().getInhabitantByDistance(this.world, this.pos.getX(), this.pos.getZ());
			entityTag.setString("id", mobType.getEntityID().toString());
		}

		Entity entity = EntityList.createEntityFromNBT(entityTag, this.world);

		if (entity != null) {
			double offset = entity.width < 0.96F ? 0.5D - entity.width * 0.5D : 0.02D;
			double x = this.pos.getX() + 0.5D + (RANDOM.nextDouble() - RANDOM.nextDouble()) * offset;
			double y = this.pos.getY();
			double z = this.pos.getZ() + 0.5D + (RANDOM.nextDouble() - RANDOM.nextDouble()) * offset;
			entity.setPosition(x, y, z);

			this.world.spawnEntity(entity);

			NBTTagList passengers = entityTag.getTagList("Passengers", Constants.NBT.TAG_COMPOUND);
			for (NBTBase passengerNBT : passengers) {
				Entity passenger = this.spawnEntityFromNBT((NBTTagCompound) passengerNBT);
				passenger.startRiding(entity);
			}
		}

		return entity;
	}

	protected boolean isNonCreativePlayerInRange(double range) {
		if (range > 0.0D) {
			double d = range * range;
			for (EntityPlayer player : this.world.playerEntities) {
				if (!player.isCreative() && !player.isSpectator() && player.getDistanceSqToCenter(this.pos) < d) {
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
