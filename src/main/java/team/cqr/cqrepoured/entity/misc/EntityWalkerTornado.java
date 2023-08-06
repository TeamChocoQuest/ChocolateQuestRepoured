package team.cqr.cqrepoured.entity.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.world.entity.MoverType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
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
	protected Vec3 velocity = new Vec3(0, 0, 0);
	protected Entity owner = null;

	public static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.<Integer>defineId(EntityWalkerTornado.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<String> OWNER_ID = SynchedEntityData.<String>defineId(EntityWalkerTornado.class, EntityDataSerializers.STRING);

	public EntityWalkerTornado(Level world) {
		this(CQREntityTypes.WALKER_TORNADO.get(), world);
	}
	
	public EntityWalkerTornado(EntityType<? extends EntityWalkerTornado> type, Level worldIn) {
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
			this.discard();
			return;
		}

		if (this.level().isClientSide()) {
			this.updateParticles();
		} else {
			this.handleNearbyEntities();
		}

		if (this.getOwnerId() != null && this.owner == null && this.tickCount % 10 == 0) {
			if (this.level() instanceof ServerLevel) {
				Entity ent = ((ServerLevel) this.level()).getEntity(this.getOwnerId());
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
			final EntityParticle particle = new ParticleWalkerTornado((ClientLevel) this.level(), -Math.sin(0.01745329f * f) * 0.75, d2 - 0.25, Math.cos(0.01745329f * f) * 0.75, d1, 0.125, d3);
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
		return Mth.sqrt(f * f + f2 * f2 + f3 * f3);
	}

	private void handleNearbyEntities() {
		double r = 0.75D;
		AABB aabb = new AABB(this.getX() - r, this.getY(), this.getZ() - r, this.getX() + r, this.getY() + 2 * r, this.getZ() + r);
		final List<Entity> list = this.level().getEntities(this, aabb);
		for (Entity ent : list) {
			this.collideWithEntity(ent);
		}
	}

	protected void collideWithEntity(Entity entityIn) {
		if (this.isEntityAffected(entityIn)) {
			Vec3 vAway = entityIn.position().subtract(this.position()).normalize().scale(1.25D);
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
	protected void addAdditionalSaveData(CompoundTag compound) {
		if (this.getOwnerId() != null) {
			compound.put("summoner", NbtUtils.createUUID(this.getOwnerId()));
		}
		compound.putDouble("vX", this.velocity.x);
		compound.putDouble("vY", this.velocity.y);
		compound.putDouble("vZ", this.velocity.z);
		compound.putInt("ticksExisted", this.tickCount);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		if (compound.contains("summoner")) {
			this.setOwner(NbtUtils.loadUUID(compound.get("summoner")));
		}
		double x = compound.getDouble("vX");
		double y = compound.getDouble("vY");
		double z = compound.getDouble("vZ");
		this.velocity = new Vec3(x, y, z);
		this.tickCount = compound.getInt("ticksExisted");
	}

	public void setVelocity(Vec3 v) {
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
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
