package team.cqr.cqrepoured.entity.ai.boss.boarmage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.boss.EntityCQRBoarmage;
import team.cqr.cqrepoured.entity.projectiles.ProjectileFireWallPart;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

public class BossAIBoarmageTeleportSpell extends AbstractCQREntityAI<EntityCQRBoarmage> {

	private int cooldown = 20;
	private int wallCounter = 0;
	private int wallsMax = 0;
	private static final int MIN_WALLS = 1;
	private static final int MAX_WALLS = 5;
	private static final int MIN_WALL_LENGTH = 6;
	private static final int MAX_WALL_LENGTH = 12;
	private static final double MIN_DISTANCE = 1;
	private static final double MAX_DISTANCE = 9;
	private static final long PREPARE_TIME = 40;

	private long ticksAtTeleport = 0;

	public BossAIBoarmageTeleportSpell(EntityCQRBoarmage entity) {
		super(entity);
	}

	@Override
	public boolean canUse() {
		if (this.ticksAtTeleport != 0) {
			return false;
		}
		if (this.cooldown > 0) {
			this.cooldown--;
			return false;
		}
		return this.entity.hasAttackTarget() && (this.entity.distanceTo(this.entity.getTarget()) <= MIN_DISTANCE || this.entity.distanceTo(this.entity.getTarget()) >= MAX_DISTANCE);
	}

	@Override
	public void start() {
		super.start();
		this.wallsMax = DungeonGenUtils.randomBetween(MIN_WALLS, MAX_WALLS, this.entity.getRandom());
		this.wallCounter = 0;
		this.world.explode(this.entity, this.entity.getX(), this.entity.getY(), this.entity.getZ(), 2, false, CQRConfig.SERVER_CONFIG.bosses.boarmageExplosionRayDestroysTerrain.get() ? ExplosionInteraction.MOB : ExplosionInteraction.NONE);
		Vec3 v = this.entity.position().subtract(this.entity.getTarget().position());
		v = v.normalize().scale(5);
		Vec3 p = this.entity.getTarget().position().subtract(v);
		if (this.entity.getNavigation().isStableDestination(BlockPos.containing(p.x, p.y, p.z))) {
			if (this.entity.randomTeleport(p.x, p.y, p.z, true)) {
				this.ticksAtTeleport = this.entity.tickCount;
				return;
			}
		}
		this.stop();
	}

	@Override
	public boolean canContinueToUse() {
		return this.ticksAtTeleport != 0 && this.wallCounter <= this.wallsMax && this.entity.hasAttackTarget();
	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		if (Math.abs(this.entity.tickCount - this.ticksAtTeleport) > PREPARE_TIME) {
			this.ticksAtTeleport = this.entity.tickCount;
			// Summon fire wall here
			int wallLength = MIN_WALL_LENGTH + this.wallCounter * ((MAX_WALL_LENGTH - MIN_WALL_LENGTH) / (this.wallsMax));

			// WALL CODE
			Vec3 v = this.entity.getTarget().position().subtract(this.entity.position());
			v = new Vec3(v.x, 0, v.z);
			v = v.normalize();
			Vec3 vR = VectorUtil.rotateVectorAroundY(v, 90);
			Vec3 vL = VectorUtil.rotateVectorAroundY(v, 270);
			Vec3[] positions = new Vec3[wallLength + 2];
			Vec3 startPos = this.entity.position().add(new Vec3(v.x / 2, 0, v.z / 2));
			int arrayIndex = 0;
			positions[arrayIndex] = startPos;
			arrayIndex++;
			for (int i = 1; i <= wallLength / 2; i++) {
				positions[arrayIndex] = startPos.add(new Vec3(i * vR.x, 0, i * vR.z));
				arrayIndex++;
				positions[arrayIndex] = startPos.add(new Vec3(i * vL.x, 0, i * vL.z));
				arrayIndex++;
			}
			this.entity.swing(InteractionHand.MAIN_HAND);

			for (Vec3 p : positions) {
				if (p != null) {
					ProjectileFireWallPart wallPart = new ProjectileFireWallPart(this.entity, this.entity.level());
					wallPart.setPos(p.x, p.y, p.z);
					// wallPart.setVelocity(v.x / 2, 0, v.z / 2);
					/*wallPart.motionX = v.x / 2D;
					wallPart.motionY = 0;
					wallPart.motionZ = v.z / 2D;
					wallPart.velocityChanged = true;*/
					wallPart.setDeltaMovement(v.x / 2D, 0, v.z / 2D);
					wallPart.hasImpulse = true;
					this.world.addFreshEntity(wallPart);
				}
			}
			// END OF WALL CODE

			this.wallCounter++;
		}
	}

	@Override
	public void stop() {
		super.stop();
		this.cooldown = 60;
		this.ticksAtTeleport = 0;
	}

}
