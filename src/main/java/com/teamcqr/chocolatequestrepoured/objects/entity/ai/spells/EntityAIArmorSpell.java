package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRLich;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class EntityAIArmorSpell extends AbstractEntityAIUseSpell {

	public EntityAIArmorSpell(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (super.shouldExecute() && !this.entity.isMagicArmorActive()) {
			if (!(this.entity instanceof EntityCQRLich)) {
				// TODO: Add staff that can apply armor
			}
			return true;
		}
		return false;
	}

	@Override
	protected void castSpell() {
		if (this.entity instanceof EntityCQRLich) {
			EntityCQRLich lich = (EntityCQRLich) this.entity;
			BlockPos pos = lich.getPosition();// .add(0,1,0);
			lich.world.setBlockState(pos, ModBlocks.PHYLACTERY.getDefaultState());
			lich.setCurrentPhylacteryBlock(pos);
		} else {
			this.entity.setMagicArmorCooldown(300);
		}

	}

	@Override
	protected int getCastingTime() {
		return 200;
	}

	@Override
	protected int getCastWarmupTime() {
		return 100;
	}

	@Override
	protected int getCastingInterval() {
		return 1200;
	}

	@Override
	protected SoundEvent getSpellPrepareSound() {
		return this.getSpellType().getSpellSound();
	}

	@Override
	protected ESpellType getSpellType() {
		return ESpellType.ACTIVATE_MAGIC_ARMOR;
	}

}
