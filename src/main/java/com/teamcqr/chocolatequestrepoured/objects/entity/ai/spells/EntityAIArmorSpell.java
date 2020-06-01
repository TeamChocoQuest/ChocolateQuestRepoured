package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRLich;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class EntityAIArmorSpell extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	public EntityAIArmorSpell(AbstractEntityCQR entity, int cooldown, int chargeUpTicks) {
		super(entity, true, false, cooldown, chargeUpTicks, 1);
	}

	@Override
	public boolean shouldExecute() {
		if (!super.shouldExecute()) {
			return false;
		}
		if (this.entity.isMagicArmorActive()) {
			return false;
		}
		if (this.entity instanceof EntityCQRLich) {
			return true;
		}
		// TODO: Add staff that can apply armor
		return true;
	}

	@Override
	public void startCastingSpell() {
		super.startCastingSpell();
		if (this.entity instanceof EntityCQRLich) {
			BlockPos pos = new BlockPos(this.entity);
			this.entity.world.setBlockState(pos, ModBlocks.PHYLACTERY.getDefaultState());
			((EntityCQRLich) this.entity).setCurrentPhylacteryBlock(pos);
		} else {
			this.entity.setMagicArmorCooldown(300);
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
	}

	@Override
	protected SoundEvent getStartCastingSound() {
		return SoundEvents.ENTITY_ILLAGER_CAST_SPELL;
	}

	@Override
	public int getWeight() {
		return 10;
	}

	@Override
	public boolean ignoreWeight() {
		return false;
	}

	@Override
	public float getRed() {
		return 0.55F;
	}

	@Override
	public float getGreen() {
		return 0.0F;
	}

	@Override
	public float getBlue() {
		return 0.8F;
	}

}
