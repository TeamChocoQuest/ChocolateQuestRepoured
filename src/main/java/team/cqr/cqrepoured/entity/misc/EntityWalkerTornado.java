package team.cqr.cqrepoured.entity.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.entity.IDontRenderFire;
import team.cqr.cqrepoured.entity.particle.EntityParticle;
import team.cqr.cqrepoured.entity.particle.ParticleWalkerTornado;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class EntityWalkerTornado extends Entity implements IEntityOwnable, IDontRenderFire {

	protected static final int PARTICLE_COUNT = 2;
	protected static final int MAX_LIVING_TICKS = 200;
	protected final List<EntityParticle> particles = new ArrayList<>();
	protected Vector3d velocity = new Vector3d(0, 0, 0);
	protected Entity owner = null;

	public static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>defineId(EntityWalkerTornado.class, DataSerializers.INT);
	public static final DataParameter<String> OWNER_ID = EntityDataManager.<String>defineId(EntityWalkerTornado.class, DataSerializers.STRING);

	public EntityWalkerTornado(World world) {
		this(CQREntityTypes.WALKER_TORNADO.get(), world);
	}
	
	public EntityWalkerTornado(EntityType<? extends EntityWalkerTornado> type, World worldIn) {
		super(type, worldIn);
	}
	
	@Override
	public boolean fireImmune() {
		return true;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.tickCount >= EntityWalkerTornado.MAX_LIVING_TICKS) {
			this.remove();
			return;
		}

		if (this.level.isClientSide()) {
			this.updateParticles();
		} else {
			this.handleNearbyEntities();
		}

		if (this.getOwnerId() != null && this.owner == null && this.tickCount % 10 == 0) {
			if (this.level instanceof ServerWorld) {
				Entity ent = ((ServerWorld) this.level).getEntity(this.getOwnerId());
				if (ent.isAlive()) {
					this.owner = ent;
				}
			}

			/*
			 * for (Entity entity : this.world.loadedEntityList) { if (entity instanceof EntityLivingBase &&
			 * this.getOwnerID().equals(entity.getPersistentID()) &&
			 * entity.isEntityAlive()) { this.owner = entity; } }
			 */
		}

		this.move(MoverType.SELF, this.velocity);
	}

	// Particle code taken from aether legacy's whirlwind
	@OnlyIn(Dist.CLIENT)
	public void updateParticles() {
		final Integer color = this.getColor();
		for (int k = 0; k < 4; ++k) {
			final double d1 = (float) this.getX() + this.random.nextFloat() * 0.25f;
			final double d2 = (float) this.getY() + this.getBbHeight() + 0.125f;
			final double d3 = (float) this.getZ() + this.random.nextFloat() * 0.25f;
			final float f = this.random.nextFloat() * 360.0f;
			final EntityParticle particle = new ParticleWalkerTornado((ClientWorld) this.level, -Math.sin(0.01745329f * f) * 0.75, d2 - 0.25, Math.cos(0.01745329f * f) * 0.75, d1, 0.125, d3);
			//Still needed?
			//FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
			this.particles.add(particle);
			particle.setColor(((color >> 16) & 0xFF) / 255.0f, ((color >> 8) & 0xFF) / 255.0f, (color & 0xFF) / 255.0f);
			particle.setPos(this.getX(), this.getY(), this.getZ());
		}

		for (int i2 = 0; i2 < this.particles.size(); ++i2) {
			final EntityParticle particle3 = this.particles.get(i2);
			if (!particle3.isAlive()) {
				this.particles.remove(particle3);
				i2--;
			} else {
				final double d7 = particle3.getX();
				final double d8 = particle3.getBoundingBox().minY;
				final double d9 = particle3.getZ();
				final double d10 = this.getDistanceToParticle(particle3);
				final double d11 = d8 - this.getY();
				particle3.setMotionY(0.11500000208616257 /* + this.motionY */);
				double d12 = Math.atan2(this.getX() - d7, this.getZ() - d9) / 0.01745329424738884;
				d12 += 160.0;
				particle3.setMotionX(-Math.cos(0.01745329424738884 * d12) * (d10 * 2.5 - d11) * 0.10000000149011612);
				particle3.setMotionZ(Math.sin(0.01745329424738884 * d12) * (d10 * 2.5 - d11) * 0.10000000149011612);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public float getDistanceToParticle(final EntityParticle particle) {
		final float f = (float) (this.getX() - particle.getX());
		final float f2 = (float) (this.getY() - particle.getY());
		final float f3 = (float) (this.getZ() - particle.getZ());
		return MathHelper.sqrt(f * f + f2 * f2 + f3 * f3);
	}

	private void handleNearbyEntities() {
		double r = 0.75D;
		AxisAlignedBB aabb = new AxisAlignedBB(this.getX() - r, this.getY(), this.getZ() - r, this.getX() + r, this.getY() + 2 * r, this.getZ() + r);
		final List<Entity> list = this.level.getEntities(this, aabb);
		for (Entity ent : list) {
			this.collideWithEntity(ent);
		}
	}

	protected void collideWithEntity(Entity entityIn) {
		if (this.isEntityAffected(entityIn)) {
			Vector3d vAway = entityIn.position().subtract(this.position()).normalize().scale(1.25D);
			vAway = vAway.add(0, vAway.y * 0.1D, 0);
			/*entityIn.motionX = vAway.x * 0.75;
			entityIn.motionY = Math.max(Math.abs(vAway.y), 0.6D);
			entityIn.motionZ = vAway.z * 0.75;*/
			entityIn.setDeltaMovement(vAway.x * 0.75, Math.max(Math.abs(vAway.y), 0.6D), vAway.z * 0.75);
			entityIn.hasImpulse = true;
			//entityIn.velocityChanged = true;
		}
	}

	private boolean isEntityAffected(Entity ent) {
		if (ent instanceof EntityWalkerTornado) {
			return false;
		}
		if (this.getOwnerId() != null) {
			if (ent.getUUID().equals(this.getOwnerId())) {
				return false;
			}
			Faction faction = FactionRegistry.instance(this).getFactionOf(this.owner);
			if (faction != null) {
				return !faction.isAlly(ent);
			}
		}
		return true;
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {
		if (this.getOwnerId() != null) {
			compound.put("summoner", NBTUtil.createUUID(this.getOwnerId()));
		}
		compound.putDouble("vX", this.velocity.x);
		compound.putDouble("vY", this.velocity.y);
		compound.putDouble("vZ", this.velocity.z);
		compound.putInt("ticksExisted", this.tickCount);
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {
		if (compound.contains("summoner")) {
			this.setOwner(NBTUtil.loadUUID(compound.get("summoner")));
		}
		double x = compound.getDouble("vX");
		double y = compound.getDouble("vY");
		double z = compound.getDouble("vZ");
		this.velocity = new Vector3d(x, y, z);
		this.tickCount = compound.getInt("ticksExisted");
	}

	public void setVelocity(Vector3d v) {
		this.velocity = v;
	}

	public void setOwner(UUID ownerID) {
		this.entityData.set(OWNER_ID, ownerID.toString());
	}

	@Override
	public UUID getOwnerId() {
		if (this.entityData.get(OWNER_ID) != null && !this.entityData.get(OWNER_ID).isEmpty()) {
			return UUID.fromString(this.entityData.get(OWNER_ID));
		}
		return null;
	}

	public void setColor(int value) {
		this.entityData.set(COLOR, value);
	}

	private Integer getColor() {
		return this.entityData.get(COLOR);
	}

	@Override
	public Entity getOwner() {
		return this.owner;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(COLOR, 0x4C0099);
		this.entityData.define(OWNER_ID, "");
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
