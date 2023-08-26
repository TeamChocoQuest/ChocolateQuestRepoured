package team.cqr.cqrepoured.item;

import java.awt.TextComponent;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRConstants;

public class ItemTeleportStone extends ItemLore {

	private static final String X = "x";
	private static final String Y = "y";
	private static final String Z = "z";
	private static final String DIMENSION = "dimension";

	public ItemTeleportStone(Properties properties)
	{
		super(properties.durability(100));
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

		if (isSelected && entityIn instanceof Player && worldIn.isClientSide && worldIn.getGameTime() % 4 == 0) {
			CompoundTag tag = stack.getTag();
			//Marker particles for the stored location
			if (tag != null && tag.contains(X) && tag.contains(Y) && tag.contains(Z) && tag.contains(DIMENSION) && worldIn.dimensionType().effectsLocation().toString().equals(tag.getString(DIMENSION))) {
				double x = Mth.floor(tag.getDouble(X)) + Mth.clamp(worldIn.random.nextGaussian() * 0.3D, -0.5D, 0.5D);
				double y = Mth.floor(tag.getDouble(Y)) + Mth.clamp(worldIn.random.nextGaussian() * 0.1D, -0.1D, 0.1D);
				double z = Mth.floor(tag.getDouble(Z)) + Mth.clamp(worldIn.random.nextGaussian() * 0.3D, -0.5D, 0.5D);
				worldIn.addParticle(ParticleTypes.DRAGON_BREATH, x + 0.5D, y + 0.1D, z + 0.5D, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 40;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		playerIn.startUsingItem(handIn);
		return InteractionResultHolder.success(stack);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		if (entityLiving instanceof ServerPlayer && worldIn instanceof ServerLevel) {
			ServerLevel sw = (ServerLevel) worldIn;
			ServerPlayer player = (ServerPlayer) entityLiving;
			player.getCooldowns().addCooldown(stack.getItem(), 60);

			if (player.isCrouching() && stack.hasTag()) {
				stack.getTag().remove(X);
				stack.getTag().remove(Y);
				stack.getTag().remove(Z);
				worldIn.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.AMBIENT, 1.0F, 1.0F, false);
					sw.sendParticles(ParticleTypes.LARGE_SMOKE, player.getX() + worldIn.random.nextDouble() - 0.5D, player.getY() + 0.5D, player.getZ() + worldIn.random.nextDouble() - 0.5D, 10, 0D, 0D, 0D, 1.0D);
			}

			else if (this.getPoint(stack) == null || !stack.hasTag()) {
				this.setPoint(stack, player);
				for (int i = 0; i < 10; i++) {
					worldIn.addParticle(ParticleTypes.FLAME, player.getX() + worldIn.random.nextDouble() - 0.5D, player.getY() + 0.5D, player.getZ() + worldIn.random.nextDouble() - 0.5D, 0D, 0D, 0D);
				}
				worldIn.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.AMBIENT, 1.0F, 1.0F, false);

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

					ServerLevel targetDimension = sw;
					if (!dimension.equals(player.getLevel().dimensionType().effectsLocation().toString())) {
						MinecraftServer server = player.level.getServer();
						if (server != null) {
							//transferPlayerToDimension(player, dimension, pos);
							ResourceKey<Level> rk = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimension));
							
							targetDimension = server.getLevel(rk);
							if(targetDimension == null) {
								targetDimension = sw;
							}
						}
					}
					player.teleportTo(targetDimension, pos.getX(), pos.getY(), pos.getZ(), player.yRot, player.xRot);
					for (int i = 0; i < 30; i++) {
						worldIn.addParticle(ParticleTypes.PORTAL, player.getX() + worldIn.random.nextDouble() - 0.5D, player.getY() + 0.5D, player.getZ() + worldIn.random.nextDouble() - 0.5D, 0D, 0D, 0D);
					}
					worldIn.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.AMBIENT, 1.0F, 1.0F, false);

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
	protected void appendAdditionalTooltipEntries(ItemStack stack, Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn, boolean holdingShift) {
		if(holdingShift) {
			if (stack.hasTag()) {
				if (stack.getTag().contains(X) && stack.getTag().contains(Y) && stack.getTag().contains(Z) && stack.getTag().contains(DIMENSION)) {
					tooltip.add(new TranslationTextComponent("item." + CQRConstants.MODID + "." + getRegistryName().getPath() + ".tooltip.position"));
					tooltip.add((new TextComponent("X: " + (int) stack.getTag().getDouble(X))).withStyle(ChatFormatting.BLUE));
					tooltip.add((new TextComponent("Y: " + (int) stack.getTag().getDouble(Y))).withStyle(ChatFormatting.BLUE));
					tooltip.add((new TextComponent("Z: " + (int) stack.getTag().getDouble(Z))).withStyle(ChatFormatting.BLUE));
					tooltip.add((new TextComponent("Dimension: " + stack.getTag().getString(DIMENSION))).withStyle(ChatFormatting.BLUE));
				}
			}
		}
	}

	private void setPoint(ItemStack stack, ServerPlayer player) {
		CompoundTag stone = stack.getTag();

		if (stone == null) {
			stone = new CompoundTag();
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
		stone.putString(DIMENSION, player.level.dimensionType().effectsLocation().toString());
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
				CompoundTag stone = stack.getTag();

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
