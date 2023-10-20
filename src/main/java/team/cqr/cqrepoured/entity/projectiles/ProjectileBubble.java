package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.entity.misc.EntityBubble;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class ProjectileBubble extends ProjectileBase {

	private LivingEntity shooter;
	protected float damage;

	public ProjectileBubble(EntityType<? extends ProjectileBase> throwableEntity, Level world) {
		super(throwableEntity, world);
	}

	public ProjectileBubble(double pX, double pY, double pZ, Level world) {
		super(CQREntityTypes.PROJECTILE_BUBBLE.get(), world);
	}

	public ProjectileBubble(LivingEntity shooter, Level world)
	{
		super(CQREntityTypes.PROJECTILE_BUBBLE.get(), shooter, world);
		this.shooter = shooter;
		this.damage = 1F;
		//this.fireImmune() FireImmune in entity constructor
	}
	
	@Override
	protected boolean canHitEntity(Entity pTarget) {
		return super.canHitEntity(pTarget) && pTarget != this.getOwner();
	}

/*	public ProjectileBubble(World worldIn) {
		super(worldIn);
	}

	public ProjectileBubble(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public ProjectileBubble(World worldIn, LivingEntity shooter) {
		super(worldIn, shooter);
		this.shooter = shooter;
		this.damage = 1F;
		this.isImmuneToFire = true;
	} */

	@Override
	protected void onDestroyedByBlockImpact() {
		super.onDestroyedByBlockImpact();
		
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0, 0.05, 0);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0, -0.05, 0);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0.05, 0, 0);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), -0.05, 0, 0);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0, 0, 0.05);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0, 0, -0.05);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0.05, 0, 0.05);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), -0.05, 0, -0.05);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0.05, 0.05, 0);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), -0.05, 0.05, 0);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0, 0.05, 0.05);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0, 0.05, -0.05);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0.05, 0.05, 0.05);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), -0.05, 0.05, -0.05);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0.05, -0.05, 0);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), -0.05, -0.05, 0);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0, -0.05, 0.05);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0, -0.05, -0.05);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), 0.05, -0.05, 0.05);
		this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY(), this.getZ(), -0.05, -0.05, -0.05);
	}
	
	@Override
	protected void onHit(HitResult pResult)
	{
		HitResult.Type raytraceresult$type = pResult.getType();
		if(raytraceresult$type == HitResult.Type.ENTITY)
		{
			EntityHitResult entityResult = ((EntityHitResult)pResult);
			if(!(entityResult.getEntity() instanceof PartEntity) && entityResult.getEntity() != this.shooter)
			{
				this.onHitEntity((EntityHitResult)pResult);
			}
		} else if (raytraceresult$type == HitResult.Type.BLOCK) {
			super.onHitBlock((BlockHitResult) pResult);
		}
		super.onHit(pResult);
	}
/*	@Override
	protected void onHit(RayTraceResult result)
	{
		RayTraceResult.Type type = result.getType();

		if(!this.level.isClientSide && type == RayTraceResult.Type.ENTITY)
		{ //&& result.entityHit != null && result.entityHit != this.shooter && !(result.entityHit instanceof PartEntity)) {
			this.onHitEntity((EntityRayTraceResult)type);
		}

		super.onHit(result);
	} */

	@Override
	public void onHitEntity(EntityHitResult entityResult)
	{
		Entity entity = entityResult.getEntity();

		if (entity == this.shooter) {
			return;
		}

		if (entity instanceof EntityBubble || entity instanceof ProjectileBubble) {
			return;
		}

		if (entity.isPassenger() && entity.getVehicle() instanceof EntityBubble) {
			return;
		}

		if (entity instanceof Mob && ((Mob)entity).getUseItem().getItem() instanceof ShieldItem) {
			return;
		}

		entity.hurt(DamageSource.indirectMagic(this.shooter, this), this.damage);
		float pitch = (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F;
		this.level().playLocalSound(this.position().x, this.position().y, this.position().z, SoundEvents.PLAYER_SWIM, SoundSource.PLAYERS, 4, pitch, true);

		EntityBubble bubbles = CQREntityTypes.BUBBLE.get().create(this.level());
		bubbles.moveTo(entity.blockPosition().offset(0, 0.25, 0), entity.getYRot(), entity.getXRot());
		this.level().addFreshEntity(bubbles);

		entity.startRiding(bubbles, true);

		this.discard();
	}

	@Override
	protected void onUpdateInAir() {
		super.onUpdateInAir();
		if (this.level().isClientSide()) {
			if (this.tickCount % 5 == 0) {
				this.level().addParticle(ParticleTypes.BUBBLE, this.position().x, this.position().y + 0.1D, this.position().z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected void defineSynchedData() {

	}
	
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
