package com.teamcqr.chocolatequestrepoured.objects.entity.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;
import com.teamcqr.chocolatequestrepoured.objects.entity.particle.EntityParticle;
import com.teamcqr.chocolatequestrepoured.objects.entity.particle.ParticleWalkerTornado;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWalkerTornado extends Entity {

	protected static final int PARTICLE_COUNT = 2;
	protected static final int MAX_LIVING_TICKS = 200;
	protected final ArrayList<EntityParticle> particles = new ArrayList<>();
	protected Vec3d velocity = new Vec3d(0, 0, 0);
	protected Entity owner = null;

	public static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityWalkerTornado.class, DataSerializers.VARINT);
	public static final DataParameter<String> OWNER_ID = EntityDataManager.<String>createKey(EntityWalkerTornado.class, DataSerializers.STRING);

	public EntityWalkerTornado(World worldIn) {
		super(worldIn);
		this.isImmuneToFire = true;
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(COLOR, 4980889);
		this.dataManager.register(OWNER_ID, "");
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (this.ticksExisted >= EntityWalkerTornado.MAX_LIVING_TICKS) {
			this.setDead();
			return;
		}

		if (this.world.isRemote) {
			this.updateParticles();
		} else {
			this.handleNearbyEntities();
		}

		if (this.getOwnerID() != null && this.owner == null && this.ticksExisted % 10 == 0) {
			if (this.world instanceof WorldServer) {
				Entity ent = ((WorldServer) this.world).getEntityFromUuid(getOwnerID());
				if (ent.isEntityAlive()) {
					this.owner = ent;
				}
			}

			/*
			 * for (Entity entity : this.world.loadedEntityList) { if (entity instanceof EntityLivingBase && this.getOwnerID().equals(entity.getPersistentID()) &&
			 * entity.isEntityAlive()) { this.owner = entity; } }
			 */
		}

		this.move(MoverType.SELF, this.velocity.x, this.velocity.y, this.velocity.z);
	}

	// Particle code taken from aether legacy's whirlwind
	@SideOnly(Side.CLIENT)
	public void updateParticles() {
		final Integer color = this.getColor();
		for (int k = 0; k < 4; ++k) {
			final double d1 = (float) this.posX + this.rand.nextFloat() * 0.25f;
			final double d2 = (float) this.posY + this.height + 0.125f;
			final double d3 = (float) this.posZ + this.rand.nextFloat() * 0.25f;
			final float f = this.rand.nextFloat() * 360.0f;
			final EntityParticle particle = new ParticleWalkerTornado(this.world, -Math.sin(0.01745329f * f) * 0.75, d2 - 0.25, Math.cos(0.01745329f * f) * 0.75, d1, 0.125, d3);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect((Particle) particle);
			this.particles.add(particle);
			particle.setRBGColorF(((color >> 16) & 0xFF) / 255.0f, ((color >> 8) & 0xFF) / 255.0f, (color & 0xFF) / 255.0f);
			particle.setPosition(this.posX, this.posY, this.posZ);
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
				final double d11 = d8 - this.posY;
				particle3.setMotionY(0.11500000208616257 /* + this.motionY */);
				double d12 = Math.atan2(this.posX - d7, this.posZ - d9) / 0.01745329424738884;
				d12 += 160.0;
				particle3.setMotionX(-Math.cos(0.01745329424738884 * d12) * (d10 * 2.5 - d11) * 0.10000000149011612);
				particle3.setMotionZ(Math.sin(0.01745329424738884 * d12) * (d10 * 2.5 - d11) * 0.10000000149011612);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public float getDistanceToParticle(final EntityParticle particle) {
		final float f = (float) (this.posX - particle.getX());
		final float f2 = (float) (this.posY - particle.getY());
		final float f3 = (float) (this.posZ - particle.getZ());
		return MathHelper.sqrt(f * f + f2 * f2 + f3 * f3);
	}

	private void handleNearbyEntities() {
		double r = 0.75D;
		AxisAlignedBB aabb = new AxisAlignedBB(this.posX - r, this.posY, this.posZ - r, this.posX + r, this.posY + 2 * r, this.posZ + r);
		final List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity((Entity) this, aabb);
		for (Entity ent : list) {
			this.collideWithEntity(ent);
		}
	}

	protected void collideWithEntity(Entity entityIn) {
		if (this.isEntityAffected(entityIn)) {
			Vec3d vAway = entityIn.getPositionVector().subtract(this.getPositionVector()).normalize().scale(1.25D);
			vAway = vAway.add(0, vAway.y * 0.1D, 0);
			entityIn.motionX = vAway.x * 0.75;
			entityIn.motionY = Math.max(Math.abs(vAway.y), 0.6D);
			entityIn.motionZ = vAway.z * 0.75;
			entityIn.velocityChanged = true;
		}
	}

	private boolean isEntityAffected(Entity ent) {
		if (ent instanceof EntityWalkerTornado) {
			return false;
		}
		if (this.getOwnerID() != null) {
			if (ent.getPersistentID().equals(this.getOwnerID())) {
				return false;
			}
			CQRFaction faction = FactionRegistry.instance().getFactionOf(this.owner);
			if (faction != null) {
				return !faction.isAlly(ent);
			}
		}
		return true;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		if (this.getOwnerID() != null) {
			compound.setTag("summoner", NBTUtil.createUUIDTag(this.getOwnerID()));
		}
		compound.setDouble("vX", this.velocity.x);
		compound.setDouble("vY", this.velocity.y);
		compound.setDouble("vZ", this.velocity.z);
		compound.setInteger("ticksExisted", this.ticksExisted);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("summoner")) {
			this.setOwner(NBTUtil.getUUIDFromTag(compound.getCompoundTag("summoner")));
		}
		double x = compound.getDouble("vX");
		double y = compound.getDouble("vY");
		double z = compound.getDouble("vZ");
		this.velocity = new Vec3d(x, y, z);
		this.ticksExisted = compound.getInteger("ticksExisted");
	}

	public void setVelocity(Vec3d v) {
		this.velocity = v;
	}

	public void setOwner(UUID ownerID) {
		this.dataManager.set(OWNER_ID, ownerID.toString());
	}

	public UUID getOwnerID() {
		if (this.dataManager.get(OWNER_ID) != null && !this.dataManager.get(OWNER_ID).isEmpty()) {
			return UUID.fromString(this.dataManager.get(OWNER_ID));
		}
		return null;
	}

	public void setColor(int value) {
		this.dataManager.set(COLOR, value);
	}

	private Integer getColor() {
		return this.dataManager.get(COLOR);
	}

}
