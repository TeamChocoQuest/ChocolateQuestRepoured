package team.cqr.cqrepoured.entity.ai.boss.piratecaptain.parrot;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.Mth;
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
	public boolean canUse() {
		this.cd--;
		return this.entity.getTarget() != null && this.entity.getTarget().isAlive() && this.cd <= 0;
	}

	@Override
	public void start() {
		super.start();

		// Equip potion
		this.equipPotion(this.entity);
	}

	private void equipPotion(EntityCQRPirateParrot entity2) {
		Potion type = null;
		switch (this.entity.getRandom().nextInt((3))) {
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
		if (this.entity.getTarget().getMobType() == CreatureAttribute.UNDEAD) {
			if (type == Potions.STRONG_HARMING) {
				type = Potions.STRONG_HEALING;
			}
			if (type == Potions.HEALING) {
				type = Potions.HEALING;
			}
		}
		ItemStack potion = PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), type);
		this.entity.setItemSlot(EquipmentSlot.MAINHAND, potion);
	}

	@Override
	public void tick() {
		super.tick();

		this.entity.getLookControl().setLookAt(this.entity.getTarget(), 30, 30);
		if (this.entity.distanceToSqr(this.entity.getTarget()) <= MIN_DISTANCE_SQ) {
			// Throw stuff
			this.throwPotion(this.entity, this.entity.getTarget());

			this.cd = COOLDOWN;
		} else {
			this.entity.getNavigation().moveTo(this.entity.getTarget(), SPEED);
		}
	}

	private void throwPotion(EntityCQRPirateParrot thrower, LivingEntity target) {
		double d0 = target.getY() + target.getEyeHeight() - 1.100000023841858D;
		double d1 = target.getX() + target.getDeltaMovement().x() - thrower.getX();
		double d2 = d0 - thrower.getY();
		double d3 = target.getZ() + target.getDeltaMovement().z() - thrower.getZ();
		float f = Mth.sqrt(d1 * d1 + d3 * d3);
		ItemStack potionItem = thrower.getMainHandItem();
		PotionEntity potion = new PotionEntity(thrower.level, thrower/*, potionItem*/);
		potion.setItem(potionItem);
		potion.xRot += 20F;
		potion.shoot(d1, d2 + f * 0.2F, d3, 0.75F, 8.0F);
		thrower.level.playSound((Player) null, thrower.getX(), thrower.getY(), thrower.getZ(), SoundEvents.WITCH_THROW, thrower.getSoundSource(), 1.0F, 0.8F + thrower.getRandom().nextFloat() * 0.4F);
		thrower.level.addFreshEntity(potion);

		this.entity.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && this.canUse();
	}

}
