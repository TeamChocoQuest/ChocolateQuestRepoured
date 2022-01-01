package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Potions;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class ProjectilePoisonSpell extends ProjectileSpiderBall {

	private LivingEntity shooter;
	private boolean canPlaceAura = false;

	public ProjectilePoisonSpell(World worldIn) {
		super(worldIn);
	}

	public ProjectilePoisonSpell(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public ProjectilePoisonSpell(World worldIn, LivingEntity shooter) {
		super(worldIn, shooter);
		this.shooter = shooter;
		this.isImmuneToFire = false;
		this.damage = 1.0F;
	}

	public void enableAuraPlacement() {
		this.canPlaceAura = true;
	}

	@Override
	public boolean hasNoGravity() {
		return false;
	}

	@Override
	protected void onUpdateInAir() {
	}

	public LivingEntity getShooter() {
		return this.shooter;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (this.world.isRemote) {
			return;
		}

		if (this.canPlaceAura && DungeonGenUtils.percentageRandom(0.6)) {
			AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(this.world, this.posX, this.posY, this.posZ);

			cloud.setOwner(this.shooter);
			cloud.setRadius(DungeonGenUtils.randomBetween(1, 3));
			cloud.setRadiusOnUse(-0.1F);
			cloud.setWaitTime(10);
			cloud.setDuration(300);
			cloud.setRadiusPerTick(-cloud.getRadius() / cloud.getDuration());
			cloud.setPotion(Potions.STRONG_POISON);
			cloud.setColor(35849);
			cloud.setNoGravity(false);

			this.world.spawnEntity(cloud);
		}

		super.onImpact(result);
	}
}
