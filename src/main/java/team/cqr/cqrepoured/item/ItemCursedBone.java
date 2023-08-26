package team.cqr.cqrepoured.item;

import java.awt.TextComponent;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.entity.EntityList;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.entity.bases.ISummoner;
import team.cqr.cqrepoured.entity.misc.EntitySummoningCircle;
import team.cqr.cqrepoured.entity.misc.EntitySummoningCircle.ECircleTexture;

public class ItemCursedBone extends ItemLore {

	public ItemCursedBone(Properties properties) {
		super(properties.durability(3).setNoRepair());
		//this.setMaxDamage(3);
		//this.setMaxStackSize(1);
		//this.setNoRepair();
	}

	@Override
	public boolean isRepairable(ItemStack stack) {
		return false;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 40;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		if (!worldIn.isClientSide && this.spawnEntity((Player) entityLiving, worldIn, stack)) {
			stack.hurtAndBreak(1, entityLiving, e -> e.broadcastBreakEvent(e.getUsedItemHand()));
		}
		if (entityLiving instanceof ServerPlayer) {
			((ServerPlayer) entityLiving).getCooldowns().addCooldown(this, 20);
		}
		if (stack.getDamageValue() >= stack.getMaxDamage()) {
			return ItemStack.EMPTY;
		}
		return super.finishUsingItem(stack, worldIn, entityLiving);
	}

	public Optional<Entity> spawnEntity(BlockPos pos, Level worldIn, ItemStack item, LivingEntity summoner, ISummoner isummoner) {
		if (worldIn.isEmptyBlock(pos.relative(Direction.UP, 1)) && worldIn.isEmptyBlock(pos.relative(Direction.UP, 2))) {
			// DONE: Spawn circle
			ResourceLocation resLoc = new ResourceLocation(CQRConstants.MODID, "skeleton");
			// Get entity id
			if (hasCursedBoneEntityTag(item)) {
				try {
					CompoundTag tag = item.getTag();// .getCompoundTag("tag");
					resLoc = new ResourceLocation(tag.getString("entity_to_summon"));
					if (!ForgeRegistries.ENTITIES.containsKey(resLoc)) {
						resLoc = new ResourceLocation(CQRConstants.MODID, "skeleton");
					}
				} catch (Exception ex) {
					resLoc = new ResourceLocation(CQRConstants.MODID, "skeleton");
				}
			}
			EntitySummoningCircle circle = new EntitySummoningCircle(worldIn, resLoc, 1F, ECircleTexture.METEOR, isummoner, summoner);
			circle.setSummon(resLoc);
			circle.setNoGravity(false);
			circle.setPos(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
			worldIn.addFreshEntity(circle);
			return Optional.of(circle);
		}
		return Optional.empty();
	}

	public boolean spawnEntity(Player player, Level worldIn, ItemStack item) {
		Vec3 start = player.getEyePosition(1.0F);
		Vec3 end = start.add(player.getLookAngle().scale(5.0D));
		BlockHitResult result = worldIn.clip(new ClipContext(start, end, ClipContext.BlockMode.OUTLINE, ClipContext.FluidMode.NONE, player));

		if (result != null) {
			return this.spawnEntity(result.getBlockPos(), worldIn, item, player, null).isPresent();
		}
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		playerIn.startUsingItem(handIn);
		return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		super.releaseUsing(stack, worldIn, entityLiving, timeLeft);
		if (entityLiving instanceof Player) {
			((Player) entityLiving).getCooldowns().addCooldown(this, 20);
		}
		// stack.damageItem(1, entityLiving);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		if (hasCursedBoneEntityTag(stack)) {
			try {
				CompoundTag tag = stack.getTag();// .getCompoundTag("tag");
				tooltip.add(new TranslationTextComponent("item.cqrepoured.cursed_bone.tooltip", this.getEntityName(tag.getString("entity_to_summon"))));
				//tooltip.add(TextFormatting.BLUE + I18n.format("description.cursed_bone.name") + " " + this.getEntityName(tag.getString("entity_to_summon")));
			} catch (Exception ex) {
				//tooltip.add(TextFormatting.BLUE + I18n.format("description.cursed_bone.name") + "missingNo");
				tooltip.add(new TranslationTextComponent("item.cqrepoured.cursed_bone.tooltip", "missingNo"));
			}
			return;
		}
		tooltip.add(new TranslationTextComponent("item.cqrepoured.cursed_bone.tooltip", this.getEntityName(CQRConstants.MODID + ":skeleton")));
	}

	private String getEntityName(String registryName) {
		EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(registryName));
		if (entityType != null) {
			return new TranslationTextComponent(entityType.toString()).getString();
		}
		return "missingNO";
	}

	public static boolean hasCursedBoneEntityTag(ItemStack stack) {
		if (stack == null) {
			return false;
		}
		return stack.hasTag() && stack.getTag().contains("entity_to_summon", Constants.NBT.TAG_STRING);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity clickedEntity) {
		if (player.isCreative() && player.isCrouching() && !player.level.isClientSide) {
			if (clickedEntity instanceof MobEntity && clickedEntity.isAlive()) {
				if (!stack.hasTag()) {
					stack.setTag(new CompoundTag());
				}
				stack.getTag().putString("entity_to_summon", EntityList.getKey(clickedEntity).toString());
				return true;
			}
		}
		return super.onLeftClickEntity(stack, player, clickedEntity);
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
