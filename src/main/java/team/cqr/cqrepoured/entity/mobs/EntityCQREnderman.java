package team.cqr.cqrepoured.entity.mobs;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.EntityAITeleportToTargetWhenStuck;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class EntityCQREnderman extends AbstractEntityCQR {
	
	protected boolean mayTeleport = true;

	public EntityCQREnderman(World world) {
		this(CQREntityTypes.ENDERMAN.get(), world);
	}
	
	public EntityCQREnderman(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
		super(type, worldIn);
		this.maxUpStep = 1.0F;
		this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
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
		if (this.isInWater() /*|| (this.isWet()*/ && this.getItemBySlot(EquipmentSlotType.HEAD).isEmpty()) {
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
         if (event.isCanceled()) return false;
         boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
         if (flag2 && !this.isSilent()) {
            this.level.playSound((PlayerEntity)null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
         }

         return flag2;
	}

	@Override
	public void teleport(double x, double y, double z) {
		this.teleportTo(x, y, z);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Enderman;
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
				this.level.addParticle(ParticleTypes.PORTAL, this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(), this.getY() + this.random.nextDouble() * this.getBbHeight() - 0.25D, this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(),
						(this.random.nextDouble() - 0.5D) * 2.0D);
			}
		}
		super.aiStep();
	}

	@Override
	public CreatureAttribute getMobType() {
		return CQRCreatureAttributes.VOID;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("mayTeleport", mayTeleport);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		if(compound.contains("mayTeleport")) {
			this.mayTeleport = compound.getBoolean("mayTeleport");
		}
	}
	
}
