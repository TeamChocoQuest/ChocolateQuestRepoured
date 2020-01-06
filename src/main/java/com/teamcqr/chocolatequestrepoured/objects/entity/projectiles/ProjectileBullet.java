package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class ProjectileBullet extends ProjectileBase implements IEntityAdditionalSpawnData {

	private int type;

	public ProjectileBullet(World worldIn) {
		super(worldIn);
	}

	public ProjectileBullet(World worldIn, double x, double y, double z, int type) {
		super(worldIn, x, y, z);
		this.type = type;
	}

	public ProjectileBullet(World worldIn, EntityLivingBase shooter, int type) {
		super(worldIn, shooter);
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
				if (result.entityHit == this.thrower) {
					return;
				}

				if (result.entityHit instanceof EntityLivingBase) {
					EntityLivingBase entity = (EntityLivingBase) result.entityHit;

					float damage = 5.0F;

					if (this.type == 1) {
						damage += 2.5F;
						entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.thrower), damage);
					} else if (this.type == 2) {
						damage += 3.75F;
						entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.thrower), damage);
					} else if (this.type == 3) {
						damage += 5.0F;
						entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.thrower), damage);
					} else if (this.type == 4) {
						damage += 5.0F;
						entity.setFire(3);
						entity.attackEntityFrom(new EntityDamageSourceIndirect("onFire", this, this.thrower).setFireDamage(), damage);
					}
					this.setDead();
				}
			}

			super.onImpact(result);
		}
	}

	@Override
	protected void onUpdateInAir() {
		if (this.world.isRemote) {
			if (this.ticksExisted < 10) {
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(this.type);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.type = additionalData.readInt();
	}

}
