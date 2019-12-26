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
import net.minecraft.entity.EntityList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/*
 * 20.12.2019
 * Made by: DerToaster98
 * Comment: Simple AI to summon some minions
 */
public class EntityAISummonMinionSpell extends AbstractEntityAIUseSpell {

	protected ISummoner summoner = null;
	protected int MAX_MINIONS = 10;
	protected int MAX_MINIONS_AT_A_TIME = 3;
	protected List<Entity> activeCircles = new ArrayList<Entity>();
	protected boolean summonViaCircle = true;
	protected ResourceLocation minionOverride = null;
	protected ECircleTexture circleTextureOverride = null;
	
	public EntityAISummonMinionSpell(AbstractEntityCQR entity) {
		super(entity);
		if(entity instanceof ISummoner) {
			this.summoner = (ISummoner) entity;
		}
	}
	
	public EntityAISummonMinionSpell(AbstractEntityCQR entity, ResourceLocation minion, ECircleTexture texture, boolean useCircle, int maxMinions, int maxMinionsPerSpawn) {
		this(entity);
		this.summonViaCircle = useCircle;
		this.minionOverride = minion;
		this.circleTextureOverride = texture;
		this.MAX_MINIONS = maxMinions;
		this.MAX_MINIONS_AT_A_TIME = maxMinionsPerSpawn;
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
			double angle = /*180D*/360D / (double)minionCount;
			//vector = VectorUtil.rotateVectorAroundY(vector, 270 + (angle /2));
			BlockPos[] spawnPositions = new BlockPos[minionCount];
			for(int i = 0; i < minionCount; i++) {
				spawnPositions[i] = entity.getPosition().add(new BlockPos(VectorUtil.rotateVectorAroundY(vector, angle*i)));
			}
			for(BlockPos p : spawnPositions) {
				if(entity.getNavigator().getPathToPos(p) != null) {
					//System.out.println("Pos: " + p.toString());
					ResourceLocation summon = null;
					boolean rdmFlag = false;
					if(minionOverride != null) {
						summon = minionOverride;
					} else {
						summon = new ResourceLocation(Reference.MODID, "zombie");
						if(entity.getRNG().nextInt(4) == 3) {
							summon = new ResourceLocation(Reference.MODID, "skeleton");
							rdmFlag = true;
						}
					}
					ECircleTexture texture = null;
					if(circleTextureOverride != null) {
						texture = circleTextureOverride;
					} else {
						texture = ECircleTexture.ZOMBIE;
						if(rdmFlag) {
							texture = ECircleTexture.SKELETON;
						}
					}
					if(entity.world.getBlockState(p).isFullBlock()) {
						p = p.add(0,1,0);
					}
					if(summonViaCircle) {
						EntitySummoningCircle circle = new EntitySummoningCircle(entity.world, summon, 1.1F, texture, (ISummoner) this.entity);
						circle.setSummon(summon);
						circle.setPosition(p.getX(), p.getY() +0.1, p.getZ());
						
						entity.world.spawnEntity(circle);
						summoner.addSummonedEntityToList(circle);
						activeCircles.add(circle);
					} else {
						Entity summoned = EntityList.createEntityByIDFromName(summon, entity.world);
						
						summoned.setUniqueId(MathHelper.getRandomUUID());
						summoned.setPosition(p.getX(), p.getY() + 0.5D, p.getZ());
						
						entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, p.getX(), p.getY() + 0.02, p.getZ(), 0F, 0.5F, 0F, 2);
		        		entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, p.getX(), p.getY() + 0.02, p.getZ(), 0.5F, 0.0F, 0.5F, 1);
		        		entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, p.getX(), p.getY() + 0.02, p.getZ(), 0.5F, 0.0F, -0.5F, 1);
		        		entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, p.getX(), p.getY() + 0.02, p.getZ(), -0.5F, 0.0F, 0.5F, 1);
		        		entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, p.getX(), p.getY() + 0.02, p.getZ(), -0.5F, 0.0F, -0.5F, 1);
						
						entity.world.spawnEntity(summoned);
						if(summoner != null && !summoner.getSummoner().isDead) {
							summoner.setSummonedEntityFaction(summoned);
							summoner.addSummonedEntityToList(summoned);
						}
						
						entity.world.spawnEntity(summoned);
						summoner.addSummonedEntityToList(summoned);
						activeCircles.add(summoned);
					}
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
