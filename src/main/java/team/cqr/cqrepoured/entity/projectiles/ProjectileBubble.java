package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraftforge.entity.PartEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.misc.EntityBubble;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class ProjectileBubble extends ProjectileBase {

	private LivingEntity shooter;
	protected float damage;

	public ProjectileBubble(EntityType<? extends ThrowableEntity> throwableEntity, World world) {
		super(throwableEntity, world);
	}

	public ProjectileBubble(double pX, double pY, double pZ, World world) {
		super(CQREntityTypes.PROJECTILE_BUBBLE.get(), world);
	}

	public ProjectileBubble(LivingEntity shooter, World world)
	{
		super(CQREntityTypes.PROJECTILE_BUBBLE.get(), shooter, world);
		this.shooter = shooter;
		this.damage = 1F;
		//this.fireImmune() FireImmune in entity constructor
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
	protected void onHit(RayTraceResult pResult)
	{
		RayTraceResult.Type raytraceresult$type = pResult.getType();
		if(raytraceresult$type == RayTraceResult.Type.ENTITY)
		{
			EntityRayTraceResult entityResult = ((EntityRayTraceResult)pResult);
			if(!(entityResult.getEntity() instanceof PartEntity) && entityResult.getEntity() != this.shooter)
			{
				this.onHitEntity((EntityRayTraceResult)pResult);
			}
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
	public void onHitEntity(EntityRayTraceResult entityResult)
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

		if (entity instanceof MobEntity && ((MobEntity)entity).getUseItem().getItem() instanceof ShieldItem) {
			return;
		}

		entity.hurt(DamageSource.indirectMagic(this.shooter, this), this.damage);
		float pitch = (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F;
		this.level.playLocalSound(this.position().x, this.position().y, this.position().z, SoundEvents.PLAYER_SWIM, SoundCategory.PLAYERS, 4, pitch, true);

		EntityBubble bubbles = CQREntityTypes.BUBBLE.get().create(this.level);
		bubbles.moveTo(entity.blockPosition().offset(0, 0.25, 0), entity.yRot, entity.xRot);
		this.level.addFreshEntity(bubbles);

		entity.startRiding(bubbles, true);

		this.remove();
	}

	@Override
	protected void onUpdateInAir() {
		super.onUpdateInAir();
		if (this.level.isClientSide) {
			if (this.tickCount % 5 == 0) {
				this.level.addParticle(ParticleTypes.BUBBLE, this.position().x, this.position().y + 0.1D, this.position().z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected void defineSynchedData() {

	}
}
