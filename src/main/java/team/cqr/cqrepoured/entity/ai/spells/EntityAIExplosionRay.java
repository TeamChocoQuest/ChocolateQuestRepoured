package team.cqr.cqrepoured.entity.ai.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAIExplosionRay extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	public EntityAIExplosionRay(AbstractEntityCQR entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, true, true, false);
	}

	@Override
	public void startCastingSpell() {
		Vec3 v = this.entity.getTarget().position().subtract(this.entity.position());
		int explosionCount = (int) v.length();
		v = v.normalize().scale(2);
		explosionCount /= 2;
		BlockPos start = this.entity.blockPosition();
		BlockPos[] positions = new BlockPos[explosionCount];
		for (int i = 1; i <= explosionCount; i++) {
			BlockPos p = start.offset((int)v.x * i, (int)v.y * i, (int)v.z * i);
			positions[i - 1] = p;
		}

		for (BlockPos p : positions) {
			this.entity.level().explode(this.entity, p.getX(), p.getY(), p.getZ(), 1.5F, this.entity.getRandom().nextBoolean(), CQRConfig.SERVER_CONFIG.bosses.boarmageExplosionRayDestroysTerrain.get() ? ExplosionInteraction.MOB : ExplosionInteraction.NONE);
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.CREEPER_PRIMED;
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
		return 0.0F;
	}

	@Override
	public float getGreen() {
		return 0.0F;
	}

	@Override
	public float getBlue() {
		return 0.4F;
	}

}
