package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.init.CQRSounds;

import java.util.Random;

public class ProjectileEnergyOrb extends DamagingProjectileEntity {

	private int deflectionsByPlayer = 0;
	public int innerRotation;
	public Random rand = new Random();
	public LivingEntity shootingEntity;

	public ProjectileEnergyOrb(EntityType<? extends DamagingProjectileEntity> entityType, World worldIn) {
		super(entityType, worldIn);
		this.innerRotation = this.rand.nextInt(100_000);
		//this.setSize(1.5F, 1.5F);
	}

	public ProjectileEnergyOrb(double x, double y, double z, double offsetX, double offsetY, double offsetZ, World level) {
		super(CQREntityTypes.PROJECTILE_ENERGY_ORB.get(), x, y, z, offsetX, offsetY, offsetZ, level);
		this.innerRotation = this.rand.nextInt(100_000);
	}

	public ProjectileEnergyOrb(LivingEntity shooter, double offsetX, double offsetY, double offsetZ, World level) {
		super(CQREntityTypes.PROJECTILE_ENERGY_ORB.get(), shooter, offsetX, offsetY, offsetZ, level);
		this.innerRotation = this.rand.nextInt(100_000);
		this.shootingEntity = shooter;
	}

	public int getDeflectionCount() {
		return this.deflectionsByPlayer;
	}

	@Override
	public void tick() {
		++this.innerRotation;
		super.tick();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		boolean result = super.hurt(source, amount);

		if (result) {
			if (source.getEntity() instanceof PlayerEntity) {
				this.deflectionsByPlayer++;
			}
		}

		return result;
	}

	/*
	 * Creates an orb that directly flies towards the target (no homing), it returns the spawned entity
	 */
	public static ProjectileEnergyOrb shootAt(Entity target, LivingEntity shooter, World world) {
		//Vector3d vec3d = shooter.getLook(1.0F);
		Vector3d vec3d = shooter.getLookAngle();
		double vx = target.getX() - (shooter.getX() + vec3d.x * shooter.getBbWidth());
		double vy = target.getBoundingBox().minY + target.getBbHeight() / 2.0F - (0.5D + shooter.getY() + shooter.getBbHeight() / 2.0F);
		double vz = target.getZ() - (shooter.getZ() + vec3d.z * shooter.getBbWidth());
		ProjectileEnergyOrb orb = new ProjectileEnergyOrb(shooter, vx, vy, vz, world);
		//orb.posX = shooter.getX() + vec3d.x * shooter.getBbWidth();
		//orb.posY = shooter.getY() + shooter.getBbHeight() / 2.0F + 0.5D;
		//orb.posZ = shooter.getZ() + vec3d.z * shooter.getBbWidth();
		orb.setPos(shooter.getX() + vec3d.x * shooter.getBbWidth(), shooter.getY() + shooter.getBbHeight() / 2.0F + 0.5D, shooter.getZ() + vec3d.z * shooter.getBbWidth());
		world.addFreshEntity(orb);

		return orb;
	}

	public void redirect(Entity target, LivingEntity shooter) {
		this.setOwner(shooter);// = shooter;
		//float vec3d = shooter.getViewYRot(1.0F);
		Vector3d vec3d = shooter.getLookAngle();
		double accelX = target.getX() - (shooter.getX() + vec3d.x * shooter.getBbWidth());
		double accelY = target.getBoundingBox().minY + target.getBbHeight() / 2.0F - (0.5D + shooter.getY() + shooter.getBbHeight() / 2.0F);
		double accelZ = target.getZ() - (shooter.getZ() + vec3d.z * shooter.getBbWidth());
		//this.posX = shooter.getX() + vec3d.x * shooter.getBbWidth();
		//this.posY = shooter.getY() + shooter.getBbHeight() / 2.0F + 0.5D;
		//this.posZ = shooter.getZ() + vec3d.z * shooter.getBbWidth();
		this.setPos(shooter.getX() + vec3d.x * shooter.getBbWidth(), shooter.getY() + shooter.getBbHeight() / 2.0F + 0.5D, shooter.getZ() + vec3d.z * shooter.getBbWidth());

		//this.motionX = 0.0D;
		//this.motionY = 0.0D;
		//this.motionZ = 0.0D;
		this.setDeltaMovement(0.0D, 0.0D, 0.0D);
		accelX = accelX + this.rand.nextGaussian() * 0.4D;
		accelY = accelY + this.rand.nextGaussian() * 0.4D;
		accelZ = accelZ + this.rand.nextGaussian() * 0.4D;
		double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.xPower = accelX / d0 * 0.1D;
		this.yPower = accelY / d0 * 0.1D;
		this.zPower = accelZ / d0 * 0.1D;

	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.deflectionsByPlayer = compound.getInt("deflections");
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("deflections", this.deflectionsByPlayer);
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult result)
	{
		super.onHitEntity(result);
		if (!result.getEntity().hurt(DamageSource.indirectMagic(this, this.shootingEntity), 4)) {
			return;
		}
		this.doEnchantDamageEffects(this.shootingEntity, result.getEntity());
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult result) {
		super.onHitBlock(result);
		this.level.explode(this.shootingEntity, this.getX(), this.getY(), this.getZ(), 0.0F, Explosion.Mode.NONE);
		AreaEffectCloudEntity entityareaeffectcloud = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
		entityareaeffectcloud.setOwner(this.shootingEntity);
		entityareaeffectcloud.setParticle(ParticleTypes.WITCH); //SPELL MOB
		entityareaeffectcloud.setFixedColor(0xFFFF26);// Yellow
		entityareaeffectcloud.setRadius(4F);
		entityareaeffectcloud.setDuration(400);
		entityareaeffectcloud.setRadiusOnUse(-0.125F);
		entityareaeffectcloud.setWaitTime(20);
		entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / entityareaeffectcloud.getDuration());
		entityareaeffectcloud.addEffect(new EffectInstance(Effects.POISON, 60, 2));

		this.level.addFreshEntity(entityareaeffectcloud);

		this.playSound(CQRSounds.PROJECTILE_ENERGY_SPHERE_IMPACT, 8.0F, 1.0F);

		this.remove();
	}
}