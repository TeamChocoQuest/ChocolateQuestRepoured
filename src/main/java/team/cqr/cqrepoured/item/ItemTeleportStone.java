package team.cqr.cqrepoured.item;

import java.util.List;

import javax.annotation.Nullable;

import net.java.games.input.Keyboard;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.network.play.server.SRespawnPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

public class ItemTeleportStone extends ItemLore {

	private static final String X = "x";
	private static final String Y = "y";
	private static final String Z = "z";
	private static final String DIMENSION = "dimension";

	public ItemTeleportStone(Properties properties)
	{
		super(properties.durability(100));
		//this.setMaxDamage(100);
		//this.setMaxStackSize(1);
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

		if (isSelected && entityIn instanceof PlayerEntity && worldIn.isClientSide && worldIn.getGameTime() % 4 == 0) {
			CompoundNBT tag = stack.getTag();
			if (tag != null && tag.contains(X) && tag.contains(Y) && tag.contains(Z) && tag.contains(DIMENSION) && worldIn.dimensionType().toString().equals(tag.getString(DIMENSION))) {
				double x = MathHelper.floor(tag.getDouble(X)) + MathHelper.clamp(worldIn.random.nextGaussian() * 0.3D, -0.5D, 0.5D);
				double y = MathHelper.floor(tag.getDouble(Y)) + MathHelper.clamp(worldIn.random.nextGaussian() * 0.1D, -0.1D, 0.1D);
				double z = MathHelper.floor(tag.getDouble(Z)) + MathHelper.clamp(worldIn.random.nextGaussian() * 0.3D, -0.5D, 0.5D);
				worldIn.addParticle(ParticleTypes.DRAGON_BREATH, x + 0.5D, y + 0.1D, z + 0.5D, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 40;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		playerIn.startUsingItem(handIn);
		return ActionResult.success(stack);
	}

	/**
	 * Taken from CoFHCore's EntityHelper
	 * (https://github.com/CoFH/CoFHCore/blob/1.12/src/main/java/cofh/core/util/helpers/EntityHelper.java)
	 */
	private static void transferPlayerToDimension(ServerPlayerEntity player, int dimension, PlayerList manager) {
		int oldDim = player.dimension;
		ServerWorld oldWorld = manager.getServerInstance().getWorld(player.dimension);
		player.dimension = dimension;
		ServerWorld newWorld = manager.getServerInstance().getWorld(player.dimension);
		player.connection.sendPacket(new SRespawnPacket(player.dimension, newWorld.getDifficulty(), newWorld.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
		oldWorld.removeEntityDangerously(player);
		if (player.isBeingRidden()) {
			player.removePassengers();
		}
		if (player.isRiding()) {
			player.dismountRidingEntity();
		}
		player.isDead = false;
		transferEntityToWorld(player, oldWorld, newWorld);
		manager.preparePlayer(player, oldWorld);
		player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		player.interactionManager.setWorld(newWorld);
		manager.updateTimeAndWeatherForPlayer(player, newWorld);
		manager.syncPlayerInventory(player);

		for (EffectInstance potioneffect : player.getActivePotionEffects()) {
			player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), potioneffect));
		}
		FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
	}

	/**
	 * Taken from CoFHCore's EntityHelper
	 * (https://github.com/CoFH/CoFHCore/blob/1.12/src/main/java/cofh/core/util/helpers/EntityHelper.java)
	 */
	private static void transferEntityToWorld(Entity entity, ServerWorld oldWorld, ServerWorld newWorld) {
		net.minecraft.world.dimension.Dimension oldWorldProvider = oldWorld.provider;
		net.minecraft.world.dimension.Dimension newWorldProvider = newWorld.provider;
		double moveFactor = oldWorldProvider.getMovementFactor() / newWorldProvider.getMovementFactor();
		double x = entity.posX * moveFactor;
		double z = entity.posZ * moveFactor;

		oldWorld.profiler.startSection("placing");
		x = MathHelper.clamp(x, -29_999_872, 29_999_872);
		z = MathHelper.clamp(z, -29_999_872, 29_999_872);
		if (entity.isEntityAlive()) {
			entity.setLocationAndAngles(x, entity.posY, z, entity.rotationYaw, entity.rotationPitch);
			newWorld.spawnEntity(entity);
			newWorld.updateEntityWithOptionalForce(entity, false);
		}
		oldWorld.profiler.endSection();

		entity.setWorld(newWorld);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (entityLiving instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
			player.getCooldowns().addCooldown(stack.getItem(), 60);

			if (player.isCrouching() && stack.hasTag()) {
				stack.getTag().remove(X);
				stack.getTag().remove(Y);
				stack.getTag().remove(Z);
				worldIn.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
				for (int i = 0; i < 10; i++) {
					worldIn.addParticle(ParticleTypes.LARGE_SMOKE, player.getX() + worldIn.random.nextDouble() - 0.5D, player.getY() + 0.5D, player.getZ() + worldIn.random.nextDouble() - 0.5D, 0D, 0D, 0D);
				}
			}

			else if (this.getPoint(stack) == null || !stack.hasTag()) {
				this.setPoint(stack, player);
				for (int i = 0; i < 10; i++) {
					worldIn.addParticle(ParticleTypes.FLAME, player.getX() + worldIn.random.nextDouble() - 0.5D, player.getY() + 0.5D, player.getZ() + worldIn.random.nextDouble() - 0.5D, 0D, 0D, 0D);
				}
				worldIn.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundCategory.AMBIENT, 1.0F, 1.0F, false);

				return super.finishUsingItem(stack, worldIn, entityLiving);
			}

			else if (stack.hasTag() && !player.isCrouching()) {
				if (stack.getTag().contains(X) && stack.getTag().contains(Y) && stack.getTag().contains(Z)) {
					String dimension = stack.getTag().contains(DIMENSION, Constants.NBT.TAG_STRING) ? stack.getTag().getString(DIMENSION) : DimensionType.OVERWORLD_EFFECTS.toString();
					BlockPos pos = this.getPoint(stack);

					if (player.isVehicle()) {
						player.ejectPassengers();
					}
					if (player.isPassenger()) {
						player.stopRiding();
					}

					if (!dimension.equals(player.getLevel().dimensionType().effectsLocation())) {
						MinecraftServer server = player.level.getServer();
						if (server != null) {
							transferPlayerToDimension(player, dimension, server.getPlayerList());
						}
					}
					player.teleportTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
					// player.attemptTeleport(stack.getTagCompound().getDouble(this.X), stack.getTagCompound().getDouble(this.Y),
					// stack.getTagCompound().getDouble(this.Z));
					/*
					 * if(worldIn.provider.getDimension() != dimension) {
					 * 
					 * WorldServer worldServer = player.getServer().getWorld(dimension);
					 * WorldServer worldServerOld = player.getServerWorld();
					 * player.moveToBlockPosAndAngles(new BlockPos(stack.getTagCompound().getDouble(this.X),
					 * stack.getTagCompound().getDouble(this.Y),
					 * stack.getTagCompound().getDouble(this.Z)), player.rotationYaw, player.rotationPitch);
					 * worldServerOld.removeEntity(player);
					 * boolean flag = player.forceSpawn;
					 * player.forceSpawn = true;
					 * worldServer.spawnEntity(player);
					 * player.forceSpawn = flag;
					 * worldServer.updateEntityWithOptionalForce(player, false);
					 * 
					 * }
					 */
					// player.connection.setPlayerLocation(stack.getTagCompound().getDouble(this.X),
					// stack.getTagCompound().getDouble(this.Y),
					// stack.getTagCompound().getDouble(this.Z), player.rotationYaw, player.rotationPitch);
					for (int i = 0; i < 30; i++) {
						worldIn.addParticle(ParticleTypes.PORTAL, player.getX() + worldIn.random.nextDouble() - 0.5D, player.getY() + 0.5D, player.getZ() + worldIn.random.nextDouble() - 0.5D, 0D, 0D, 0D);
					}
					worldIn.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);

					if (!player.abilities.instabuild) {
						stack.hurtAndBreak(1, entityLiving, e -> e.broadcastBreakEvent(entityLiving.getUsedItemHand()));
					}

					return super.finishUsingItem(stack, worldIn, entityLiving);
				}
			}

		}
		return super.finishUsingItem(stack, worldIn, entityLiving);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.teleport_stone.name"));

			if (stack.hasTagCompound()) {
				if (stack.getTagCompound().hasKey(X) && stack.getTagCompound().hasKey(Y) && stack.getTagCompound().hasKey(Z)) {
					tooltip.add(TextFormatting.BLUE + I18n.format("description.teleport_stone_position.name"));
					tooltip.add(TextFormatting.BLUE + I18n.format("X: " + (int) stack.getTagCompound().getDouble(X)));
					tooltip.add(TextFormatting.BLUE + I18n.format("Y: " + (int) stack.getTagCompound().getDouble(Y)));
					tooltip.add(TextFormatting.BLUE + I18n.format("Z: " + (int) stack.getTagCompound().getDouble(Z)));
					tooltip.add(TextFormatting.BLUE + I18n.format("Dimension: " + (stack.getTagCompound().hasKey(DIMENSION, Constants.NBT.TAG_INT) ? stack.getTagCompound().getInteger(DIMENSION) : 0)));
				}
			}
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	private void setPoint(ItemStack stack, ServerPlayerEntity player) {
		CompoundNBT stone = stack.getTag();

		if (stone == null) {
			stone = new CompoundNBT();
			stack.setTag(stone);
		}

		if (!stone.contains(X)) {
			stone.putDouble(X, player.getX());
		}

		if (!stone.contains(Y)) {
			stone.putDouble(Y, player.getY());
		}

		if (!stone.contains(Z)) {
			stone.putDouble(Z, player.getZ());
		}

		// Don't re-enable this check, if it is enabled it will never trigger correctly for whatever reason
		// if (!stone.hasKey(this.Dimension)) {
		stone.putString(DIMENSION, player.level.dimensionType().toString());
		// }
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return this.getPoint(stack) != null;
	}

	@Nullable
	private BlockPos getPoint(ItemStack stack) {
		if (stack.hasTag()) {
			if (stack.getTag().contains(X) && stack.getTag().contains(Y) && stack.getTag().contains(Z)) {
				CompoundNBT stone = stack.getTag();

				double x = stone.getDouble(X);
				double y = stone.getDouble(Y);
				double z = stone.getDouble(Z);

				return new BlockPos(x, y, z);
			}
		}
		return null;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

}
