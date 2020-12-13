package team.cqr.cqrepoured.objects.entity.ai.spells;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.entity.misc.EntitySummoningCircle;
import team.cqr.cqrepoured.objects.entity.misc.EntitySummoningCircle.ECircleTexture;
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
		Vec3d vector = this.entity.getLookVec().normalize();
		vector = vector.add(vector).add(vector).add(vector).add(vector);

		int ballCount = DungeonGenUtils.randomBetween(MIN_FIREBALLS_PER_CAST, MAX_FIREBALLS_PER_CAST, this.entity.getRNG());

		if (ballCount > 0) {
			double angle = 360D / (double) ballCount;
			// vector = VectorUtil.rotateVectorAroundY(vector, 270 + (angle /2));
			BlockPos[] spawnPositions = new BlockPos[ballCount];
			BlockPos centeredPos = this.entity.getAttackTarget().getPosition();
			Vec3d v = this.entity.getAttackTarget().getPositionVector().subtract(this.entity.getPositionVector());
			v = v.normalize().scale(Math.abs((ballCount / 3) - 2));
			centeredPos = centeredPos.add(v.x, v.y, v.z);
			for (int i = 0; i < ballCount; i++) {
				spawnPositions[i] = centeredPos.add(new BlockPos(VectorUtil.rotateVectorAroundY(vector, angle * i)));
			}
			for (BlockPos p : spawnPositions) {
				if (this.entity.getNavigator().getPathToPos(p) != null) {
					// System.out.println("Pos: " + p.toString());
					ResourceLocation summon = new ResourceLocation("cqrepoured", "projectile_hot_fireball");
					ECircleTexture texture = ECircleTexture.METEOR;
					EntitySummoningCircle circle = new EntitySummoningCircle(this.entity.world, summon, 0.1F, texture, null);
					circle.setSummon(summon);
					circle.setPosition(p.getX(), p.getY() + 10, p.getZ());
					circle.setVelocityForSummon(new Vec3d(0D, -1D, 0D));

					this.entity.world.spawnEntity(circle);
				}
			}
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.ENTITY_WITHER_SPAWN;
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
