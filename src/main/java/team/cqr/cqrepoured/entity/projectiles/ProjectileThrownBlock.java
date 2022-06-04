package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class ProjectileThrownBlock extends ProjectileBase implements IEntityAdditionalSpawnData {

	private ResourceLocation block = Blocks.END_STONE.getRegistryName();
	private BlockState state = null;
	private boolean placeOnImpact = false;
	private LivingEntity shooter;

	public ProjectileThrownBlock(EntityType<? extends ProjectileBase> throwableEntity, World world) {
		super(throwableEntity, world);
	}

	public ProjectileThrownBlock(double pX, double pY, double pZ, World world) {
		super(CQREntityTypes.PROJECTILE_THROWN_BLOCK.get(), pX, pY, pZ, world);
	}

	public ProjectileThrownBlock(LivingEntity shooter, World world, BlockState block, boolean placeOnImpact)
	{
		super(CQREntityTypes.PROJECTILE_THROWN_BLOCK.get(), shooter, world);
		this.block = block.getBlock().getRegistryName();
		this.placeOnImpact = placeOnImpact;
		this.state = block;
		this.shooter = shooter;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		double dx = this.getX() + (-0.5 + (level.random.nextDouble()));
		double dy = 0.25 + this.getY() + (-0.5 + (level.random.nextDouble()));
		double dz = this.getZ() + (-0.5 + (level.random.nextDouble()));
		if(this.level.isClientSide()) {
			this.level.addParticle(ParticleTypes.DRAGON_BREATH, dx, dy, dz, 0, 0, 0);
		} else if(this.level instanceof ServerWorld) {
			((ServerWorld)this.level).sendParticles(ParticleTypes.DRAGON_BREATH, dx, dy, dz, 1, 0, 0, 0, 0.05D);
		}
	}

	public BlockState getBlock() {
		return this.state != null ? this.state : Blocks.BEDROCK.defaultBlockState();
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	public boolean isNoGravity() {
		return false;
	}

	@Override
	public void onHitEntity(EntityRayTraceResult entityResult)
	{
		super.onHitEntity(entityResult);

		Entity entity = entityResult.getEntity();

		if(entity == this.shooter) return;

		if(entity instanceof PartEntity && ((PartEntity<?>)entity).getParent() == this.shooter) return;

		entity.hurt(DamageSource.indirectMobAttack(this, this.shooter), 10);
		this.remove();
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult result)
	{
		super.onHitBlock(result);

		if(CQRConfig.bosses.thrownBlocksGetPlaced && this.placeOnImpact) {
			// TODO: Add placed block to whitelist of protected region
			this.level.setBlockAndUpdate(new BlockPos(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ()), this.state);
			// this.world.createExplosion(this.thrower, this.posX, this.posY, this.posZ, 1.5F, false);
			if (this.level instanceof ServerWorld) {
				ServerWorld ws = (ServerWorld) this.level;
				//Vector3d pos = result.getLocation();
				BlockPos pos = result.getBlockPos();
				double particleSpeed = 0.2D;
				for (int i = 0; i < 50; i++) {
					double dx = -0.5 + this.random.nextDouble();
					dx *= particleSpeed;
					double dy = -0.5 + this.random.nextDouble();
					dy *= particleSpeed;
					double dz = -0.5 + this.random.nextDouble();
					dz *= particleSpeed;
					ws.sendParticles(new BlockParticleData(ParticleTypes.BLOCK, this.state), pos.getX(), pos.getY(), pos.getZ(), 10, dx, dy, dz, 0.1);
					this.playSound(this.state.getBlock().getSoundType(this.state, this.level, this.blockPosition(), this).getPlaceSound(), 1.5F, 1.25F);
				}
			}
		}
		this.remove();
	}

/*	@Override
	protected void onHit(RayTraceResult result) {
		if (this.world.isRemote) {
			return;
		}

		if (result.typeOfHit == Type.ENTITY) {
			if (result.entityHit == this.thrower) {
				return;
			}

			if (result.entityHit instanceof PartEntity && ((PartEntity) result.entityHit).parent == this.thrower) {
				return;
			}

			result.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.thrower), 10);
			this.setDead();
			return;
		}
		if (CQRConfig.bosses.thrownBlocksGetPlaced && this.placeOnImpact) {
			// TODO: Add placed block to whitelist of protected region
			this.world.setBlockState(new BlockPos(result.hitVec.x, result.hitVec.y, result.hitVec.z), this.state);
			// this.world.createExplosion(this.thrower, this.posX, this.posY, this.posZ, 1.5F, false);
			if (this.world instanceof ServerWorld) {
				ServerWorld ws = (ServerWorld) this.world;
				Vector3d pos = result.hitVec;
				double particleSpeed = 0.2D;
				for (int i = 0; i < 50; i++) {
					double dx = -0.5 + this.rand.nextDouble();
					dx *= particleSpeed;
					double dy = -0.5 + this.rand.nextDouble();
					dy *= particleSpeed;
					double dz = -0.5 + this.rand.nextDouble();
					dz *= particleSpeed;
					ws.spawnParticle(ParticleTypes.BLOCK_CRACK, pos.x, pos.y, pos.z, dx, dy, dz, Block.getStateId(this.state));
					this.playSound(this.state.getBlock().getSoundType(this.state, this.world, this.getPosition(), this).getPlaceSound(), 1.5F, 1.25F);
				}
			}
		}

		this.setDead();
	} */

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound)
	{
		try {
			CompoundNBT blockstate = compound.getCompound("blockdata");
			this.state = NBTUtil.readBlockState(blockstate);
		} catch (Exception ex) {
			// Ignore
			this.state = Blocks.END_STONE.defaultBlockState();
		}
		super.readAdditionalSaveData(compound);
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound)
	{
		CompoundNBT tag = NBTUtil.writeBlockState(this.state);
		tag.put("blockdata", tag);
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeResourceLocation(this.block);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData)
	{
		this.block = additionalData.readResourceLocation();
		this.state = ForgeRegistries.BLOCKS.getValue(this.block).defaultBlockState();
	}
}
