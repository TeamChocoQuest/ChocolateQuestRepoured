package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.boarmage;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRBoarmage;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;

public class BossAIBoarmageExplodeAreaAttack extends AbstractCQREntityAI<EntityCQRBoarmage> {

	private static final int MIN_EXPLOSIONS = 6;
	private static final int MAX_EXPLOSIONS = 12;
	
	private static final long TIMEDIV = 20;
	private long lastExplodeTechTick = 0;
	
	private int explosionCount = 0;
	private List<BlockPos> explosions = new ArrayList<>();
	
	private static final int MIN_COOLDOWN = 20;
	private static final int MAX_COOLDOWN = 60;
	private int cooldown = 40;
	
	public BossAIBoarmageExplodeAreaAttack(EntityCQRBoarmage entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (cooldown > 0) {
			cooldown--;
			return false;
		}
		return entity != null && entity.isEntityAlive() && entity.isExecutingExplodeAreaAttack();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return entity != null && entity.isEntityAlive() && entity.isExecutingExplodeAreaAttack();
	}
	
	@Override
	public void startExecuting() {
		this.explosionCount = DungeonGenUtils.getIntBetweenBorders(MIN_EXPLOSIONS, MAX_EXPLOSIONS, entity.getRNG());
		this.lastExplodeTechTick = entity.ticksExisted;
		addExplosionLoc();
	}
	
	private void addExplosionLoc() {
		if(entity.getAttackTarget() != null) {
			this.explosions.add(entity.getAttackTarget().getPosition());
		}
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		//Particles on positions
		if(entity.ticksExisted % 5 == 0) {
			for(BlockPos p : explosions) {
				world.spawnParticle(EnumParticleTypes.FLAME, p.getX(), p.getY(), p.getZ(), -0.125, 0.125, -0.125, 1);
				world.spawnParticle(EnumParticleTypes.FLAME, p.getX(), p.getY(), p.getZ(), -0.125, 0.125, 0.125, 1);
				world.spawnParticle(EnumParticleTypes.FLAME, p.getX(), p.getY(), p.getZ(), 0.125, 0.125, -0.125, 1);
				world.spawnParticle(EnumParticleTypes.FLAME, p.getX(), p.getY(), p.getZ(), 0.125, 0.125, 0.125, 1);
			}
		}
		
		if(explosions.size() >= explosionCount) {
			//EXPLOSION!!!!
			
			for(BlockPos p : explosions) {
				world.newExplosion(entity, p.getX(), p.getY(), p.getZ(), 3, entity.getRNG().nextBoolean(), CQRConfig.bosses.boarmageExplosionAreaDestroysTerrain);
			}
			
			resetTask();
		}
		else if(Math.abs(entity.ticksExisted - lastExplodeTechTick) > TIMEDIV) {
			entity.swingArm(EnumHand.OFF_HAND);
			this.lastExplodeTechTick = entity.ticksExisted;
			addExplosionLoc();
		}
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		this.explosionCount = 0;
		this.explosions.clear();
		this.cooldown = DungeonGenUtils.getIntBetweenBorders(MIN_COOLDOWN, MAX_COOLDOWN, entity.getRNG());
		this.entity.stopExplodeAreaAttack();
	}

}
