package team.cqr.cqrepoured.entity.ai.boss.walkerking;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.boss.EntityCQRWalkerKing;
import team.cqr.cqrepoured.entity.misc.EntityWalkerKingIllusion;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityAIWalkerIllusions extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	public EntityAIWalkerIllusions(AbstractEntityCQR entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, true, true, false);
	}

	@Override
	public void startCastingSpell() {
		// entity.getAttackTarget().addPotionEffect(new PotionEffect(Potion.getPotionById(15), 40));
		this.entity.world.getEntitiesInAABBexcluding(this.entity, new AxisAlignedBB(this.entity.getPosition().add(-20, -10, -20), this.entity.getPosition().add(20, 10, 20)), TargetUtil.createPredicateNonAlly(this.entity.getFaction())).forEach(t -> {
			if (t instanceof LivingEntity) {
				((LivingEntity) t).addPotionEffect(new EffectInstance(Effect.getPotionById(15), 40));
			}
		});
		Vector3d v = new Vector3d(2.5, 0, 0);
		for (int i = 0; i < 3; i++) {
			Vector3d pos = this.entity.getPositionVector().add(VectorUtil.rotateVectorAroundY(v, 120 * i));
			EntityWalkerKingIllusion illusion = new EntityWalkerKingIllusion(1200, (EntityCQRWalkerKing) this.entity, this.entity.getEntityWorld());
			illusion.setPosition(pos.x, pos.y, pos.z);
			this.entity.world.spawnEntity(illusion);
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED;
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
