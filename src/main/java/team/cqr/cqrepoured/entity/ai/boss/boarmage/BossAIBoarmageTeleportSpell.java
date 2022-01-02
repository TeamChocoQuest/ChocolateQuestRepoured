package team.cqr.cqrepoured.entity.ai.boss.boarmage;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
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
		return this.entity.hasAttackTarget() && (this.entity.getDistance(this.entity.getAttackTarget()) <= MIN_DISTANCE || this.entity.getDistance(this.entity.getAttackTarget()) >= MAX_DISTANCE);
	}

	@Override
	public void start() {
		super.start();
		this.wallsMax = DungeonGenUtils.randomBetween(MIN_WALLS, MAX_WALLS, this.entity.getRNG());
		this.wallCounter = 0;
		this.world.newExplosion(this.entity, this.entity.posX, this.entity.posY, this.entity.posZ, 2, false, CQRConfig.bosses.boarmageExplosionRayDestroysTerrain);
		Vector3d v = this.entity.position().subtract(this.entity.getAttackTarget().position());
		v = v.normalize().scale(5);
		Vector3d p = this.entity.getAttackTarget().position().subtract(v);
		if (this.entity.getNavigator().canEntityStandOnPos(new BlockPos(p.x, p.y, p.z))) {
			if (this.entity.attemptTeleport(p.x, p.y, p.z)) {
				this.ticksAtTeleport = this.entity.ticksExisted;
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
		if (Math.abs(this.entity.ticksExisted - this.ticksAtTeleport) > PREPARE_TIME) {
			this.ticksAtTeleport = this.entity.ticksExisted;
			// Summon fire wall here
			int wallLength = MIN_WALL_LENGTH + this.wallCounter * ((MAX_WALL_LENGTH - MIN_WALL_LENGTH) / (this.wallsMax));

			// WALL CODE
			Vector3d v = new Vector3d(this.entity.getAttackTarget().getPosition().subtract(this.entity.getPosition()));
			v = new Vector3d(v.x, 0, v.z);
			v = v.normalize();
			Vector3d vR = VectorUtil.rotateVectorAroundY(v, 90);
			Vector3d vL = VectorUtil.rotateVectorAroundY(v, 270);
			Vector3d[] positions = new Vector3d[wallLength + 2];
			Vector3d startPos = this.entity.position().add(new Vector3d(v.x / 2, 0, v.z / 2));
			int arrayIndex = 0;
			positions[arrayIndex] = startPos;
			arrayIndex++;
			for (int i = 1; i <= wallLength / 2; i++) {
				positions[arrayIndex] = startPos.add(new Vector3d(i * vR.x, 0, i * vR.z));
				arrayIndex++;
				positions[arrayIndex] = startPos.add(new Vector3d(i * vL.x, 0, i * vL.z));
				arrayIndex++;
			}
			this.entity.swingArm(Hand.MAIN_HAND);

			for (Vector3d p : positions) {
				if (p != null) {
					ProjectileFireWallPart wallPart = new ProjectileFireWallPart(this.entity.world, this.entity);
					wallPart.setPosition(p.x, p.y, p.z);
					// wallPart.setVelocity(v.x / 2, 0, v.z / 2);
					wallPart.motionX = v.x / 2D;
					wallPart.motionY = 0;
					wallPart.motionZ = v.z / 2D;
					wallPart.velocityChanged = true;
					this.world.spawnEntity(wallPart);
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
