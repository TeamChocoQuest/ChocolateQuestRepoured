package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class ProjectileThrownBlock extends ProjectileBase implements IEntityAdditionalSpawnData {

	private ResourceLocation block = Blocks.END_STONE.getRegistryName();
	private BlockState state = null;
	private boolean placeOnImpact = false;

	public ProjectileThrownBlock(EntityType<? extends ProjectileBase> throwableEntity, Level world) {
		super(throwableEntity, world);
	}

	public ProjectileThrownBlock(double pX, double pY, double pZ, Level world) {
		super(CQREntityTypes.PROJECTILE_THROWN_BLOCK.get(), pX, pY, pZ, world);
	}

	public ProjectileThrownBlock(LivingEntity shooter, Level world, BlockState block, boolean placeOnImpact)
	{
		super(CQREntityTypes.PROJECTILE_THROWN_BLOCK.get(), shooter, world);
		this.block = block.getBlock().getRegistryName();
		this.placeOnImpact = placeOnImpact;
		this.state = block;
		//this.shooter = shooter;
		this.setOwner(shooter);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		double dx = this.getX() + (-0.5 + (level.random.nextDouble()));
		double dy = 0.25 + this.getY() + (-0.5 + (level.random.nextDouble()));
		double dz = this.getZ() + (-0.5 + (level.random.nextDouble()));
		if(this.level.isClientSide()) {
			this.level.addParticle(ParticleTypes.DRAGON_BREATH, dx, dy, dz, 0, 0, 0);
		} else if(this.level instanceof ServerLevel) {
			((ServerLevel)this.level).sendParticles(ParticleTypes.DRAGON_BREATH, dx, dy, dz, 1, 0, 0, 0, 0.05D);
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
	protected boolean canHitEntity(Entity pTarget) {
		return super.canHitEntity(pTarget) && pTarget != this.getOwner();
	}

	@Override
	public void onHitEntity(EntityHitResult entityResult)
	{
		super.onHitEntity(entityResult);

		Entity entity = entityResult.getEntity();

		if(entity == this.getOwner()) return;

		if(entity instanceof PartEntity && ((PartEntity<?>)entity).getParent() == this.getOwner()) return;

		entity.hurt(DamageSource.indirectMobAttack(this, this.getOwner() instanceof LivingEntity ? (LivingEntity) this.getOwner() : null), 10);
		this.remove();
	}

	@Override
	protected void onHitBlock(BlockHitResult result)
	{
		super.onHitBlock(result);

		if(CQRConfig.SERVER_CONFIG.bosses.thrownBlocksGetPlaced.get() && this.placeOnImpact) {
			// TODO: Add placed block to whitelist of protected region
			this.level.setBlockAndUpdate(new BlockPos(this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ()), this.state);
			// this.world.createExplosion(this.thrower, this.posX, this.posY, this.posZ, 1.5F, false);
			if (this.level instanceof ServerLevel) {
				ServerLevel ws = (ServerLevel) this.level;
				//Vector3d pos = result.getLocation();
				BlockPos pos = result.getBlockPos();
				double particleSpeed = 0.2D;
				double dx = -0.5 + this.random.nextDouble();
				dx *= particleSpeed;
				double dy = -0.5 + this.random.nextDouble();
				dy *= particleSpeed;
				double dz = -0.5 + this.random.nextDouble();
				dz *= particleSpeed;
				ws.sendParticles(new BlockParticleData(ParticleTypes.BLOCK, this.state), pos.getX(), pos.getY(), pos.getZ(), 50, dx, dy, dz, 0.1);
				this.playSound(this.state.getBlock().getSoundType(this.state, this.level, this.blockPosition(), this).getPlaceSound(), 1.5F, 1.25F);
			}
		}
		this.remove();
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound)
	{
		try {
			CompoundTag blockstate = compound.getCompound("blockdata");
			this.state = NbtUtils.readBlockState(blockstate);
		} catch (Exception ex) {
			// Ignore
			this.state = Blocks.END_STONE.defaultBlockState();
		}
		super.readAdditionalSaveData(compound);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound)
	{
		CompoundTag tag = NbtUtils.writeBlockState(this.state);
		tag.put("blockdata", tag);
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer)
	{
		buffer.writeResourceLocation(this.block);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData)
	{
		this.block = additionalData.readResourceLocation();
		this.state = ForgeRegistries.BLOCKS.getValue(this.block).defaultBlockState();
	}
}
