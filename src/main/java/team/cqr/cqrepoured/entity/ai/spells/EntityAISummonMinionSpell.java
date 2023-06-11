package team.cqr.cqrepoured.entity.ai.spells;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3d;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.entity.EntityList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.bases.ISummoner;
import team.cqr.cqrepoured.entity.misc.EntitySummoningCircle;
import team.cqr.cqrepoured.entity.misc.EntitySummoningCircle.ECircleTexture;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityAISummonMinionSpell extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	protected ISummoner summoner = null;
	protected int MAX_MINIONS = 10;
	protected int MAX_MINIONS_AT_A_TIME = 3;
	protected List<Entity> activeCircles = new ArrayList<>();
	protected boolean summonViaCircle = true;
	protected Vector3d positionOffsetForSummons = new Vector3d(0, 0, 0);
	protected ResourceLocation minionOverride = null;
	protected ECircleTexture circleTextureOverride = null;

	public EntityAISummonMinionSpell(AbstractEntityCQR entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, true, true, false);
		if (entity instanceof ISummoner) {
			this.summoner = (ISummoner) entity;
		}
	}

	public EntityAISummonMinionSpell(AbstractEntityCQR entity, int cooldown, int chargeUpTicks, ResourceLocation minion, ECircleTexture texture, boolean useCircle, int maxMinions, int maxMinionsPerSpawn, Vector3d offsetV) {
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
			if (minio != null && minio.isAlive()) {
				aliveMinions++;
			} else {
				toRemove.add(minio);
			}
		}
		for (Entity minio : this.summoner.getSummonedEntities()) {
			if (minio != null && minio.isAlive()) {
				aliveMinions++;
			}
		}
		this.activeCircles.removeAll(toRemove);
		return aliveMinions;
	}

	@Override
	public void startCastingSpell() {
		Vector3d vector = this.entity.getLookAngle().normalize();
		vector = vector.add(vector).add(vector).add(vector).add(vector);
		int minionCount = this.MAX_MINIONS - this.getAliveMinionCount();
		if (minionCount > this.MAX_MINIONS_AT_A_TIME) {
			minionCount = this.MAX_MINIONS_AT_A_TIME;
		}
		if (minionCount > 0) {
			double angle = /* 180D */360D / minionCount;
			// vector = VectorUtil.rotateVectorAroundY(vector, 270 + (angle /2));
			BlockPos[] spawnPositions = new BlockPos[minionCount];
			for (int i = 0; i < minionCount; i++) {
				spawnPositions[i] = this.entity.blockPosition().offset(new BlockPos(VectorUtil.rotateVectorAroundY(vector, angle * i)));
			}
			for (BlockPos p : spawnPositions) {
				if (this.entity.getNavigation().createPath(p, 3 /* doesn't need to be too accurate... */) != null) {
					// System.out.println("Pos: " + p.toString());
					ResourceLocation summon = null;
					boolean rdmFlag = false;
					if (this.minionOverride != null) {
						summon = this.minionOverride;
					} else {
						summon = new ResourceLocation(CQRMain.MODID, "zombie");
						if (this.entity.getRandom().nextInt(4) == 3) {
							summon = new ResourceLocation(CQRMain.MODID, "skeleton");
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
					if (this.entity.level.getBlockState(p).isFaceSturdy(this.world, p.below(), Direction.UP)) {
						p = p.offset(0, 1, 0);
					}
					if (this.summonViaCircle) {
						EntitySummoningCircle circle = new EntitySummoningCircle(this.entity.level, summon, 1.1F, texture, (ISummoner) this.entity);
						circle.setSummon(summon);
						circle.setPos(p.getX() + this.positionOffsetForSummons.x, p.getY() + 0.1 + this.positionOffsetForSummons.y, p.getZ() + this.positionOffsetForSummons.z);

						this.entity.level.addFreshEntity(circle);
						this.summoner.addSummonedEntityToList(circle);
						this.activeCircles.add(circle);
					} else {
						Entity summoned = EntityList.createEntityByIDFromName(summon, this.entity.level);

						summoned.setUUID(MathHelper.createInsecureUUID());
						summoned.setPos(p.getX() + this.positionOffsetForSummons.x, p.getY() + 0.5D + this.positionOffsetForSummons.y, p.getZ() + this.positionOffsetForSummons.z);

						if(!this.entity.level.isClientSide) {
							ServerWorld sw = (ServerWorld)this.entity.level;
							
							sw.sendParticles(ParticleTypes.WITCH, p.getX(), p.getY() + 0.02, p.getZ(), 1, 0F, 0.5F, 0F, 2);
							sw.sendParticles(ParticleTypes.WITCH, p.getX(), p.getY() + 0.02, p.getZ(), 1, 0.5F, 0.0F, 0.5F, 1);
							sw.sendParticles(ParticleTypes.WITCH, p.getX(), p.getY() + 0.02, p.getZ(), 1, 0.5F, 0.0F, -0.5F, 1);
							sw.sendParticles(ParticleTypes.WITCH, p.getX(), p.getY() + 0.02, p.getZ(), 1, -0.5F, 0.0F, 0.5F, 1);
							sw.sendParticles(ParticleTypes.WITCH, p.getX(), p.getY() + 0.02, p.getZ(), 1, -0.5F, 0.0F, -0.5F, 1);
						}

						this.entity.level.addFreshEntity(summoned);
						if (this.summoner != null && !this.summoner.getSummoner().isDeadOrDying()) {
							this.summoner.setSummonedEntityFaction(summoned);
							this.summoner.tryEquipSummon(summoned, this.world.random);
							this.summoner.addSummonedEntityToList(summoned);
						}

						/*
						 * this.entity.world.spawnEntity(summoned); this.summoner.addSummonedEntityToList(summoned);
						 * this.activeCircles.add(summoned);
						 */
					}
				}
			}
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.EVOKER_PREPARE_ATTACK;
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
