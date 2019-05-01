package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectilePoisonSpell extends ProjectileSpiderBall
{
	private EntityLivingBase shooter;
	
	public ProjectilePoisonSpell(World worldIn) 
	{
		super(worldIn);
	}
	
	public ProjectilePoisonSpell(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public ProjectilePoisonSpell(World worldIn, EntityLivingBase shooter)
    {
    	super(worldIn, shooter);
    	this.shooter = shooter;
    	this.isImmuneToFire = false;
    	this.damage = 5.0F;
    }
    
    @Override
    public boolean hasNoGravity()
    {
        return false;
    }
    
    @Override
	protected void onUpdateInAir() {}
}