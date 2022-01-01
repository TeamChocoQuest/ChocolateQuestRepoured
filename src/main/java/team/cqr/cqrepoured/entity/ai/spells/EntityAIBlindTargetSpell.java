package team.cqr.cqrepoured.entity.ai.spells;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
		LivingEntity attackTarget = this.entity.getAttackTarget();
		Vec3d vec = attackTarget.getPositionVector();
		vec = vec.subtract(attackTarget.getLookVec().scale(8.0D));
		vec = vec.subtract(0.0D, 0.001D, 0.0D);
		BlockPos pos = new BlockPos(vec);

		attackTarget.addPotionEffect(new EffectInstance(Effects.BLINDNESS, this.duration));
		if (this.entity.world.getBlockState(pos).isSideSolid(this.entity.world, pos, Direction.UP)) {
			this.entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.7F, 1.1F);
			this.entity.attemptTeleport(vec.x, vec.y, vec.z);
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
