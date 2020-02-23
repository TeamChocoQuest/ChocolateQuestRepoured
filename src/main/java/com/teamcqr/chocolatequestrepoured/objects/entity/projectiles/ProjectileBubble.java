package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityBubble;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ProjectileBubble extends ProjectileBase {
	
	private EntityLivingBase shooter;
	protected float damage;

	public ProjectileBubble(World worldIn) {
		super(worldIn);
	}

	public ProjectileBubble(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public ProjectileBubble(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
		this.shooter = shooter;
		this.damage = 1F;
		this.isImmuneToFire = true;
	}
	
	@Override
	public void applyEntityCollision(Entity entityHit) {
		if (entityHit == this.shooter) {
			return;
		}

		entityHit.attackEntityFrom(DamageSource.MAGIC, this.damage);
		float pitch = (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F;
		this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_SWIM, SoundCategory.PLAYERS, 4, pitch, true);
		
		EntityBubble bubbles = new EntityBubble(world);
		bubbles.moveToBlockPosAndAngles(entityHit.getPosition().add(0,0.25,0), entityHit.rotationYaw, entityHit.rotationPitch);
		world.spawnEntity(bubbles);
		
		entityHit.startRiding(bubbles, true);
		
		this.setDead();
	}
	
	@Override
	protected void onUpdateInAir() {
		if (this.world.isRemote) {
			if (this.ticksExisted % 5 == 0) {
				this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, this.posY + 0.1D, this.posZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}

}
