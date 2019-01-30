package com.tiviacz.chocolatequestrepoured.objects.entity.projectiles;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class ProjectileBullet extends EntityThrowable implements IEntityAdditionalSpawnData
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
    	this.setNoGravity(true);
    }
    
    @Override
	public void onUpdate()
	{
		motionX *= 1.05D;
		motionZ *= 1.05D;
		
		if(getThrower() != null && getThrower().isDead)
		{
			setDead();
		}
		
		else
		{
			if(ticksExisted++ > 300)
			{
				setDead();
			}
			
			this.onUpdateInAir();
			super.onUpdate();
		}
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
					float damage = 5F;
					
					if(result.entityHit == shooter)
					{
						return;
					}
					
					if(type == 1)
					{
						damage += damage * 0.5F;
						entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, entity), damage);
					}
					if(type == 2)
					{
						damage += damage * 0.75F;
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
			
			if(result.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				IBlockState state = world.getBlockState(result.getBlockPos());
					
				if(!state.getBlock().isPassable(world, result.getBlockPos()))
				{
					setDead();
				}
			} 
		}
	}
	
	private void onUpdateInAir()
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