package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.Potions;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class ProjectilePoisonSpell extends ProjectileBase {

	private LivingEntity shooter;
	private boolean canPlaceAura = false;
	protected float damage;

	public ProjectilePoisonSpell(EntityType<? extends ProjectileBase> throwableEntity, World world) {
		super(throwableEntity, world);
	}

	public ProjectilePoisonSpell(double pX, double pY, double pZ, World world) {
		super(CQREntityTypes.PROJECTILE_POISON_SPELL.get(), pX, pY, pZ, world);
	}

	public ProjectilePoisonSpell(LivingEntity shooter, World world)
	{
		super(CQREntityTypes.PROJECTILE_POISON_SPELL.get(), shooter, world);
		this.shooter = shooter;
		this.damage = 1.0F;
	}

	public void enableAuraPlacement() {
		this.canPlaceAura = true;
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	public boolean isNoGravity() {
		return false;
	}

	@Override
	protected void onUpdateInAir() {
	}

	public LivingEntity getShooter() {
		return this.shooter;
	}

	@Override
	public void onHitEntity(EntityRayTraceResult entityResult)
	{
		if(entityResult.getEntity() instanceof LivingEntity)
		{
			LivingEntity entity = (LivingEntity)entityResult.getEntity();

			if(entity == this.shooter) return;

			entity.addEffect(new EffectInstance(Effects.POISON, 100, 0));
			entity.hurt(DamageSource.MAGIC, this.damage);
			this.remove();
		}
	}

	@Override
	protected void onHit(RayTraceResult result) {
		if (this.level.isClientSide) {
			return;
		}

		if (this.canPlaceAura && DungeonGenUtils.percentageRandom(0.6)) {
			AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());

			cloud.setOwner(this.shooter);
			cloud.setRadius(DungeonGenUtils.randomBetween(1, 3));
			cloud.setRadiusOnUse(-0.1F);
			cloud.setWaitTime(10);
			cloud.setDuration(300);
			cloud.setRadiusPerTick(-cloud.getRadius() / cloud.getDuration());
			cloud.setPotion(Potions.STRONG_POISON);
			cloud.setFixedColor(35849);
			cloud.setNoGravity(false);

			this.level.addFreshEntity(cloud);
		}

		super.onHit(result);
	}
}
