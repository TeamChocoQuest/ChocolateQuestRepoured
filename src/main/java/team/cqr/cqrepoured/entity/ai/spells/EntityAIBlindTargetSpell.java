package team.cqr.cqrepoured.entity.ai.spells;

import org.joml.Vector3d;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.potion.Effects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAIBlindTargetSpell extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	private final int duration;

	public EntityAIBlindTargetSpell(AbstractEntityCQR entity, int cooldown, int chargingTicks, int duration) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, true, true, false);
		this.duration = duration;
	}

	@Override
	public void startCastingSpell() {
		LivingEntity attackTarget = this.entity.getTarget();
		Vector3d vec = attackTarget.position();
		vec = vec.subtract(attackTarget.getLookAngle().scale(8.0D));
		vec = vec.subtract(0.0D, 0.001D, 0.0D);
		BlockPos pos = new BlockPos(vec);

		attackTarget.addEffect(new EffectInstance(Effects.BLINDNESS, this.duration));
		if (this.entity.level.getBlockState(pos).isFaceSturdy(this.entity.level, pos, Direction.UP)) {
			this.entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 0.7F, 1.1F);
			this.entity.randomTeleport(vec.x, vec.y, vec.z, true);
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
