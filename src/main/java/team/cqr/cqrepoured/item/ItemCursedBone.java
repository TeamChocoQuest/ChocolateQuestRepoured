package team.cqr.cqrepoured.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.ISummoner;
import team.cqr.cqrepoured.entity.misc.EntitySummoningCircle;
import team.cqr.cqrepoured.entity.misc.EntitySummoningCircle.ECircleTexture;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

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
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (!worldIn.isClientSide && this.spawnEntity((PlayerEntity) entityLiving, worldIn, stack)) {
			stack.hurtAndBreak(1, entityLiving, e -> e.broadcastBreakEvent(e.getUsedItemHand()));
		}
		if (entityLiving instanceof ServerPlayerEntity) {
			((ServerPlayerEntity) entityLiving).getCooldowns().addCooldown(this, 20);
		}
		if (stack.getDamageValue() >= stack.getMaxDamage()) {
			return ItemStack.EMPTY;
		}
		return super.finishUsingItem(stack, worldIn, entityLiving);
	}

	public Optional<Entity> spawnEntity(BlockPos pos, World worldIn, ItemStack item, LivingEntity summoner, ISummoner isummoner) {
		if (worldIn.isEmptyBlock(pos.relative(Direction.UP, 1)) && worldIn.isEmptyBlock(pos.relative(Direction.UP, 2))) {
			// DONE: Spawn circle
			ResourceLocation resLoc = new ResourceLocation(CQRMain.MODID, "skeleton");
			// Get entity id
			if (hasCursedBoneEntityTag(item)) {
				try {
					CompoundNBT tag = item.getTag();// .getCompoundTag("tag");
					resLoc = new ResourceLocation(tag.getString("entity_to_summon"));
					if (!ForgeRegistries.ENTITIES.containsKey(resLoc)) {
						resLoc = new ResourceLocation(CQRMain.MODID, "skeleton");
					}
				} catch (Exception ex) {
					resLoc = new ResourceLocation(CQRMain.MODID, "skeleton");
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

	public boolean spawnEntity(PlayerEntity player, World worldIn, ItemStack item) {
		Vector3d start = player.getEyePosition(1.0F);
		Vector3d end = start.add(player.getLookAngle().scale(5.0D));
		BlockRayTraceResult result = worldIn.clip(new RayTraceContext(start, end, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));

		if (result != null) {
			return this.spawnEntity(result.getBlockPos(), worldIn, item, player, null).isPresent();
		}
		return false;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		playerIn.startUsingItem(handIn);
		return ActionResult.success(playerIn.getItemInHand(handIn));
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		super.releaseUsing(stack, worldIn, entityLiving, timeLeft);
		if (entityLiving instanceof PlayerEntity) {
			((PlayerEntity) entityLiving).getCooldowns().addCooldown(this, 20);
		}
		// stack.damageItem(1, entityLiving);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (hasCursedBoneEntityTag(stack)) {
			try {
				CompoundNBT tag = stack.getTag();// .getCompoundTag("tag");
				tooltip.add(new TranslationTextComponent("item.cqrepoured.cursed_bone.tooltip", this.getEntityName(tag.getString("entity_to_summon"))));
				//tooltip.add(TextFormatting.BLUE + I18n.format("description.cursed_bone.name") + " " + this.getEntityName(tag.getString("entity_to_summon")));
			} catch (Exception ex) {
				//tooltip.add(TextFormatting.BLUE + I18n.format("description.cursed_bone.name") + "missingNo");
				tooltip.add(new TranslationTextComponent("item.cqrepoured.cursed_bone.tooltip", "missingNo"));
			}
			return;
		}
		tooltip.add(new TranslationTextComponent("item.cqrepoured.cursed_bone.tooltip", this.getEntityName(CQRMain.MODID + ":skeleton")));
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
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity clickedEntity) {
		if (player.isCreative() && player.isCrouching() && !player.level.isClientSide) {
			if (clickedEntity instanceof MobEntity && clickedEntity.isAlive()) {
				if (!stack.hasTag()) {
					stack.setTag(new CompoundNBT());
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
