package team.cqr.cqrepoured.item.gun;

import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBubble;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.item.IRangedWeapon;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemBubblePistol extends ItemLore implements IRangedWeapon {

	private final Random rng = new Random();

	public ItemBubblePistol(Properties properties) {
		super(properties.durability(200).stacksTo(1));
		//this.setMaxDamage(this.getMaxUses());
		//this.setMaxStackSize(1);
	}

	//public int getMaxUses() {
	//	return 200;
	//}

	public double getInaccurary() {
		return 0.5D;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 10;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (entityLiving instanceof PlayerEntity) {
			((PlayerEntity) entityLiving).getCooldowns().addCooldown(this, this.getCooldown());
		}
		stack.hurtAndBreak(1, entityLiving, e -> e.broadcastBreakEvent(e.getUsedItemHand()));
		return super.finishUsingItem(stack, worldIn, entityLiving);
	}

	@Override
	public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		super.releaseUsing(stack, worldIn, entityLiving, timeLeft);
		stack.hurtAndBreak(1, entityLiving, e -> e.broadcastBreakEvent(e.getUsedItemHand()));
		if (entityLiving instanceof PlayerEntity) {
			((PlayerEntity) entityLiving).getCooldowns().addCooldown(this, this.getCooldown());
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof LivingEntity && ((LivingEntity) entityIn).isUsingItem() && ((LivingEntity) entityIn).getUseItem() == stack) {
			this.shootBubbles((LivingEntity) entityIn);
		}
	}

	private void shootBubbles(LivingEntity entity) {
		double x = -Math.sin(Math.toRadians(entity.yRot));
		double z = Math.cos(Math.toRadians(entity.yRot));
		double y = -Math.sin(Math.toRadians(entity.xRot));
		this.shootBubbles(new Vector3d(x, y, z), entity);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		playerIn.startUsingItem(handIn);
		return ActionResult.success(playerIn.getItemInHand(handIn));
	}

	private void shootBubbles(Vector3d velocity, LivingEntity shooter) {
		Vector3d v = new Vector3d(-this.getInaccurary() + velocity.x + (2 * this.getInaccurary() * this.rng.nextDouble()), -this.getInaccurary() + velocity.y + (2 * this.getInaccurary() * this.rng.nextDouble()), -this.getInaccurary() + velocity.z + (2 * this.getInaccurary() * this.rng.nextDouble()));
		v = v.normalize();
		v = v.scale(1.4);

		shooter.playSound(CQRSounds.BUBBLE_BUBBLE, 1, 0.75F + (0.5F * shooter.getRandom().nextFloat()));

		ProjectileBubble bubble = new ProjectileBubble(shooter, shooter.level);
		bubble.setDeltaMovement(v);
		//bubble.motionX = v.x;
		///bubble.motionY = v.y;
		//bubble.motionZ = v.z;
		//bubble.velocityChanged = true;
		shooter.level.addFreshEntity(bubble);
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public void shoot(World world, LivingEntity shooter, Entity target, Hand hand) {
		this.shootBubbles(shooter);
	}

	@Override
	public SoundEvent getShootSound() {
		return SoundEvents.FISHING_BOBBER_THROW;
	}

	@Override
	public double getRange() {
		return 32.0D;
	}

	@Override
	public int getCooldown() {
		return 80;
	}

	@Override
	public int getChargeTicks() {
		return 0;
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
