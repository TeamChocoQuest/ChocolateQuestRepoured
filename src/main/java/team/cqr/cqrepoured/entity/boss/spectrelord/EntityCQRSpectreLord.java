package team.cqr.cqrepoured.entity.boss.spectrelord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkHooks;
import mod.azure.azurelib3.core.manager.AnimationData;
import mod.azure.azurelib3.core.manager.AnimationFactory;
import mod.azure.azurelib3.util.AzureLibUtil;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.ai.boss.spectrelord.EntityAISpectreLordChannelHate;
import team.cqr.cqrepoured.entity.ai.boss.spectrelord.EntityAISpectreLordDash;
import team.cqr.cqrepoured.entity.ai.boss.spectrelord.EntityAISpectreLordLaser;
import team.cqr.cqrepoured.entity.ai.boss.spectrelord.EntityAISpectreLordSummonIllusions;
import team.cqr.cqrepoured.entity.ai.boss.spectrelord.EntityAISpectreLordSwordShield;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.entity.bases.ISummoner;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.faction.Faction;

public class EntityCQRSpectreLord extends AbstractEntityCQRBoss implements ISummoner, IAnimatableCQR {

	private static final EntityDataAccessor<Integer> SWORD_SHIELD_ACTIVE = SynchedEntityData.<Integer>defineId(EntityCQRSpectreLord.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> CHANNELING_LASER = SynchedEntityData.<Boolean>defineId(EntityCQRSpectreLord.class, EntityDataSerializers.BOOLEAN);

	private final List<Entity> summonedEntities = new ArrayList<>();

	public EntityCQRSpectreLord(EntityType<? extends EntityCQRSpectreLord> type, Level world) {
		super(type, world);
	}

	@Override
	public CreatureAttribute getMobType() {
		return CreatureAttribute.UNDEAD;
	}

	@Override
	public double getBaseHealth() {
		return 200.0F;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.UNDEAD;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		// this.dataManager.register(INVISIBILITY, 0.0F);
		this.entityData.define(SWORD_SHIELD_ACTIVE, 0);
		this.entityData.define(CHANNELING_LASER, false);
	}

	@Override
	protected void applyAttributeValues() {
		super.applyAttributeValues();

		this.getAttribute(Attributes.ARMOR).setBaseValue(16.0D);
		this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(8.0D);
		this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.spellHandler.addSpell(0, new EntityAISpectreLordChannelHate(this, 600, 60, 480));
		this.spellHandler.addSpell(1, new EntityAISpectreLordDash(this, 200, 40, 3, 3.0D, 1.5D));
		this.spellHandler.addSpell(2, new EntityAISpectreLordLaser(this, 600, 60, 320));
		this.spellHandler.addSpell(3, new EntityAISpectreLordSummonIllusions(this, 400, 40, 4, 800));
		this.spellHandler.addSpell(4, new EntityAISpectreLordSwordShield(this, 600, 60));
	}

	@Override
	public void baseTick() {
		if (!this.level.isClientSide && this.fallDistance > 3.0F) {
			this.fallDistance = 0.0F;
			if (this.hasAttackTarget()) {
				Vec3 vec = TargetUtil.getPositionNearTarget(this.level, this, this.getTarget(), 2.0D, 8.0D, 2.0D);
				if (vec != null) {
					this.teleport(vec.x, vec.y, vec.z);
				}
			} else if (this.hasHomePositionCQR()) {
				BlockPos pos = this.getHomePositionCQR();
				this.teleport(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
			} else {
				Vec3 vec = TargetUtil.getPositionNearTarget(this.level, this, this, 0.0D, 16.0D, 8.0D);
				if (vec != null) {
					this.teleport(vec.x, vec.y, vec.z);
				}
			}
		}

		super.baseTick();

		for (Iterator<Entity> iterator = this.summonedEntities.iterator(); iterator.hasNext();) {
			Entity e = iterator.next();
			if (!e.isAlive()) {
				iterator.remove();
			}
		}

		if (!this.level.isClientSide) {
			if (this.entityData.get(SWORD_SHIELD_ACTIVE) > 0) {
				this.entityData.set(SWORD_SHIELD_ACTIVE, this.entityData.get(SWORD_SHIELD_ACTIVE) - 1);
			}
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.getInvisibility() == 1.0F) {
			return false;
		}
		if (this.isChannelingLaser()) {
			amount *= 2.0F;
		}
		if (this.isSwordShieldActive()) {
			if (source.isProjectile()) {
				return false;
			}
			if (source.getDirectEntity() != null) {
				boolean flag = super.hurt(source, amount);
				if (flag) {
					source.getDirectEntity().hurt(new DamageSource("thorns").bypassArmor(), amount * 0.25F);
				}
				return flag;
			}
		}
		return super.hurt(source, amount);
	}

	@Override
	public float getInvisibility() {
		return this.entityData.get(INVISIBILITY);
	}

	public void setSwordShieldActive(int ticks) {
		this.entityData.set(SWORD_SHIELD_ACTIVE, ticks);
	}

	public boolean isSwordShieldActive() {
		return this.entityData.get(SWORD_SHIELD_ACTIVE) > 0;
	}

	public void setChannelingLaser(boolean channelingLaser) {
		this.entityData.set(CHANNELING_LASER, channelingLaser);
	}

	public boolean isChannelingLaser() {
		return this.entityData.get(CHANNELING_LASER);
	}

	@Override
	public void teleport(double x, double y, double z) {
		double oldX = this.getX();
		double oldY = this.getY();
		double oldZ = this.getZ();
		super.teleport(x, y, z);
		this.playSound(SoundEvents.SHULKER_TELEPORT, 1.0F, 0.9F + this.random.nextFloat() * 0.2F);
		((ServerLevel) this.level).addParticle(ParticleTypes.PORTAL, oldX, oldY + this.getBbHeight() * 0.5D, oldZ, /* 4, */ 0.2D, 0.2D, 0.2D/* , 0.0D */);
		((ServerLevel) this.level).addParticle(ParticleTypes.PORTAL, x, y + this.getBbHeight() * 0.5D, z, /* 4, */ 0.2D, 0.2D, 0.2D/* , 0.0D */);
	}

	@Override
	public Faction getSummonerFaction() {
		return this.getFaction();
	}

	@Override
	public List<Entity> getSummonedEntities() {
		return this.summonedEntities;
	}

	@Override
	public LivingEntity getSummoner() {
		return this;
	}

	@Override
	public void addSummonedEntityToList(Entity summoned) {
		this.summonedEntities.add(summoned);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	// Geckolib
	private AnimationFactory factory = AzureLibUtil.createFactory(this);

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public Set<String> getAlwaysPlayingAnimations() {
		return null;
	}
	
	@Override
	public void registerControllers(AnimationData data) {
		this.registerControllers(this, data);
	}

}
