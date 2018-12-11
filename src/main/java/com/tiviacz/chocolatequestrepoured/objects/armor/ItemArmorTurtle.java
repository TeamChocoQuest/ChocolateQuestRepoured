package com.tiviacz.chocolatequestrepoured.objects.armor;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.tiviacz.chocolatequestrepoured.init.ModItems;
import com.tiviacz.chocolatequestrepoured.init.base.ArmorBase;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorTurtle extends ArmorBase
{
	private int ticksFromLastHeal = 0;
	private String CD = "Cooldown";
	private String ON = "Enabled";
	private int Cooldown = 3600;
	
	public ItemArmorTurtle(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) 
	{
		super(name, materialIn, renderIndexIn, equipmentSlotIn);
	}

	@Override
	public void onArmorTick(World worldIn, EntityPlayer player, ItemStack stack)
	{	
		if(isFullSet(player, stack))
		{	
			ticksFromLastHeal++;
			
			if(ticksFromLastHeal == 1200)
			{
				heal(player, 1.0F);
				
				ticksFromLastHeal = 0;
				
				if(worldIn.isRemote)
				{
					worldIn.spawnParticle(EnumParticleTypes.HEART, player.posX + itemRand.nextDouble() - 0.5D, player.posY + itemRand.nextDouble(), player.posZ + itemRand.nextDouble() - 0.5D, 0.0D, 1.0D, 0.0D);
				}
			}
		}
		
		if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.HELMET_TURTLE && stack.getItem() == ModItems.HELMET_TURTLE)
		{
			NBTTagCompound helmet = stack.getTagCompound();
				
			if(helmet == null)
			{
				helmet = new NBTTagCompound();
				stack.setTagCompound(helmet);
			}
				
			if(!helmet.hasKey(CD))
			{
				helmet.setInteger(CD, 0);
			}
				
			if(!helmet.hasKey(ON))
			{
				helmet.setBoolean(ON, false);
			}
			
			if(helmet.hasKey(CD) && helmet.hasKey(ON))
			{
				if(helmet.getInteger(CD) > 0)
				{
					if(!worldIn.isRemote)
					{
						helmet.setInteger(CD, helmet.getInteger(CD) - 1);
					}
				}
					
				if(player.getHealth() < 2.0F && helmet.getInteger(CD) == 0 && isFullSet(player, stack) && isFullSetReady(player))
				{
					helmet.setBoolean(ON, true);
				}
					
				if(helmet.getBoolean(ON) && isFullSet(player, stack) && isFullSetReady(player))
				{
					if(player.getHealth() < player.getMaxHealth()) 
					{
						heal(player, 1.0F);
								
						if(worldIn.isRemote)
						{
							worldIn.spawnParticle(EnumParticleTypes.HEART, player.posX + itemRand.nextDouble() - 0.5D, player.posY + itemRand.nextDouble(), player.posZ + itemRand.nextDouble() - 0.5D, 0.0D, 1.0D, 0.0D);
						}
					}
					else
					{
						helmet.setBoolean(ON, false);
						helmet.setInteger(CD, Cooldown);
						applyCooldown(player);
					}
				}
			}
		}
			
		if(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.CHESTPLATE_TURTLE && stack.getItem() == ModItems.CHESTPLATE_TURTLE)
		{
			NBTTagCompound chestplate = stack.getTagCompound();
				
			if(chestplate == null)
			{
				chestplate = new NBTTagCompound();
				stack.setTagCompound(chestplate);
			}
				
			if(!chestplate.hasKey(CD))
			{
				chestplate.setInteger(CD, 0);
			}
				
			if(!chestplate.hasKey(ON))
			{
				chestplate.setBoolean(ON, false);
			}
				
			if(chestplate.hasKey(CD) && chestplate.hasKey(ON))
			{
				if(chestplate.getInteger(CD) > 0)
				{
					if(!worldIn.isRemote)
					{
						chestplate.setInteger(CD, chestplate.getInteger(CD) - 1);
					}
				}
					
				if(player.getHealth() < 2.0F && chestplate.getInteger(CD) == 0 && isFullSet(player, stack) && isFullSetReady(player))
				{
					chestplate.setBoolean(ON, true);
				}
					
				if(chestplate.getBoolean(ON) && isFullSet(player, stack) && isFullSetReady(player))
				{
					if(player.getHealth() < player.getMaxHealth()) 
					{
						heal(player, 1.0F);
								
						if(worldIn.isRemote)
						{
							worldIn.spawnParticle(EnumParticleTypes.HEART, player.posX + itemRand.nextDouble() - 0.5D, player.posY + itemRand.nextDouble(), player.posZ + itemRand.nextDouble() - 0.5D, 0.0D, 1.0D, 0.0D);
						}
					}
					else
					{
						chestplate.setBoolean(ON, false);
						chestplate.setInteger(CD, Cooldown);
						applyCooldown(player);
					}
				}
			}
		}
			
		if(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.LEGGINGS_TURTLE && stack.getItem() == ModItems.LEGGINGS_TURTLE)
		{
			NBTTagCompound leggings = stack.getTagCompound();
				
			if(leggings == null)
			{
				leggings = new NBTTagCompound();
				stack.setTagCompound(leggings);
			}
				
			if(!leggings.hasKey(CD))
			{
				leggings.setInteger(CD, 0);
			}
				
			if(!leggings.hasKey(ON))
			{
				leggings.setBoolean(ON, false);
			}
			
			if(leggings.hasKey(CD) && leggings.hasKey(ON))
			{
				if(leggings.getInteger(CD) > 0)
				{
					if(!worldIn.isRemote)
					{
						leggings.setInteger(CD, leggings.getInteger(CD) - 1);
					}
				}
					
				if(player.getHealth() < 2.0F && leggings.getInteger(CD) == 0 && isFullSet(player, stack) && isFullSetReady(player))
				{
					leggings.setBoolean(ON, true);
				}
					
				if(leggings.getBoolean(ON) && isFullSet(player, stack) && isFullSetReady(player))
				{
					if(player.getHealth() < player.getMaxHealth()) 
					{
						heal(player, 1.0F);
							
						if(worldIn.isRemote)
						{
							worldIn.spawnParticle(EnumParticleTypes.HEART, player.posX + itemRand.nextDouble() - 0.5D, player.posY + itemRand.nextDouble(), player.posZ + itemRand.nextDouble() - 0.5D, 0.0D, 1.0D, 0.0D);
						}
					}
					else
					{
						leggings.setBoolean(ON, false);
						leggings.setInteger(CD, Cooldown);
						applyCooldown(player);
					}
				}
			}
		}
			
		if(player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.BOOTS_TURTLE && stack.getItem() == ModItems.BOOTS_TURTLE)
		{
			NBTTagCompound boots = stack.getTagCompound();
				
			if(boots == null)
			{
				boots = new NBTTagCompound();
				stack.setTagCompound(boots);
			}
				
			if(!boots.hasKey(CD))
			{
				boots.setInteger(CD, 0);
			}
				
			if(!boots.hasKey(ON))
			{
				boots.setBoolean(ON, false);
			}
			
			if(boots.hasKey(CD) && boots.hasKey(ON))
			{
				if(boots.getInteger(CD) > 0)
				{
					if(!worldIn.isRemote)
					{
						boots.setInteger(CD, boots.getInteger(CD) - 1);
					}
				}
					
				if(player.getHealth() < 2.0F && boots.getInteger(CD) == 0 && isFullSet(player, stack) && isFullSetReady(player))
				{
					boots.setBoolean(ON, true);
				}
					
				if(boots.getBoolean(ON) && isFullSet(player, stack) && isFullSetReady(player))
				{
					if(player.getHealth() < player.getMaxHealth()) 
					{
						heal(player, 1.0F);
								
						if(worldIn.isRemote)
						{
							worldIn.spawnParticle(EnumParticleTypes.HEART, player.posX + itemRand.nextDouble() - 0.5D, player.posY + itemRand.nextDouble(), player.posZ + itemRand.nextDouble() - 0.5D, 0.0D, 1.0D, 0.0D);
						}
					}
					else
					{
						boots.setBoolean(ON, false);
						boots.setInteger(CD, Cooldown);
						applyCooldown(player);
					}
				}
			}
		}
	}
	
	private void applyCooldown(EntityPlayer player)
	{
		if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.HELMET_TURTLE && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.CHESTPLATE_TURTLE &&
		player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.LEGGINGS_TURTLE && player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.BOOTS_TURTLE)
		{
			NBTTagCompound helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getTagCompound();
			NBTTagCompound chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getTagCompound();
			NBTTagCompound leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getTagCompound();
			NBTTagCompound boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getTagCompound();
			
			if(helmet.hasKey(CD) || chestplate.hasKey(CD) || leggings.hasKey(CD) || boots.hasKey(CD))
			{
				if(helmet.getInteger(CD) == 0)
				{
					helmet.setInteger(CD, Cooldown);
				}
				
				if(chestplate.getInteger(CD) == 0)
				{
					chestplate.setInteger(CD, Cooldown);
				}
				
				if(leggings.getInteger(CD) == 0)
				{
					leggings.setInteger(CD, Cooldown);
				}
				
				if(boots.getInteger(CD) == 0)
				{
					boots.setInteger(CD, Cooldown);
				}
			}
		}
	}
	
	private boolean isFullSetReady(EntityPlayer player)
	{
		if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.HELMET_TURTLE && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.CHESTPLATE_TURTLE &&
		player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.LEGGINGS_TURTLE && player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.BOOTS_TURTLE)
		{
			NBTTagCompound helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getTagCompound();
			NBTTagCompound chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getTagCompound();
			NBTTagCompound leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getTagCompound();
			NBTTagCompound boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getTagCompound();
			
			if(helmet.getInteger(CD) > 0 || chestplate.getInteger(CD) > 0 || leggings.getInteger(CD) > 0 || boots.getInteger(CD) > 0)
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(stack.hasTagCompound())
		{
			NBTTagCompound tag = stack.getTagCompound();
					
			if(tag.hasKey(CD))
			{
				if(tag.getInteger(CD) > 0)
				{
					tooltip.add(TextFormatting.RED + I18n.format("description.turtle_armor_charging.name") + convertCooldown(stack));
				}
			}  
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.turtle_armor.name"));
		}
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
	
	private void heal(EntityPlayer playerIn, float amount)
	{
		playerIn.setHealth(playerIn.getHealth() + amount);
	}
	
	private String convertCooldown(ItemStack stack)
	{
		NBTTagCompound tag = stack.getTagCompound();
			
		if(tag.hasKey(CD))
		{
			int getSeconds = tag.getInteger(CD) / 20;
			int minutes = (int)Math.floor(getSeconds / 60);
			int seconds = getSeconds % 60;
			
			if(seconds < 10)
			{
				return minutes + ":" + "0" + seconds;
			}
				
			return minutes + ":" + seconds;
		}
		return "Error!";
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
		if(toRepair.getItem() == ModItems.HELMET_TURTLE || toRepair.getItem() == ModItems.CHESTPLATE_TURTLE || toRepair.getItem() == ModItems.LEGGINGS_TURTLE || toRepair.getItem() == ModItems.BOOTS_TURTLE)
		{
			if(repair.getItem() == ModItems.SCALE_TURTLE)
			{
				return true;
			}
		}
		return false;
    }
}