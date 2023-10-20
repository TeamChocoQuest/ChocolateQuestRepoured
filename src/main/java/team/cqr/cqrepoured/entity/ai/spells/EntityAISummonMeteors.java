package team.cqr.cqrepoured.entity.ai.spells;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.misc.EntitySummoningCircle;
import team.cqr.cqrepoured.entity.misc.EntitySummoningCircle.ECircleTexture;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityAISummonMeteors extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	protected static final int MIN_FIREBALLS_PER_CAST = 3;
	protected static final int MAX_FIREBALLS_PER_CAST = 12;

	public EntityAISummonMeteors(AbstractEntityCQR entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, true, true, false);
	}

	@Override
	public void startCastingSpell() {
		Vec3 vector = this.entity.getLookAngle().normalize();
		vector = vector.add(vector).add(vector).add(vector).add(vector);

		int ballCount = DungeonGenUtils.randomBetween(MIN_FIREBALLS_PER_CAST, MAX_FIREBALLS_PER_CAST, this.entity.getRandom());

		if (ballCount > 0) {
			double angle = 360D / ballCount;
			// vector = VectorUtil.rotateVectorAroundY(vector, 270 + (angle /2));
			BlockPos[] spawnPositions = new BlockPos[ballCount];
			BlockPos centeredPos = this.entity.getTarget().blockPosition();
			Vec3 v = this.entity.getTarget().position().subtract(this.entity.position());
			v = v.normalize().scale(Math.abs((ballCount / 3) - 2));
			centeredPos = centeredPos.offset((int)v.x, (int)v.y, (int)v.z);
			for (int i = 0; i < ballCount; i++) {
				spawnPositions[i] = centeredPos.offset(BlockPos.containing(VectorUtil.rotateVectorAroundY(vector, angle * i)));
			}
			for (BlockPos p : spawnPositions) {
				if (this.entity.getNavigation().createPath(p, 1 /* accuracy */) != null) {
					// System.out.println("Pos: " + p.toString());
					ResourceLocation summon = new ResourceLocation("cqrepoured", "projectile_hot_fireball");
					ECircleTexture texture = ECircleTexture.METEOR;
					EntitySummoningCircle circle = new EntitySummoningCircle(this.entity.level(), summon, 0.1F, texture, null);
					circle.setSummon(summon);
					circle.setPos(p.getX(), p.getY() + 10.0D, p.getZ());
					circle.setVelocityForSummon(new Vec3(0D, -1D, 0D));

					this.entity.level().addFreshEntity(circle);
				}
			}
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.WITHER_SPAWN;
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
		return 0.8F;
	}

	@Override
	public float getGreen() {
		return 0.0F;
	}

	@Override
	public float getBlue() {
		return 0.0F;
	}

}
