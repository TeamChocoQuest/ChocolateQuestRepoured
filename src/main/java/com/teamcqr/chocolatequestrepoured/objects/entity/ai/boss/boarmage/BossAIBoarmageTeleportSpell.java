package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.boarmage;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRBoarmage;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileFireWallPart;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BossAIBoarmageTeleportSpell extends AbstractCQREntityAI<EntityCQRBoarmage> {

	private int cooldown = 10;
	private int wallCounter = 0;
	private int wallsMax = 0;
	private static final int MIN_WALLS = 2;
	private static final int MAX_WALLS = 5;
	private static final int MIN_WALL_LENGTH = 6;
	private static final int MAX_WALL_LENGTH = 12;
	private static final double MIN_DISTANCE = 3;
	private static final double MAX_DISTANCE = 12;
	private static final long PREPARE_TIME = 10;
	
	private long ticksAtTeleport = 0;
	
	public BossAIBoarmageTeleportSpell(EntityCQRBoarmage entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if(cooldown > 0) {
			cooldown--;
			return false;
		}
		return entity.hasAttackTarget() && (entity.getDistance(entity.getAttackTarget()) <= MIN_DISTANCE || entity.getDistance(entity.getAttackTarget()) >= MAX_DISTANCE);
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		this.wallsMax = DungeonGenUtils.getIntBetweenBorders(MIN_WALLS, MAX_WALLS, entity.getRNG());
		this.wallCounter = 1;
		world.newExplosion(entity, entity.posX, entity.posY, entity.posZ, 2, true, false);
		Vec3d v = entity.getPositionVector().subtract(entity.getAttackTarget().getPositionVector());
		v = v.normalize().scale(5);
		Vec3d p = entity.getAttackTarget().getPositionVector().add(v);
		if(entity.getNavigator().canEntityStandOnPos(new BlockPos(p.x,p.y,p.z))) {
			if(entity.attemptTeleport(p.x, p.y, p.z)) {
				ticksAtTeleport = entity.ticksExisted;
				return;
			}
		} 
		resetTask();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && ticksAtTeleport != 0 && wallCounter <= wallsMax;
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		if(entity.ticksExisted - ticksAtTeleport > PREPARE_TIME) {
			ticksAtTeleport = entity.ticksExisted;
			//Summon fire wall here
			int wallLength = MIN_WALL_LENGTH + wallCounter * ((MAX_WALL_LENGTH - MIN_WALL_LENGTH) / (wallsMax));
			
			//WALL CODE
			Vec3d v = new Vec3d(this.entity.getAttackTarget().getPosition().subtract(this.entity.getPosition()));
			v = new Vec3d(v.x, 0, v.z);
			v = v.normalize();
			Vec3d vR = VectorUtil.rotateVectorAroundY(v, 90);
			Vec3d vL = VectorUtil.rotateVectorAroundY(v, 270);
			Vec3d[] positions = new Vec3d[wallLength + 2];
			Vec3d startPos = this.entity.getPositionVector().add(new Vec3d(v.x / 2, 0, v.z / 2));
			int arrayIndex = 0;
			positions[arrayIndex] = startPos;
			arrayIndex++;
			for (int i = 1; i <= wallLength / 2; i++) {
				positions[arrayIndex] = startPos.add(new Vec3d(i * vR.x, 0, i * vR.z));
				arrayIndex++;
				positions[arrayIndex] = startPos.add(new Vec3d(i * vL.x, 0, i * vL.z));
				arrayIndex++;
			}

			for (Vec3d p : positions) {
				if (p != null) {
					ProjectileFireWallPart wallPart = new ProjectileFireWallPart(this.entity.world, this.entity);
					wallPart.setPosition(p.x, p.y, p.z);
					// wallPart.setVelocity(v.x / 2, 0, v.z / 2);
					wallPart.motionX = v.x / 2D;
					wallPart.motionY = 0;
					wallPart.motionZ = v.z / 2D;
					wallPart.velocityChanged = true;
					this.entity.world.spawnEntity(wallPart);
				}
			}
			//END OF WALL CODE
					
			wallCounter++;
		} 
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		cooldown = 40;
		ticksAtTeleport = 0;
	}

}
