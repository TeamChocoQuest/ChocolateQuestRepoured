package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIBlindTargetSpell extends AbstractEntityAISpell implements IEntityAISpellAnimatedVanilla {

	private final int duration;

	public EntityAIBlindTargetSpell(AbstractEntityCQR entity, int cooldown, int chargeUpTicks, int duration) {
		super(entity, true, cooldown, chargeUpTicks, 1);
		this.duration = duration;
	}

	@Override
	protected void chargeUpSpell() {

	}

	@Override
	protected void castSpell() {
		if (this.tick == this.chargeUpTicks) {
			this.entity.playSound(SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 1.0F, 1.0F);
		}
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		Vec3d vec = attackTarget.getPositionVector();
		vec = vec.subtract(attackTarget.getLookVec().scale(8.0D));
		vec = vec.subtract(0.0D, 0.001D, 0.0D);
		BlockPos pos = new BlockPos(vec);

		attackTarget.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, this.duration));
		if (this.entity.world.getBlockState(pos).isSideSolid(this.entity.world, pos, EnumFacing.UP)) {
			this.entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.7F, 1.1F);
			this.entity.attemptTeleport(vec.x, vec.y, vec.z);
		}
	}

	@Override
	public int getWeight() {
		return 10;
	}

	@Override
	public boolean ignoreWeight() {
		return true;
	}

	@Override
	public float getRed() {
		return 1.0F;
	}

	@Override
	public float getGreen() {
		return 1.0F;
	}

	@Override
	public float getBlue() {
		return 1.0F;
	}

}
