package com.teamcqr.chocolatequestrepoured.objects.items.swords;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.objects.base.SwordBase;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSwordMoonlightSunshine extends SwordBase
{
	private boolean isMoonlight = false;
	
	public ItemSwordMoonlightSunshine(String name, ToolMaterial material, boolean isMoonlight) 
	{
		super(name, material);
		
		this.isMoonlight = isMoonlight;
	}
	
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if(this.isMoonlight)
		{
			if(!target.world.isDaytime())
			{
				if(target instanceof Entity)
				{
					((Entity)target).attackEntityFrom(DamageSource.GENERIC, 3.0F);
				}
			}
		}
		if(!this.isMoonlight)
		{
			if(target.world.isDaytime())
			{
				if(target instanceof Entity)
				{
					((Entity)target).attackEntityFrom(DamageSource.GENERIC, 3.0F);
				}
			}
		}
        return super.hitEntity(stack, target, attacker);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) && this.isMoonlight)
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.moonlight.name"));
		}	
		
		if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) && !this.isMoonlight)
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.sunshine.name"));
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
		
		if(this.isMoonlight)
		{
			tooltip.add(TextFormatting.BLUE + "+3 " + I18n.format("description.attack_damage_at_night.name"));
		}
		
		if(!this.isMoonlight)
		{
			tooltip.add(TextFormatting.BLUE + "+3 " + I18n.format("description.attack_damage_at_day.name"));
		} 
    }
}