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
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityAISummonMinionSpell extends AbstractEntityAISpell implements IEntityAISpellAnimatedVanilla {

	protected ISummoner summoner = null;
	protected int MAX_MINIONS = 10;
	protected int MAX_MINIONS_AT_A_TIME = 3;
	protected List<Entity> activeCircles = new ArrayList<>();
	protected boolean summonViaCircle = true;
	protected Vec3d positionOffsetForSummons = new Vec3d(0, 0, 0);
	protected ResourceLocation minionOverride = null;
	protected ECircleTexture circleTextureOverride = null;

	public EntityAISummonMinionSpell(AbstractEntityCQR entity, int cooldown, int chargeUpTicks) {
		super(entity, true, cooldown, chargeUpTicks, 1);
		if (entity instanceof ISummoner) {
			this.summoner = (ISummoner) entity;
		}
	}

	public EntityAISummonMinionSpell(AbstractEntityCQR entity, int cooldown, int chargeUpTicks, ResourceLocation minion, ECircleTexture texture, boolean useCircle, int maxMinions, int maxMinionsPerSpawn, Vec3d offsetV) {
		this(entity, cooldown, chargeUpTicks);
		this.summonViaCircle = useCircle;
		this.minionOverride = minion;
		this.circleTextureOverride = texture;
		this.MAX_MINIONS = maxMinions;
		this.MAX_MINIONS_AT_A_TIME = maxMinionsPerSpawn;
		this.positionOffsetForSummons = offsetV;
	}

	@Override
	public boolean shouldExecute() {
		if (!super.shouldExecute()) {
			return false;
		}
		if (this.summoner == null) {
			return false;
		}
		return this.getAliveMinionCount() < this.MAX_MINIONS;
	}

	protected int getAliveMinionCount() {
		if (this.activeCircles.isEmpty() && this.summoner.getSummonedEntities().isEmpty()) {
			return 0;
		}
		int aliveMinions = 0;
		List<Entity> toRemove = new ArrayList<>();
		for (Entity minio : this.activeCircles) {
			if (minio != null && !minio.isDead) {
				aliveMinions++;
			} else {
				toRemove.add(minio);
			}
		}
		for (Entity minio : this.summoner.getSummonedEntities()) {
			if (minio != null && !minio.isDead) {
				aliveMinions++;
			}
		}
		this.activeCircles.removeAll(toRemove);
		return aliveMinions;
	}

	@Override
	protected void startCastingSpell() {
		Vec3d vector = this.entity.getLookVec().normalize();
		vector = vector.add(vector).add(vector).add(vector).add(vector);
		int minionCount = this.MAX_MINIONS - this.getAliveMinionCount();
		if (minionCount > this.MAX_MINIONS_AT_A_TIME) {
			minionCount = this.MAX_MINIONS_AT_A_TIME;
		}
		if (minionCount > 0) {
			double angle = /* 180D */360D / (double) minionCount;
			// vector = VectorUtil.rotateVectorAroundY(vector, 270 + (angle /2));
			BlockPos[] spawnPositions = new BlockPos[minionCount];
			for (int i = 0; i < minionCount; i++) {
				spawnPositions[i] = this.entity.getPosition().add(new BlockPos(VectorUtil.rotateVectorAroundY(vector, angle * i)));
			}
			for (BlockPos p : spawnPositions) {
				if (this.entity.getNavigator().getPathToPos(p) != null) {
					// System.out.println("Pos: " + p.toString());
					ResourceLocation summon = null;
					boolean rdmFlag = false;
					if (this.minionOverride != null) {
						summon = this.minionOverride;
					} else {
						summon = new ResourceLocation(Reference.MODID, "zombie");
						if (this.entity.getRNG().nextInt(4) == 3) {
							summon = new ResourceLocation(Reference.MODID, "skeleton");
							rdmFlag = true;
						}
					}
					ECircleTexture texture = null;
					if (this.circleTextureOverride != null) {
						texture = this.circleTextureOverride;
					} else {
						texture = ECircleTexture.ZOMBIE;
						if (rdmFlag) {
							texture = ECircleTexture.SKELETON;
						}
					}
					if (this.entity.world.getBlockState(p).isFullBlock()) {
						p = p.add(0, 1, 0);
					}
					if (this.summonViaCircle) {
						EntitySummoningCircle circle = new EntitySummoningCircle(this.entity.world, summon, 1.1F, texture, (ISummoner) this.entity);
						circle.setSummon(summon);
						circle.setPosition(p.getX() + this.positionOffsetForSummons.x, p.getY() + 0.1 + this.positionOffsetForSummons.y, p.getZ() + this.positionOffsetForSummons.z);

						this.entity.world.spawnEntity(circle);
						this.summoner.addSummonedEntityToList(circle);
						this.activeCircles.add(circle);
					} else {
						Entity summoned = EntityList.createEntityByIDFromName(summon, this.entity.world);

						summoned.setUniqueId(MathHelper.getRandomUUID());
						summoned.setPosition(p.getX() + this.positionOffsetForSummons.x, p.getY() + 0.5D + this.positionOffsetForSummons.y, p.getZ() + this.positionOffsetForSummons.z);

						this.entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, p.getX(), p.getY() + 0.02, p.getZ(), 0F, 0.5F, 0F, 2);
						this.entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, p.getX(), p.getY() + 0.02, p.getZ(), 0.5F, 0.0F, 0.5F, 1);
						this.entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, p.getX(), p.getY() + 0.02, p.getZ(), 0.5F, 0.0F, -0.5F, 1);
						this.entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, p.getX(), p.getY() + 0.02, p.getZ(), -0.5F, 0.0F, 0.5F, 1);
						this.entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, p.getX(), p.getY() + 0.02, p.getZ(), -0.5F, 0.0F, -0.5F, 1);

						this.entity.world.spawnEntity(summoned);
						if (this.summoner != null && !this.summoner.getSummoner().isDead) {
							this.summoner.setSummonedEntityFaction(summoned);
							this.summoner.addSummonedEntityToList(summoned);
						}

						this.entity.world.spawnEntity(summoned);
						this.summoner.addSummonedEntityToList(summoned);
						this.activeCircles.add(summoned);
					}
				}
			}
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
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
		return 0.7F;
	}

	@Override
	public float getGreen() {
		return 0.7F;
	}

	@Override
	public float getBlue() {
		return 0.8F;
	}

}
