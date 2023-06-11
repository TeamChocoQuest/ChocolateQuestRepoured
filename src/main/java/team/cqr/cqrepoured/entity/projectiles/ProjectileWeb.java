package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.network.IPacket;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.AirBlock;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.world.structure.generation.generators.volcano.GeneratorVolcano;

public class ProjectileWeb extends ProjectileBase {

	private LivingEntity shooter;

	public ProjectileWeb(EntityType<? extends ProjectileBase> throwableEntity, World world) {
		super(throwableEntity, world);
	}

	public ProjectileWeb(double pX, double pY, double pZ, World world)
	{
		super(CQREntityTypes.PROJECTILE_WEB.get(), world);
	}

	public ProjectileWeb(LivingEntity shooter, World world)
	{
		super(CQREntityTypes.PROJECTILE_WEB.get(), shooter, world);
		this.shooter = shooter;
		//this.isImmuneToFire = false;
	}
	
	@Override
	protected boolean canHitEntity(Entity pTarget) {
		return super.canHitEntity(pTarget) && pTarget != this.getOwner();
	}

/*	@Override
	protected void onHit(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
				if (result.entityHit instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) result.entityHit;

					if (result.entityHit == this.shooter) {
						return;
					}

					entity.addPotionEffect(new EffectInstance(Effects.POISON, 60, 0));
					entity.setInWeb();
					this.world.setBlockState(entity.getPosition(), CQRBlocks.POISONOUS_WEB.getDefaultState());
					this.setDead();
				}
			} else if (DungeonGenUtils.percentageRandom(75)) {
				GeneratorVolcano.forEachSpherePosition(this.getPosition(), DungeonGenUtils.randomBetween(1, 3), t -> {
					if (ProjectileWeb.this.world.getBlockState(t).getBlock() instanceof AirBlock) {
						ProjectileWeb.this.world.setBlockState(t, CQRBlocks.TEMPORARY_WEB.getDefaultState());
					}
				});
			}
			super.onHit(result);
		}
	} */

	@Override
	protected void onHitBlock(BlockRayTraceResult result)
	{
		if(DungeonGenUtils.percentageRandom(75))
		{
			GeneratorVolcano.forEachSpherePosition(blockPosition(), DungeonGenUtils.randomBetween(1, 3), t ->
			{
				if(ProjectileWeb.this.level.getBlockState(t).getBlock() instanceof AirBlock)
				{
					//ProjectileWeb.this.level.setBlockAndUpdate(t, CQRBlocks) //#TODO temporary web?
				}
			});
		}
		super.onHitBlock(result);
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult result)
	{
		if(result.getEntity() instanceof LivingEntity)
		{
			LivingEntity entity = (LivingEntity)result.getEntity();

			if(entity == this.shooter) return;

			entity.addEffect(new EffectInstance(Effects.POISON, 60, 0));
			//entity.makeStuckInBlock(); //Dont now what about this #TODO
			this.level.setBlockAndUpdate(entity.blockPosition(), CQRBlocks.POISONOUS_WEB.get().defaultBlockState());
			this.remove();
		}
		super.onHitEntity(result);
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	public boolean isNoGravity() {
		return false;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
