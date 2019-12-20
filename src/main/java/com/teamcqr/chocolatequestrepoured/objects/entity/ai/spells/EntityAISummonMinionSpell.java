package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.ISummoner;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle.ECircleTexture;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAISummonMinionSpell extends AbstractEntityAIUseSpell {

	protected ISummoner summoner = null;
	protected static final int MAX_MINIONS = 10;
	protected static final int MAX_MINIONS_AT_A_TIME = 3;
	protected List<Entity> activeCircles = new ArrayList<Entity>();
	
	public EntityAISummonMinionSpell(AbstractEntityCQR entity) {
		super(entity);
		if(entity instanceof ISummoner) {
			this.summoner = (ISummoner) entity;
		}
	}
	
	@Override
	public boolean shouldExecute() {
		if(this.summoner == null) {
			return false;
		}
		if(super.shouldExecute()) {
			return getAliveMinionCount() < MAX_MINIONS;
		}
		return false;
	}
	
	protected int getAliveMinionCount() {
		if(activeCircles.isEmpty() && summoner.getSummonedEntities().isEmpty()) {
			return 0;
		}
		int aliveMinions = 0;
		List<Entity> toRemove = new ArrayList<>();
		for(Entity minio : activeCircles) {
			if(minio != null && !minio.isDead) {
				aliveMinions++;
			} else {
				toRemove.add(minio);
			}
		}
		for(Entity minio : summoner.getSummonedEntities()) {
			if(minio != null && !minio.isDead) {
				aliveMinions++;
			}
		}
		activeCircles.removeAll(toRemove);
		return aliveMinions;
	}

	@Override
	protected void castSpell() {
		Vec3d vector = entity.getLookVec().normalize();
		vector = vector.add(vector).add(vector).add(vector);
		int minionCount = MAX_MINIONS - getAliveMinionCount();
		if(minionCount > MAX_MINIONS_AT_A_TIME) {
			minionCount = MAX_MINIONS_AT_A_TIME;
		}
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
					summoner.addSummonedEntityToList(circle);
					activeCircles.add(circle);
				}
			}
		}
	}

	@Override
	protected int getCastingTime() {
		return 80;
	}

	@Override
	protected int getCastingInterval() {
		return 100;
	}

	@Override
	protected SoundEvent getSpellPrepareSound() {
		return null;
	}

	@Override
	protected ESpellType getSpellType() {
		return ESpellType.SUMMON_MINIONS;
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		this.activeCircles.clear();
		this.entity.setSpellCasting(false);
	}

}
