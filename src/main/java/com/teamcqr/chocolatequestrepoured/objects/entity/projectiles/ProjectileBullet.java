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

public class ProjectileBullet extends ProjectileBase implements IEntityAdditionalSpawnData
{
	private int type;
	private EntityLivingBase shooter;
	
	public ProjectileBullet(World worldIn) 
	{
		super(worldIn);
	}
	
	public ProjectileBullet(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public ProjectileBullet(World worldIn, EntityLivingBase shooter, int type)
    {
    	super(worldIn, shooter);
    	this.shooter = shooter;
    	this.isImmuneToFire = true;
    	this.type = type;
    }
    
    public int getType()
    {
    	return type;
    }

	@Override
	protected void onImpact(RayTraceResult result) 
	{
		if(!world.isRemote)
		{
			if(result.typeOfHit == RayTraceResult.Type.ENTITY)
			{
				if(result.entityHit instanceof EntityLivingBase)
				{
					EntityLivingBase entity = (EntityLivingBase)result.entityHit;
					
					float damage = 5.0F;
					
					if(shooter.getHeldItemMainhand().getItem() instanceof ItemMusket || shooter.getHeldItemOffhand().getItem() instanceof ItemMusket
							|| shooter.getHeldItemMainhand().getItem() instanceof ItemMusketKnife || shooter.getHeldItemOffhand().getItem() instanceof ItemMusketKnife)
					{
						damage += 2.5F;
					}
					
					if(result.entityHit == shooter)
					{
						return;
					}
					
					if(type == 1)
					{
						damage += 2.5F;
						entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, entity), damage);
					}
					if(type == 2)
					{
						damage += 3.75;
						entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, entity), damage);
					}
					if(type == 3)
					{
						damage += damage;
						entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, entity), damage);
					}
					if(type == 4)
					{
						damage += damage;
						entity.setFire(3);
						entity.attackEntityFrom(DamageSource.IN_FIRE, damage);
					}
					setDead();
				}
			}

			super.onImpact(result);
		}
	}
	
	@Override
	protected void onUpdateInAir()
	{
		if(world.isRemote)
		{
			if(ticksExisted < 10)
			{
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY + 0.1D, posZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) 
	{
		buffer.writeInt(type);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) 
	{
		int t = additionalData.readInt();
		type = t;
	}
}