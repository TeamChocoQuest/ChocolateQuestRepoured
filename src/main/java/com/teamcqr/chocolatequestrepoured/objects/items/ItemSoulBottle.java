package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSoulBottle extends Item
{
	private String EntityIn = "EntityIn";
	
	public ItemSoulBottle() 
	{
		setMaxStackSize(64);
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		NBTTagCompound bottle = stack.getTagCompound();
		
		if(bottle == null)
		{
			bottle = new NBTTagCompound();
			stack.setTagCompound(bottle);
		}
		
		NBTTagCompound entityTag = new NBTTagCompound();
		
		if(bottle.getTag(EntityIn) == null && entity.writeToNBTOptional(entityTag))
		{
			bottle.setTag(EntityIn, entityTag);
			entity.setDead();
			this.spawnAdditions(entity.world, entity);
		}
		
		return true;
    }
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound bottle = stack.getTagCompound();

		if(bottle != null)
		{
			if(bottle.getTag(EntityIn) != null)
			{
				NBTTagCompound entityTag = (NBTTagCompound)bottle.getTag(EntityIn);
				Entity entity = this.createEntityFromNBT(entityTag, worldIn, pos.getX(), pos.getY(), pos.getZ());
				entity.setUniqueId(MathHelper.getRandomUUID(new Random()));
				
				if(!worldIn.isRemote)
				{	
					if(entity != null)
					{
						worldIn.spawnEntity(entity);
					}
	
					if(!(player.isCreative() || player.isSpectator()) || (player.isCreative() && player.isSneaking())) 
					{
						bottle.removeTag(EntityIn);
					}
				}
				
		    	this.spawnAdditions(worldIn, entity);
			}
		}
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
	
	private Entity createEntityFromNBT(NBTTagCompound tag, World worldIn, int x, int y, int z)
	{
		Entity entity = EntityList.createEntityFromNBT(tag, worldIn);
		entity.readFromNBT(tag);
			
		if(entity != null)
		{
			entity.posX = (x + 0.5D);
			entity.posY = (y + 1.0D);
			entity.posZ = (z + 0.5D);
			entity.setPosition(entity.posX, entity.posY, entity.posZ);
		}
		return entity;
	}
	
	private void spawnAdditions(World worldIn, Entity entity)
	{
		for(int x = 0; x < 5; x++)
    	{
    		worldIn.spawnParticle(EnumParticleTypes.CLOUD, entity.posX + itemRand.nextFloat() - 0.5D, entity.posY + 0.5D + itemRand.nextFloat(), entity.posZ + itemRand.nextFloat() - 0.5D, 0, 0, 0);
    	}
		
		worldIn.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.MASTER, 4.0F, (1.0F + (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F) * 0.7F, false);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.soul_bottle.name"));
		}
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
		
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().hasKey(EntityIn))
			{
				NBTTagCompound tag = (NBTTagCompound)stack.getTagCompound().getTag(EntityIn);
				Entity entity = EntityList.createEntityFromNBT(tag, worldIn);
				entity.readFromNBT(tag);
				
				tooltip.add(TextFormatting.BLUE + I18n.format("description.contains.name") + " " + this.getName(entity));
			}
			else
			{
				tooltip.add(TextFormatting.BLUE + I18n.format("description.contains.name") + " " + I18n.format("description.empty.name"));
			}
		}
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.contains.name") + " " + I18n.format("description.empty.name"));
		}
    }
	
	private String getName(Entity entity)
    {
		String s = EntityList.getEntityString(entity);

        if (s == null)
        {
            s = "generic";
        }
        
        if(entity.hasCustomName())
        {
            return I18n.format("entity." + s + ".name") + TextFormatting.BLUE + " - " + entity.getCustomNameTag();
        }
        else
        {

            return I18n.format("entity." + s + ".name");
        }
    }
}
