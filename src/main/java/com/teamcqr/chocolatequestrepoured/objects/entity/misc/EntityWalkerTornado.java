package com.teamcqr.chocolatequestrepoured.objects.entity.misc;

import java.util.ArrayList;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;
import com.teamcqr.chocolatequestrepoured.objects.entity.particle.EntityParticle;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWalkerTornado extends EntityLiving {
	
	protected ArrayList<EntityParticle> particles = new ArrayList<>();
	protected final int PARTICLE_COUNT = 2;
	protected final int MAX_LIVING_TICKS = 100;
	protected Vec3d velocity = new Vec3d(0,0,0);
	protected Entity owner = null;
	
	public static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityWalkerTornado.class, DataSerializers.VARINT);

	public EntityWalkerTornado(World worldIn) {
		super(worldIn);
		this.setSize(1.5F, 2.5F);
		this.setNoGravity(true);
		this.noClip = true;
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(COLOR, 15);
	}
	
	@Override
	public void onLivingUpdate() {
		if(this.ticksExisted >= MAX_LIVING_TICKS) {
			setDead();
		}
		if(world.isRemote) {
			updateParticles();
		} else {
			this.motionX = this.velocity.x;
			this.motionY = this.velocity.y;
			this.motionZ = this.velocity.z;
			this.velocityChanged = true;
		}
	}
	
	public void setVelocity(Vec3d v) {
		this.velocity = v;
	}
	
	public void setOwner(Entity owner) {
		this.owner = owner;
	}
	
	//Particle code taken from aether legacy's whirlwind
	@SideOnly(Side.CLIENT)
    public void updateParticles() {
       
		final Integer color = this.getColor();
        for (int k = 0; k < 2; ++k) {
            final double d1 = (float)this.posX + this.rand.nextFloat() * 0.25f;
            final double d2 = (float)this.posY + this.height + 0.125f;
            final double d3 = (float)this.posZ + this.rand.nextFloat() * 0.25f;
            final float f = this.rand.nextFloat() * 360.0f;
            final EntityParticle particle = new EntityParticle(this.world, -Math.sin(0.01745329f * f) * 0.75, d2 - 0.25, Math.cos(0.01745329f * f) * 0.75, d1, 0.125, d3);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect((Particle)particle);
            this.particles.add(particle);
            particle.setRBGColorF((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f);
            particle.setPosition(this.posX, this.posY, this.posZ);
        }

        if (this.particles.size() > 0) {
            for (int i2 = 0; i2 < this.particles.size(); ++i2) {
                final EntityParticle particle3 = this.particles.get(i2);
                if (!particle3.isAlive()) {
                    this.particles.remove(particle3);
                }
                else {
                    final double d7 = particle3.getX();
                    final double d8 = particle3.getBoundingBox().minY;
                    final double d9 = particle3.getZ();
                    final double d10 = this.getDistanceToParticle(particle3);
                    final double d11 = d8 - this.posY;
                    particle3.setMotionY(0.11500000208616257);
                    double d12 = Math.atan2(this.posX - d7, this.posZ - d9) / 0.01745329424738884;
                    d12 += 160.0;
                    particle3.setMotionX(-Math.cos(0.01745329424738884 * d12) * (d10 * 2.5 - d11) * 0.10000000149011612);
                    particle3.setMotionZ(Math.sin(0.01745329424738884 * d12) * (d10 * 2.5 - d11) * 0.10000000149011612);
                }
            }
        }
    }

	public void setColor(int value) {
		this.dataManager.set(COLOR, value);
	}
	
	@Override
	protected void collideWithEntity(Entity entityIn) {
		if(!world.isRemote && isEntityAffected(entityIn)) {
			//TODO: Fly away :D
			Vec3d vAway = entityIn.getPositionVector().subtract(getPositionVector()).normalize().scale(1.25D);
			vAway = vAway.addVector(0, vAway.y * 0.5D, 0);
			entityIn.motionX = vAway.x;
			entityIn.motionY = Math.max(Math.abs(vAway.y), 2.0D);
			entityIn.motionZ = vAway.z;
			entityIn.velocityChanged = true;
		}
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	
	private boolean isEntityAffected(Entity ent) {
		if(owner != null) {
			if(ent == owner) {
				return false;
			}
			CQRFaction faction = FactionRegistry.instance().getFactionOf(owner);
			if(faction != null) {
				return faction.isAlly(ent);
			}
			return true;
		}
		return true;
	}
	
	private Integer getColor() {
		return this.dataManager.get(COLOR);
	}

	@SideOnly(Side.CLIENT)
	public float getDistanceToParticle(final EntityParticle particle) {
	    final float f = (float)(this.posX - particle.getX());
	    final float f2 = (float)(this.posY - particle.getY());
	    final float f3 = (float)(this.posZ - particle.getZ());
	    return MathHelper.sqrt(f * f + f2 * f2 + f3 * f3);
	}

}

