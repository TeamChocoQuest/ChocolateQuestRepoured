package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.lich;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.entity.ECQREntityArmPoses;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.ISummoner;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle.ECircleTexture;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BossAISummonZombie extends AbstractCQREntityAI {

	private final int COOLDOWN = 20;
	private final int ANIMATION_TIME = 30;
	private int currentCooldownValue = COOLDOWN;
	private int currentAnimationTime = 0;
	private int maxMinions = 5;
	
	private List<Entity> summonedMinions = new ArrayList<>();
	private boolean animationRunning;
	
	/*
	 * WARNING: entity has to implement ISummoner for this to work!
	 */
	public BossAISummonZombie(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if(entity == null || entity.isDead || entity.getAttackTarget() == null) {
			this.currentAnimationTime = ANIMATION_TIME;
			return false;
		}
		if(this.animationRunning) {
			currentAnimationTime++;
			if(currentAnimationTime >= ANIMATION_TIME) {
				this.entity.setArmPose(ECQREntityArmPoses.NONE);
				this.currentAnimationTime = 0;
				this.animationRunning = false;
			} else {
				this.entity.setVelocity(0, 0, 0);
				this.entity.setArmPose(ECQREntityArmPoses.SPELLCASTING);
			}
		} else {
			this.entity.setArmPose(ECQREntityArmPoses.NONE);
		}
		currentCooldownValue--;
		//System.out.println("Current value: " + currentCooldownValue);
		if(currentCooldownValue <= 0) {
			return (summonedMinions.isEmpty() || getAliveMinionCount() < maxMinions);
		}
		return false;
	}
	
	private int getAliveMinionCount() {
		if(summonedMinions.isEmpty()) {
			return 0;
		}
		int aliveMinions = 0;
		List<Entity> toRemove = new ArrayList<>();
		for(Entity minio : summonedMinions) {
			if(minio != null && !minio.isDead) {
				aliveMinions++;
			} else {
				toRemove.add(minio);
			}
		}
		summonedMinions.removeAll(toRemove);
		return aliveMinions;
	}
	
	@Override
	public void updateTask() {
		if(!shouldContinueExecuting()) {
			return;
		}
		currentCooldownValue = COOLDOWN;
		this.entity.setArmPose(ECQREntityArmPoses.NONE);
		Vec3d vector = entity.getLookVec().normalize();
		vector = vector.add(vector).add(vector).add(vector);
		int minionCount = maxMinions - getAliveMinionCount();
		if(minionCount > 0 ) {
			double angle = 180D / (double)minionCount;
			vector = VectorUtil.rotateVectorAroundY(vector, 270);
			BlockPos[] spawnPositions = new BlockPos[minionCount];
			for(int i = 0; i < minionCount; i++) {
				spawnPositions[i] = entity.getPosition().add(new BlockPos(VectorUtil.rotateVectorAroundY(vector, angle*i)));
			}
			for(BlockPos p : spawnPositions) {
				if(entity.getNavigator().getPathToPos(p) != null) {
					//System.out.println("Pos: " + p.toString());
					ResourceLocation summon = new ResourceLocation(Reference.MODID, "zombie");
					EntitySummoningCircle circle = new EntitySummoningCircle(entity.world, summon, 1.1F, ECircleTexture.ZOMBIE, (ISummoner) this.entity);
					circle.setSummon(summon);
					//circle.setLocationAndAngles(p.getX(), entity.posY +0.05, p.getZ(), 0F, 0F);
					circle.setPosition(p.getX(), p.getY() +0.05, p.getZ());
					
					entity.world.spawnEntity(circle);
					summonedMinions.add(circle);
				}
			}
			this.currentAnimationTime = 0;
			this.animationRunning = true;
			this.entity.setArmPose(ECQREntityArmPoses.SPELLCASTING);
			this.currentAnimationTime++;
			this.currentCooldownValue = 2* COOLDOWN;
		}
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute();
	}
	
	@Override
	public void resetTask() {
		this.summonedMinions.clear();
		//this.currentAnimationTime = ANIMATION_TIME;
		this.currentCooldownValue = COOLDOWN;
		this.entity.setArmPose(ECQREntityArmPoses.NONE);
		//this.animationRunning = false;
	}

}
