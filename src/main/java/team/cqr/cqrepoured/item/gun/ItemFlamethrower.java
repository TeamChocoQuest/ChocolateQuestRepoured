package team.cqr.cqrepoured.item.gun;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.ISizable;
import team.cqr.cqrepoured.item.ItemMagazineBased;

public class ItemFlamethrower extends ItemMagazineBased {

	public ItemFlamethrower() {
		super(new Predicate<ItemStack>() {

			@Override
			public boolean test(ItemStack t) {
				return t != null && t.getItem() == Items.SLIME_BALL;
			}
		});
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return this.getAmmoInItem(stack);
	}

	@Override
	public UseAction getItemUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	public void shootFlames(LivingEntity entity) {
		World world = entity.world;
		float rotationYaw = MathHelper.wrapDegrees(entity.rotationYawHead);
		double armDist = 1.0D;
		if(entity instanceof ISizable) {
			armDist *= ((ISizable)entity).getSizeVariation();
		}
		double offY = entity.height * 0.75D;
		double posX = entity.posX - Math.sin(Math.toRadians(rotationYaw)) * armDist;
		double posY = entity.posY + offY;
		double posZ = entity.posZ + Math.cos(Math.toRadians(rotationYaw)) * armDist;

		float vx = (float) -Math.sin(Math.toRadians(rotationYaw));
		float vz = (float) Math.cos(Math.toRadians(rotationYaw));
		double vy = -Math.sin(Math.toRadians(entity.rotationPitch));
		vx = (float) (vx * (1.0D - Math.abs(vy)));
		vz = (float) (vz * (1.0D - Math.abs(vy)));

		if (world.isRemote) {
			for (int i = 0; i < 8; i++) {
				world.spawnParticle(EnumParticleTypes.FLAME, posX, posY, posZ, (vx + itemRand.nextFloat() - 0.5D) / 3.0D, (vy + itemRand.nextFloat() - 0.5D) / 8.0D, (vz + itemRand.nextFloat() - 0.5D) / 3.0D);
			}
		} else {
			int dist = 10;
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().grow(entity.getLookVec().x * dist, entity.getLookVec().y * dist, entity.getLookVec().z * dist).expand(1.0D, 1.0D, 1.0D));

			for (Entity e : list) {
				if (e instanceof LivingEntity && !e.isWet() && !e.isBeingRidden()) {
					double d = posX - e.posX;
					double d2 = posZ - e.posZ;
					double rotDiff = Math.atan2(d, d2);
					rotDiff = rotDiff * 180.0D / 3.141592D;
					rotDiff = -MathHelper.wrapDegrees(rotDiff - 180.0D);
					rotDiff -= rotationYaw;

					if (Math.abs(rotDiff) < 30.0D) {
						e.setFire(2);
						e.attackEntityFrom(DamageSource.IN_FIRE, 2.5F);
					}
				}
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (this.getAmmoInItem(playerIn.getHeldItem(handIn)) <= 0) {
			return new ActionResult<>(ActionResultType.FAIL, playerIn.getHeldItem(handIn));
		}
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if (entityIn instanceof LivingEntity) {
			LivingEntity user = (LivingEntity) entityIn;
			if (user.getActiveItemStack() == stack) {
				if (user.isHandActive()) {
					this.shootFlames((LivingEntity) entityIn);
					this.removeAmmoFromItem(stack, 1);
				}
			} else if ((((LivingEntity) entityIn).getHeldItemMainhand() == stack || ((LivingEntity) entityIn).getHeldItemOffhand() == stack) && entityIn.ticksExisted % 5 == 0 && this.getAmmoInItem(stack) < this.getMaxAmmo()) {
				if (entityIn instanceof PlayerEntity) {
					this.reloadFromInventory(((PlayerEntity) user).inventory, stack, !((PlayerEntity) user).isCreative());
				}
			}
		}
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

	@Override
	public int getMaxAmmo() {
		return 2000;
	}

	@Override
	protected int getAmmoForSingleAmmoItem(ItemStack fuelItem) {
		return 20;
	}

	@Override
	protected int getMaxProcessedItemsPerReloadCycle() {
		return 1;
	}

}
