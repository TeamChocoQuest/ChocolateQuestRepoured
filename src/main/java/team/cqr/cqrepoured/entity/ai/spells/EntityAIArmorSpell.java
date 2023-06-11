package team.cqr.cqrepoured.entity.ai.spells;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.boss.EntityCQRLich;
import team.cqr.cqrepoured.init.CQRBlocks;

public class EntityAIArmorSpell extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	public EntityAIArmorSpell(AbstractEntityCQR entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, false, false, false);
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
			BlockPos pos = entity.blockPosition();
			this.entity.level.setBlockAndUpdate(pos, CQRBlocks.PHYLACTERY.get().defaultBlockState());
			((EntityCQRLich) this.entity).setCurrentPhylacteryBlock(pos);
		} else {
			this.entity.setMagicArmorCooldown(300);
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.EVOKER_PREPARE_ATTACK;
	}

	@Override
	protected SoundEvent getStartCastingSound() {
		return SoundEvents.EVOKER_CAST_SPELL;
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
