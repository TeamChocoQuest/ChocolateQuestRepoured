package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.util.EntityUtil;

public class ProjectileBullet extends ProjectileBase implements IEntityAdditionalSpawnData {

	public static enum EBulletType {

		IRON(2.5F),
		// WHY does gold do more fire than iron?!
		GOLD(3.75F), DIAMOND(5.0F), FIRE(5.0F, true), NETHERITE(6.0F, true),;

		protected final float bonusDamage;
		protected final ResourceLocation texture;
		protected final boolean fireDamage;

		EBulletType(final float addDmg) {
			this(addDmg, false);
		}

		EBulletType(final float addDmg, final boolean fire) {
			this.bonusDamage = addDmg;
			this.texture = CQRMain.prefix("textures/entity/bullet_" + this.name().toLowerCase() + "_single.png");
			this.fireDamage = fire;
		}

		public ResourceLocation getTexture() {
			return this.texture;
		}

		public double getAdditionalDamage() {
			return this.bonusDamage;
		}

		public boolean fireDamage() {
			return this.fireDamage;
		}

	}

	private LivingEntity shooter;
	private EBulletType type;

	public ProjectileBullet(EntityType<? extends ProjectileBase> throwableEntity, World world) {
		super(throwableEntity, world);
		this.type = EBulletType.IRON;
	}

	public ProjectileBullet(double pX, double pY, double pZ, World world, EBulletType type) {
		super(CQREntityTypes.PROJECTILE_BULLET.get(), world);
		this.type = type;
	}

	public ProjectileBullet(LivingEntity shooter, World world, EBulletType type) {
		super(CQREntityTypes.PROJECTILE_BULLET.get(), shooter, world);
		this.type = type;
		this.shooter = shooter;
	}

	public EBulletType getBulletType() {
		return this.type;
	}

	@Override
	public void onHitEntity(EntityRayTraceResult entityResult) {
		Entity entity = entityResult.getEntity();

		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;

			float damage = 5.0F;
			damage += this.type.bonusDamage;
			if (this.type.fireDamage) {
				livingEntity.setSecondsOnFire(3);
			}
			if (EntityUtil.isEntityFlying(entity)) {
				damage *= 2;
			}
			entity.hurt(DamageSource.indirectMobAttack(this, this.shooter), damage);
			this.remove();
		}
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult result) {
		super.onHitBlock(result);
		this.remove();
	}

	/*
	 * @Override protected void onHit(RayTraceResult result) { if (!this.level.isRemote) { if (result.typeOfHit == RayTraceResult.Type.ENTITY) { if (result.entityHit == this.thrower) { return; }
	 * 
	 * if (result.entityHit instanceof LivingEntity) { LivingEntity entity = (LivingEntity) result.entityHit;
	 * 
	 * float damage = 5.0F; if (this.type == 1) { damage += 2.5F; } else if (this.type == 2) { damage += 3.75F; } else if (this.type == 3) { damage += 5.0F; } else if (this.type == 4) { damage += 5.0F;
	 * 
	 * if (entity.attackEntityFrom(new IndirectEntityDamageSource("onFire", this, this.thrower).setFireDamage(), damage / 2)) { entity.setFire(3); } } if (EntityUtil.isEntityFlying(entity)) { damage *= 2; }
	 * 
	 * entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.thrower), damage); this.setDead(); } }
	 * 
	 * super.onHit(result); } }
	 */

	@Override
	protected void onUpdateInAir() {
		if (this.level.isClientSide) {
			if (this.tickCount < 10) {
				this.level.addParticle(ParticleTypes.SMOKE, this.position().x, this.position().y, this.position().z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeInt(this.type.ordinal());
	}

	@Override
	public void readSpawnData(PacketBuffer buffer) {
		this.type = EBulletType.values()[buffer.readInt()];
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
