package team.cqr.cqrepoured.entity.ai.boss.piratecaptain.parrot;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateParrot;

public class BossAIPirateParrotThrowPotions extends Goal {

	private final EntityCQRPirateParrot entity;

	private static final double SPEED = 2;
	private static final double MIN_DISTANCE_SQ = 4 * 4;
	private int cd = 0;
	private static final int COOLDOWN = 40;

	public BossAIPirateParrotThrowPotions(EntityCQRPirateParrot entity) {
		super();
		this.entity = entity;
	}

	@Override
	public boolean shouldExecute() {
		this.cd--;
		return this.entity.getAttackTarget() != null && this.entity.getAttackTarget().isEntityAlive() && this.cd <= 0;
	}

	@Override
	public void startExecuting() {
		super.start();

		// Equip potion
		this.equipPotion(this.entity);
	}

	private void equipPotion(EntityCQRPirateParrot entity2) {
		Potion type = null;
		switch (this.entity.getRNG().nextInt((3))) {
		case 0:
			type = Potions.HARMING;
			break;
		case 1:
			type = Potions.STRONG_HARMING;
			break;
		case 2:
			type = Potions.STRONG_POISON;
			break;
		}
		if (this.entity.getAttackTarget().getCreatureAttribute() == CreatureAttribute.UNDEAD) {
			if (type == Potions.STRONG_HARMING) {
				type = Potions.STRONG_HEALING;
			}
			if (type == Potions.HEALING) {
				type = Potions.HEALING;
			}
		}
		ItemStack potion = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), type);
		this.entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, potion);
	}

	@Override
	public void updateTask() {
		super.tick();

		this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 30, 30);
		if (this.entity.getDistanceSq(this.entity.getAttackTarget()) <= MIN_DISTANCE_SQ) {
			// Throw stuff
			this.throwPotion(this.entity, this.entity.getAttackTarget());

			this.cd = COOLDOWN;
		} else {
			this.entity.getNavigator().tryMoveToEntityLiving(this.entity.getAttackTarget(), SPEED);
		}
	}

	private void throwPotion(EntityCQRPirateParrot thrower, LivingEntity target) {
		double d0 = target.posY + target.getEyeHeight() - 1.100000023841858D;
		double d1 = target.posX + target.motionX - thrower.posX;
		double d2 = d0 - thrower.posY;
		double d3 = target.posZ + target.motionZ - thrower.posZ;
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
		ItemStack potionItem = thrower.getHeldItemMainhand();
		PotionEntity potion = new PotionEntity(thrower.getEntityWorld(), thrower, potionItem);
		potion.rotationPitch += 20F;
		potion.shoot(d1, d2 + f * 0.2F, d3, 0.75F, 8.0F);
		thrower.world.playSound((PlayerEntity) null, thrower.posX, thrower.posY, thrower.posZ, SoundEvents.ENTITY_WITCH_THROW, thrower.getSoundCategory(), 1.0F, 0.8F + thrower.getRNG().nextFloat() * 0.4F);
		thrower.world.spawnEntity(potion);

		this.entity.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return super.canContinueToUse() && this.shouldExecute();
	}

}
