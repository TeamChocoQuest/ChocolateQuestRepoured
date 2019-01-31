package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.init.base.ItemBase;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemStaffFire extends ItemBase
{
	public ItemStaffFire(String name) 
	{
		super(name);
		setMaxDamage(2048);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

	@Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
		boolean flag = super.hitEntity(stack, target, attacker);
		
		if(flag && itemRand.nextInt(5) == 0)
		{
			if(target.getRidingEntity() != null)
			{
				target.dismountRidingEntity();
			}
		}
		return flag;
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		playerIn.getCooldownTracker().setCooldown(stack.getItem(), 30);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
		if(getMaxItemUseDuration(stack) - timeLeft >= 30)
		{
			shootFromEntity(entityLiving);
			stack.damageItem(1, entityLiving);
		}
    }
	
	public void shootFromEntity(EntityLivingBase shooter)
	{
		World world = shooter.world;
		world.playSound(shooter.posX, shooter.posY, shooter.posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.MASTER, 4.0F, (1.0F + (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F) * 0.7F, false);
		   
		float x = (float)-Math.sin(Math.toRadians(shooter.rotationYaw));
		float z = (float)Math.cos(Math.toRadians(shooter.rotationYaw));
		float y = (float)-Math.sin(Math.toRadians(shooter.rotationPitch));
		x *= 1.0F - Math.abs(y);
		z *= 1.0F - Math.abs(y);
		 
		for(int i = 0; i < 50; i++)
		{
			double flameRandomMotion = itemRand.nextDouble() + 0.2D;
			float height = shooter.height;
			world.spawnParticle(EnumParticleTypes.FLAME, shooter.posX, shooter.posY + height, shooter.posZ, (x + (itemRand.nextDouble() - 0.5D) / 3.0D) * flameRandomMotion, (y + (itemRand.nextDouble() - 0.5D) / 3.0D) * flameRandomMotion, (z + (itemRand.nextDouble() - 0.5D) / 3.0D) * flameRandomMotion);
		}
 
		if(!world.isRemote)
		{
			int dist = 15;
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(shooter, shooter.getEntityBoundingBox().grow(shooter.getLookVec().x * dist, shooter.getLookVec().y * dist, shooter.getLookVec().z * dist).expand(1.0D, 1.0D, 1.0D));
	 
			for(Entity e : list)
			{
				if(e instanceof EntityLivingBase)
				{
					double rotDiff = Math.abs(getAngleBetweenEntities(shooter, e));
					double rot = rotDiff - Math.abs(MathHelper.wrapDegrees(shooter.rotationYaw));
					rot = Math.abs(rot);
				
					if(rot < 10.0D)
					{
						if(shooter.canEntityBeSeen(e)) 
						{
							e.setFire(6);
							e.attackEntityFrom(DamageSource.IN_FIRE, 4.0F);
						}
					}
				}
			}
		}
	}
	
	public double getAngleBetweenEntities(Entity attacker, Entity target)
	{
		double d = attacker.posX - target.posX;
		double d2 = attacker.posZ - target.posZ;
		double angle = Math.atan2(d, d2);
		angle = angle * 180.0D / 3.141592D;
	     
		angle = -MathHelper.wrapDegrees(angle - 180.0D);
	     
		return angle;
	}
}