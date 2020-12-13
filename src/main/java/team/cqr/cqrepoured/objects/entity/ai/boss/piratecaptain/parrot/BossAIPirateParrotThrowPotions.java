package team.cqr.cqrepoured.objects.entity.ai.boss.piratecaptain.parrot;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRPirateParrot;

public class BossAIPirateParrotThrowPotions extends EntityAIBase {

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
		super.startExecuting();

		// Equip potion
		this.equipPotion(this.entity);
	}

	private void equipPotion(EntityCQRPirateParrot entity2) {
		PotionType type = null;
		switch (this.entity.getRNG().nextInt((3))) {
		case 0:
			type = PotionTypes.HARMING;
			break;
		case 1:
			type = PotionTypes.STRONG_HARMING;
			break;
		case 2:
			type = PotionTypes.STRONG_POISON;
			break;
		}
		if (this.entity.getAttackTarget().getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
			if (type == PotionTypes.STRONG_HARMING) {
				type = PotionTypes.STRONG_HEALING;
			}
			if (type == PotionTypes.HEALING) {
				type = PotionTypes.HEALING;
			}
		}
		ItemStack potion = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), type);
		this.entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, potion);
	}

	@Override
	public void updateTask() {
		super.updateTask();

		this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 30, 30);
		if (this.entity.getDistanceSq(this.entity.getAttackTarget()) <= MIN_DISTANCE_SQ) {
			// Throw stuff
			this.throwPotion(this.entity, this.entity.getAttackTarget());

			this.cd = COOLDOWN;
		} else {
			this.entity.getNavigator().tryMoveToEntityLiving(this.entity.getAttackTarget(), SPEED);
		}
	}

	private void throwPotion(EntityCQRPirateParrot thrower, EntityLivingBase target) {
		double d0 = target.posY + (double) target.getEyeHeight() - 1.100000023841858D;
		double d1 = target.posX + target.motionX - thrower.posX;
		double d2 = d0 - thrower.posY;
		double d3 = target.posZ + target.motionZ - thrower.posZ;
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
		ItemStack potionItem = thrower.getHeldItemMainhand();
		EntityPotion potion = new EntityPotion(thrower.getEntityWorld(), thrower, potionItem);
		potion.rotationPitch += 20F;
		potion.shoot(d1, d2 + (double) (f * 0.2F), d3, 0.75F, 8.0F);
		thrower.world.playSound((EntityPlayer) null, thrower.posX, thrower.posY, thrower.posZ, SoundEvents.ENTITY_WITCH_THROW, thrower.getSoundCategory(), 1.0F, 0.8F + thrower.getRNG().nextFloat() * 0.4F);
		thrower.world.spawnEntity(potion);

		this.entity.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && this.shouldExecute();
	}

}
