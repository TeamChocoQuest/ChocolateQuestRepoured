package team.cqr.cqrepoured.event;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLEnvironment;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.customtextures.TextureSetManager;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.IEquipListener;
import team.cqr.cqrepoured.item.IFakeWeapon;
import team.cqr.cqrepoured.item.ISupportWeapon;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager;
import team.cqr.cqrepoured.world.structure.generation.lootchests.LootTableLoader;

@EventBusSubscriber
public class EventsHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onLootTableLoadPre(LootTableLoadEvent event) {
		if (event.getName().getNamespace().equals(CQRMain.MODID) && !CQRConfig.SERVER_CONFIG.general.preventOtherModLoot.get()) {
			event.setTable(LootTableLoader.fillLootTable(event.getName(), event.getTable()));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onLootTableLoadPost(LootTableLoadEvent event) {
		if (event.getName().getNamespace().equals(CQRMain.MODID) && CQRConfig.SERVER_CONFIG.general.preventOtherModLoot.get()) {
			event.setTable(LootTableLoader.fillLootTable(event.getName(), event.getTable()));
		}
		LootTableLoader.freezeLootTable();
	}

	/*@SubscribeEvent
	public static void onDefense(LivingAttackEvent event) {
		boolean tep = false;

		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			Entity attacker = event.getSource().getEntity(();
			@SuppressWarnings("unused")
			float amount = event.getAmount();
			World world = player.level;

			if (player.getActiveItemStack().getItem() != CQRItems.SHIELD_WALKER_KING || player.getMainHandItem().getItem() != CQRItems.SWORD_WALKER || player.getRidingEntity() != null || attacker == null) {
				return;
			}

			double d = attacker.getX() + (attacker.level.rand.nextDouble() - 0.5D) * 4.0D;
			double d1 = attacker.getY();
			double d2 = attacker.getZ() + (attacker.level.rand.nextDouble() - 0.5D) * 4.0D;

			@SuppressWarnings("unused")
			double d3 = player.getX();
			@SuppressWarnings("unused")
			double d4 = player.getY();
			@SuppressWarnings("unused")
			double d5 = player.getZ();

			int i = MathHelper.floor(d);
			int j = MathHelper.floor(d1);
			int k = MathHelper.floor(d2);

			BlockPos ep = new BlockPos(i, j, k);
			BlockPos ep1 = new BlockPos(i, j + 1, k);

			if (world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(attacker.getEntityBoundingBox()) && player.isActiveItemStackBlocking() && player.getDistanceSq(attacker) >= 25.0D) {
				if (world.getBlockState(ep).getBlock().isPassable(world, ep) && world.getBlockState(ep1).getBlock().isPassable(world, ep1)) {
					tep = true;
				} else {
					tep = false;
					if (!world.isClientSide) {
						((ServerWorld) world).spawnParticle(ParticleTypes.SMOKE_LARGE, player.getX(), player.getY() + player.height * 0.5D, player.getZ(), 12, 0.25D, 0.25D, 0.25D, 0.0D);
					}
				}
			}

			if (tep) {
				if (world.getBlockState(ep).getBlock().isPassable(world, ep) && world.getBlockState(ep1).getBlock().isPassable(world, ep1)) {
					if (player instanceof ServerPlayerEntity) {
						ServerPlayerEntity playerMP = (ServerPlayerEntity) player;

						playerMP.connection.setPlayerLocation(d, d1, d2, playerMP.rotationYaw, playerMP.rotationPitch);
						if (!world.isClientSide) {
							((ServerWorld) world).spawnParticle(ParticleTypes.PORTAL, player.getX(), player.getY() + player.height * 0.5D, player.getZ(), 12, 0.25D, 0.25D, 0.25D, 0.0D);
						}
						world.playSound(null, d, d1, d2, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.MASTER, 1.0F, 1.0F);
					}
					event.setCanceled(true);
					tep = false;
				}
			}
		}
	}*/

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		Random rand = new Random();
		Entity entity = event.getEntity();
		CompoundNBT tag = entity.getPersistentData();

		if (tag.contains("Items")) {
			ListNBT itemList = tag.getList("Items", Constants.NBT.TAG_COMPOUND);

			if (itemList == null) {
				return;
			}

			for (int i = 0; i < itemList.size(); i++) {
				CompoundNBT entry = itemList.getCompound(i);
				ItemStack stack = ItemStack.of(entry);

				if (!stack.isEmpty()) {
					if (!entity.level.isClientSide) {
						entity.level.addFreshEntity(new ItemEntity(entity.level, entity.getX() + rand.nextDouble(), entity.getY(), entity.getZ() + rand.nextDouble(), stack));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load e) {
		DungeonDataManager.handleWorldLoad(e.getWorld());

		if(e.getWorld() instanceof World) {
			if (!e.getWorld().isClientSide() && ((World)e.getWorld()).dimension().equals(DimensionType.OVERWORLD_LOCATION)) {
				LootTableLoader.registerCustomLootTables((ServerWorld) e.getWorld());
			}
		}
	}

	@SubscribeEvent
	public static void onWorldCreateSpawnpoint(WorldEvent.CreateSpawnPosition e) {
		DungeonDataManager.handleWorldLoad(e.getWorld());
	}

	@SubscribeEvent
	public static void onWorldSave(WorldEvent.Save e) {
		DungeonDataManager.handleWorldSave(e.getWorld());
		FactionRegistry.instance(e.getWorld()).saveAllReputationData(false, e.getWorld());
	}

	/*@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().register(new RecipesArmorDyes().setRegistryName(CQRMain.MODID, "armor_coloring"));
		event.getRegistry().register(new RecipeArmorDyableRainbow());
		event.getRegistry().register(new RecipeArmorDyableBreathing());
		event.getRegistry().register(new RecipeCrownAttach());
		event.getRegistry().register(new RecipeCrownDetach());
		// event.getRegistry().register(new RecipeDynamicCrown().setRegistryName(Reference.MODID, "dynamic_king_crown"));
	}*/

	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload e) {
		if (!e.getWorld().isClientSide()) {
			DungeonDataManager.handleWorldUnload(e.getWorld());
		}
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event) {
		FactionRegistry.instance(event.getPlayer()).loadPlayerReputationData(event.getPlayer());
		FactionRegistry.instance(event.getPlayer()).syncPlayerReputationData((ServerPlayerEntity) event.getPlayer());

		// Send packets with ct's to player
		if (FMLEnvironment.dist.isDedicatedServer() || !CQRMain.PROXY.isOwnerOfIntegratedServer(event.getPlayer())) {
			TextureSetManager.sendTexturesToClient((ServerPlayerEntity) event.getPlayer());
		}
	}

	@SubscribeEvent
	public static void onPlayerLogout(PlayerLoggedOutEvent event) {
		FactionRegistry.instance(event.getPlayer()).savePlayerReputationData((ServerPlayerEntity) event.getPlayer());
	}

	@SubscribeEvent
	public static void onAttackEntityEvent(AttackEntityEvent event) {
		if (CQRConfig.SERVER_CONFIG.mobs.blockCancelledByAxe.get()) {
			PlayerEntity player = event.getPlayer();
			World world = player.level;

			if (!world.isClientSide && event.getTarget() instanceof AbstractEntityCQR) {
				AbstractEntityCQR targetCQR = (AbstractEntityCQR) event.getTarget();

				if (targetCQR.canBlockDamageSource(DamageSource.playerAttack(player)) && player.getMainHandItem().getItem() instanceof AxeItem && player.getAttackStrengthScale(0) >= 0.9F) {
					targetCQR.setLastTimeHitByAxeWhileBlocking(targetCQR.tickCount);
				}
			}
		}
	}

	@SubscribeEvent
	public static void sayNoToCowardlyPlacingLavaAgainstBosses(FillBucketEvent event) {
		if (CQRConfig.SERVER_CONFIG.bosses.antiCowardMode.get() && event.getPlayer() != null && !event.getPlayer().isCreative()) {
			BlockPos pos = event.getPlayer().blockPosition();
			int radius = CQRConfig.SERVER_CONFIG.bosses.antiCowardRadius.get();
			AxisAlignedBB aabb = new AxisAlignedBB(pos.offset(-radius, -radius / 2, -radius), pos.offset(radius, radius / 2, radius));
			event.setCanceled(!event.getWorld().getEntitiesOfClass(AbstractEntityCQRBoss.class, aabb).isEmpty());
		}
	}

	@SubscribeEvent
	public static void sayNoToPlacingBlocksNearBosses(BlockEvent.EntityPlaceEvent event) {
		if (CQRConfig.SERVER_CONFIG.bosses.preventBlockPlacingNearBosses.get() && event.getEntity() != null && (!(event.getEntity() instanceof PlayerEntity) || !((PlayerEntity) event.getEntity()).isCreative())) {
			BlockPos pos = event.getEntity().blockPosition();
			int radius = CQRConfig.SERVER_CONFIG.bosses.antiCowardRadius.get();
			AxisAlignedBB aabb = new AxisAlignedBB(pos.offset(-radius, -radius / 2, -radius), pos.offset(radius, radius / 2, radius));
			event.setCanceled(!event.getWorld().getEntitiesOfClass(AbstractEntityCQRBoss.class, aabb).isEmpty());
		}
	}

	@SubscribeEvent
	public static void onPlayerLeaderAttackedEvent(LivingAttackEvent event) {
		if (event.getEntity().level.isClientSide) {
			return;
		}
		if (!(event.getEntity() instanceof PlayerEntity)) {
			return;
		}
		if (!(event.getSource().getEntity() instanceof LivingEntity)) {
			return;
		}
		PlayerEntity player = (PlayerEntity) event.getEntity();
		LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
		double x = player.getX();
		double y = player.getY() + player.getEyeHeight();
		double z = player.getZ();
		double r = 8.0D;
		AxisAlignedBB aabb = new AxisAlignedBB(x - r, y - r * 0.5D, z - r, x + r, y + r * 0.5D, z + r);
		for (AbstractEntityCQR entity : player.level.getEntitiesOfClass(AbstractEntityCQR.class, aabb, e -> (e.getLeader() == player))) {
			ItemStack stack = entity.getMainHandItem();
			if (stack.getItem() instanceof ISupportWeapon) {
				continue;
			}
			if (stack.getItem() instanceof IFakeWeapon) {
				continue;
			}
			if (entity.hasAttackTarget()) {
				continue;
			}
			entity.setTarget(attacker);
		}
	}

	@SubscribeEvent
	public static void onPlayerLeaderAttackingEvent(AttackEntityEvent event) {
		if (event.getPlayer().level.isClientSide) {
			return;
		}
		if (!(event.getTarget() instanceof LivingEntity)) {
			return;
		}
		PlayerEntity player = event.getPlayer();
		LivingEntity target = (LivingEntity) event.getTarget();
		double x = player.getX();
		double y = player.getY() + player.getEyeHeight();
		double z = player.getZ();
		double r = 8.0D;
		AxisAlignedBB aabb = new AxisAlignedBB(x - r, y - r * 0.5D, z - r, x + r, y + r * 0.5D, z + r);
		for (AbstractEntityCQR entity : player.level.getEntitiesOfClass(AbstractEntityCQR.class, aabb, e -> (e.getLeader() == player))) {
			ItemStack stack = entity.getMainHandItem();
			if (stack.getItem() instanceof ISupportWeapon) {
				continue;
			}
			if (stack.getItem() instanceof IFakeWeapon) {
				continue;
			}
			entity.setTarget(target);
		}
	}

	@SubscribeEvent
	public static void onLivingFallEvent(LivingFallEvent event) {
		LivingEntity entity = event.getEntityLiving();
		ItemStack feet = entity.getItemBySlot(EquipmentSlotType.FEET);
		ItemStack mainhand = entity.getItemBySlot(EquipmentSlotType.MAINHAND);
		ItemStack offhand = entity.getItemBySlot(EquipmentSlotType.OFFHAND);

		if (feet.getItem() == CQRItems.BOOTS_CLOUD.get()) {
			event.setDistance(0.0F);
		} else if (mainhand.getItem() == CQRItems.FEATHER_GOLDEN.get()) {
			mainhand.hurtAndBreak((int) event.getDistance(), entity, e -> e.broadcastBreakEvent(e.getUsedItemHand()));
			event.setDistance(0.0F);
		} else if (offhand.getItem() == CQRItems.FEATHER_GOLDEN.get()) {
			offhand.hurtAndBreak((int) event.getDistance(), entity, e -> e.broadcastBreakEvent(e.getUsedItemHand()));
			event.setDistance(0.0F);
		}
	}

	@SubscribeEvent
	public static void onLivingEquipmentChangeEvent(LivingEquipmentChangeEvent event) {
		EquipmentSlotType slot = event.getSlot();
		if (slot != EquipmentSlotType.MAINHAND) {
			return;
		}
		LivingEntity entity = event.getEntityLiving();
		ItemStack stackOld = event.getFrom();
		if (stackOld.getItem() instanceof IEquipListener) {
			((IEquipListener) stackOld.getItem()).onUnequip(entity, stackOld, slot);
		}
		ItemStack stackNew = event.getTo();
		if (stackNew.getItem() instanceof IEquipListener) {
			((IEquipListener) stackNew.getItem()).onEquip(entity, stackNew, slot);
		}
	}

}
