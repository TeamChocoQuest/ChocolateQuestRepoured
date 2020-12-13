package team.cqr.cqrepoured.structuregen.structurefile;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.items.CapabilityItemHandler;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;
import team.cqr.cqrepoured.tileentity.TileEntityBoss;
import team.cqr.cqrepoured.util.BlockPlacingHelper;

public class BlockInfoBoss extends AbstractBlockInfo {

	private NBTTagCompound bossTag;

	public BlockInfoBoss(int x, int y, int z) {
		super(x, y, z);
	}

	public BlockInfoBoss(BlockPos pos) {
		super(pos);
	}

	public BlockInfoBoss(int x, int y, int z, NBTTagCompound bossTag) {
		this(x, y, z);
		this.bossTag = bossTag;
	}

	public BlockInfoBoss(BlockPos pos, NBTTagCompound bossTag) {
		this(pos.getX(), pos.getY(), pos.getZ(), bossTag);
	}

	public BlockInfoBoss(int x, int y, int z, TileEntityBoss tileEntityBoss) {
		this(x, y, z);

		if (tileEntityBoss.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			ItemStack stack = tileEntityBoss.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0);

			if (stack.hasTagCompound()) {
				NBTTagCompound stackTag = stack.getTagCompound();

				if (stackTag.hasKey("EntityIn", Constants.NBT.TAG_COMPOUND)) {
					this.bossTag = stackTag.getCompoundTag("EntityIn");
				}
			}
		}
	}

	public BlockInfoBoss(BlockPos pos, TileEntityBoss tileEntityBoss) {
		this(pos.getX(), pos.getY(), pos.getZ(), tileEntityBoss);
	}

	@Override
	public void generateAt(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion, BlockPos pos) {
		BlockPlacingHelper.setBlockState(world, pos, Blocks.AIR.getDefaultState(), 18, false);

		if (this.bossTag != null) {
			Entity entity = EntityList.createEntityFromNBT(this.bossTag, world);

			entity.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

			if (entity instanceof EntityLiving) {
				((EntityLiving) entity).enablePersistence();

				if (entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(dungeonPos, settings, dungeonMob);
					((AbstractEntityCQR) entity).enableBossBar();
				}
			}

			world.spawnEntity(entity);

			if (protectedRegion != null) {
				protectedRegion.addEntityDependency(entity.getPersistentID());
			}
		} else if (dungeonMob.getBossID() != null) {
			Entity entity = EntityList.createEntityByIDFromName(dungeonMob.getBossID(), world);

			entity.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

			if (entity instanceof EntityLiving) {
				((EntityLiving) entity).onInitialSpawn(world.getDifficultyForLocation(pos), null);
				((EntityLiving) entity).enablePersistence();

				if (entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(dungeonPos, settings, dungeonMob);
					((AbstractEntityCQR) entity).enableBossBar();
				}
			}

			world.spawnEntity(entity);

			if (protectedRegion != null) {
				protectedRegion.addEntityDependency(entity.getPersistentID());
			}
		} else if (dungeonMob.getEntityID() != null) {
			Entity entity = EntityList.createEntityByIDFromName(dungeonMob.getEntityID(), world);

			entity.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
			entity.setCustomNameTag("Temporary Boss");

			if (entity instanceof EntityLiving) {
				((EntityLiving) entity).onInitialSpawn(world.getDifficultyForLocation(pos), null);
				((EntityLiving) entity).enablePersistence();

				if (entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(dungeonPos, settings, dungeonMob);
					((AbstractEntityCQR) entity).setSizeVariation(1.1F);
					((AbstractEntityCQR) entity).enableBossBar();
				}

				((EntityLiving) entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
				((EntityLiving) entity).setHealth(((EntityLiving) entity).getMaxHealth());

				// Some gear
				((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(CQRItems.GREAT_SWORD_DIAMOND));
				((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(dungeonMob.getShieldReplacement()));

				((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(CQRItems.HELMET_HEAVY_DIAMOND));
				((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(CQRItems.CHESTPLATE_HEAVY_DIAMOND));
				((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(CQRItems.LEGGINGS_HEAVY_DIAMOND));
				((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(CQRItems.BOOTS_HEAVY_DIAMOND));
			}

			world.spawnEntity(entity);

			if (protectedRegion != null) {
				protectedRegion.addEntityDependency(entity.getPersistentID());
			}
		}
	}

	@Override
	public byte getId() {
		return BOSS_INFO_ID;
	}

	@Override
	protected void writeToByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		ByteBufUtils.writeVarInt(buf, this.bossTag != null ? (compoundTagList.tagCount() << 1) | 1 : 0, 5);
		if (this.bossTag != null) {
			compoundTagList.appendTag(this.bossTag);
		}
	}

	@Override
	protected void readFromByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		int i = ByteBufUtils.readVarInt(buf, 5);
		if ((i & 1) == 1) {
			this.bossTag = compoundTagList.getCompoundTagAt(i >>> 1);
		}
	}

}
