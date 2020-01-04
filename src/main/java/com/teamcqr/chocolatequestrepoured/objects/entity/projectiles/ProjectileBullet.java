package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemMusket;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemMusketKnife;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class ProjectileBullet extends ProjectileBase implements IEntityAdditionalSpawnData {
	private int type;
	private EntityLivingBase shooter;

	public ProjectileBullet(World worldIn) {
		super(worldIn);
	}

	public ProjectileBullet(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public ProjectileBullet(World worldIn, EntityLivingBase shooter, int type) {
		super(worldIn, shooter);
		this.shooter = shooter;
		this.isImmuneToFire = true;
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
				if (result.entityHit instanceof EntityLivingBase) {
					EntityLivingBase entity = (EntityLivingBase) result.entityHit;

					float damage = 5.0F;

					if (this.shooter.getHeldItemMainhand().getItem() instanceof ItemMusket || this.shooter.getHeldItemOffhand().getItem() instanceof ItemMusket || this.shooter.getHeldItemMainhand().getItem() instanceof ItemMusketKnife
							|| this.shooter.getHeldItemOffhand().getItem() instanceof ItemMusketKnife) {
						damage += 2.5F;
					}

					if (result.entityHit == this.shooter) {
						return;
					}

					if (this.type == 1) {
						damage += 2.5F;
						entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, entity), damage);
					}
					if (this.type == 2) {
						damage += 3.75;
						entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, entity), damage);
					}
					if (this.type == 3) {
						damage += damage;
						entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, entity), damage);
					}
					if (this.type == 4) {
						damage += damage;
						entity.setFire(3);
						entity.attackEntityFrom(DamageSource.IN_FIRE, damage);
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
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.1D, this.posZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(this.type);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		int t = additionalData.readInt();
		this.type = t;
	}
}