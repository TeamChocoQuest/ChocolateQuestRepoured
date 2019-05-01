package com.teamcqr.chocolatequestrepoured.objects.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntitySlimePart extends EntitySlime
{
	private EntityLivingBase owner;
	private float healthValue;
	
	public EntitySlimePart(World worldIn) 
	{
		super(worldIn);
		this.fallDistance = -5F;
	}
	
	public EntitySlimePart(World worldIn, EntityLivingBase owner, float size) 
	{
		this(worldIn);
		this.owner = owner;
		this.setSlimeSize(Math.max(1, (int)size / 3), false);
		this.setHealth(1.0F);
		this.experienceValue = 0;
		this.healthValue = size;
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.4D);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(15.0D);
	}
	
	public void onUpdate()
    {
		super.onUpdate();
		
		if(this.ticksExisted > 400)
		{
			this.setDead();
		}
    }
	
	@Override
	public void applyEntityCollision(Entity entityIn)
	{
		if(entityIn == this.owner)
		{
			if(this.ticksExisted > 10) 
			{
				owner.setHealth(owner.getHealth() + 2F);
				setDead();
			}
			else
			{
				owner.setHealth(owner.getHealth() + 1F);
				setDead();
			}
		}
		
		if(entityIn instanceof EntityLivingBase && entityIn.getClass() != getClass()) 
		{
			EntityLivingBase entity = (EntityLivingBase)entityIn;
			
			if(!entity.isOnSameTeam(this))
			{
				entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40, 1, false, false));
			}
			super.applyEntityCollision(entityIn);
		}
	}
	
	@Override
	public boolean canAttackClass(Class <? extends EntityLivingBase > cls)
	{
		return(super.canAttackClass(cls)) && (cls != getClass()) && (cls != EntitySlimePart.class);
	}
	
	@Override
	public void setDead()
	{
		if(!this.world.isRemote && !this.isDead && getHealth() <= 0.0F && getSlimeSize() > 1) 
		{
			int size = MathHelper.floor(this.healthValue * 0.34F);
			
			for(int a = 0; a < 2; a++) 
			{
				EntitySlimePart part = new EntitySlimePart(this.world, this.owner, size);
				part.setPosition(this.posX, this.posY + 1.0D, this.posZ);
				part.motionX = this.rand.nextGaussian();
				part.motionZ = this.rand.nextGaussian();
				this.world.spawnEntity(part);
			}
		}
		this.isDead = true;
	} 
	
	@Override
	public Team getTeam()
	{
		if(this.owner != null)
		{
			return this.owner.getTeam();
		}
		return super.getTeam();
	}
	
	@Override
	protected int getAttackStrength()
	{
		return 0;
	}
}