package team.cqr.cqrepoured.entity.mobs;

import java.util.Set;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.ai.EntityAITeleportToTargetWhenStuck;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class EntityCQREnderman extends AbstractEntityCQR implements IAnimatableCQR {

	protected boolean mayTeleport = true;

	public EntityCQREnderman(Level world) {
		this(CQREntityTypes.ENDERMAN.get(), world);
	}

	public EntityCQREnderman(EntityType<? extends AbstractEntityCQR> type, Level worldIn) {
		super(type, worldIn);
		this.maxUpStep = 1.0F;
		this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

		this.goalSelector.addGoal(3, new EntityAITeleportToTargetWhenStuck<EntityCQREnderman>(this) {
			@Override
			public boolean canUse() {
				return EntityCQREnderman.this.mayTeleport && super.canUse();
			}
		});
	}

	public void setMayTeleport(boolean value) {
		this.mayTeleport = value;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source instanceof IndirectEntityDamageSource) {
			for (int i = 0; i < 64; ++i) {
				if (this.teleportRandomly()) {
					return false;
				}
			}
		}
		return super.hurt(source, amount);
	}

	@Override
	protected void customServerAiStep() {
		if (this.isInWater() /* || (this.isWet() */ && this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
			this.hurt(DamageSource.DROWN, 1.0F);
		}
		super.customServerAiStep();
	}

	protected boolean teleportRandomly() {
		double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
		double d1 = this.getY() + (this.random.nextInt(64) - 32);
		double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
		return this.customTeleportTo(d0, d1, d2);
	}

	private boolean customTeleportTo(double pX, double pY, double pZ) {
		net.minecraftforge.event.entity.living.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, pX, pY, pZ);
		if (event.isCanceled())
			return false;
		boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
		if (flag2 && !this.isSilent()) {
			this.level.playSound((Player) null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
			this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
		}

		return flag2;
	}

	@Override
	public void teleport(double x, double y, double z) {
		this.teleportTo(x, y, z);
	}

	@Override
	public double getBaseHealth() {
		return CQRConfig.SERVER_CONFIG.baseHealths.enderman.get();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ENDERMEN;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return /* this.isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : */ SoundEvents.ENDERMAN_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENDERMAN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENDERMAN_DEATH;
	}

	@Override
	protected void applyAttributeValues() {
		super.applyAttributeValues();
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.33D);
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(7D);
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

	@Override
	public void aiStep() {
		if (this.level.isClientSide) {
			// Client
			for (int i = 0; i < 2; ++i) {
				this.level.addParticle(ParticleTypes.PORTAL, this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(), this.getY() + this.random.nextDouble() * this.getBbHeight() - 0.25D, this.getZ() + (this.random.nextDouble() - 0.5D)
						* this.getBbWidth(), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
			}
		}
		super.aiStep();
	}

	@Override
	public CreatureAttribute getMobType() {
		return CQRCreatureAttributes.VOID;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("mayTeleport", mayTeleport);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("mayTeleport")) {
			this.mayTeleport = compound.getBoolean("mayTeleport");
		}
	}

	// Geckolib
	private AnimationFactory factory = GeckoLibUtil.createFactory(this);

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public Set<String> getAlwaysPlayingAnimations() {
		return null;
	}
	
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public void registerControllers(AnimationData data) {
		this.registerControllers(this, data);
	}

}
