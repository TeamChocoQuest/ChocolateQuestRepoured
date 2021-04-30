package team.cqr.cqrepoured.objects.entity.ai.spells;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class EntityAIExplosionRay extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	public EntityAIExplosionRay(AbstractEntityCQR entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, true, true, false);
	}

	@Override
	public void startCastingSpell() {
		Vec3d v = new Vec3d(this.entity.getAttackTarget().getPosition().subtract(this.entity.getPosition()));
		int explosionCount = (int) v.length();
		v = v.normalize().scale(2);
		explosionCount /= 2;
		BlockPos start = this.entity.getPosition();
		BlockPos[] positions = new BlockPos[explosionCount];
		for (int i = 1; i <= explosionCount; i++) {
			BlockPos p = start.add(v.x * i, v.y * i, v.z * i);
			positions[i - 1] = p;
		}

		for (BlockPos p : positions) {
			this.entity.world.newExplosion(this.entity, p.getX(), p.getY(), p.getZ(), 1.5F, this.entity.getRNG().nextBoolean(), CQRConfig.bosses.boarmageExplosionRayDestroysTerrain);
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.ENTITY_CREEPER_PRIMED;
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
