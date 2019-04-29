package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.objects.base.ItemBase;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemGoldenFeather extends ItemBase
{
	public ItemGoldenFeather(String name) 
	{
		super(name);

		setMaxStackSize(1);
		setMaxDamage(385);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		if(isSelected && entityIn instanceof EntityLivingBase)
		{
			if(entityIn.fallDistance > 1.5F)
			{
				stack.damageItem(1, (EntityLivingBase)entityIn);
				entityIn.fallDistance = 0.0F;
				
				for(int i = 0; i < 3; i++)
				{
					worldIn.spawnParticle(EnumParticleTypes.CLOUD, entityIn.posX, entityIn.posY, entityIn.posZ, (itemRand.nextFloat() - 0.5F) / 2.0F, -0.5D, (itemRand.nextFloat() - 0.5F) / 2.0F);
				}
			}
		}
	}
}