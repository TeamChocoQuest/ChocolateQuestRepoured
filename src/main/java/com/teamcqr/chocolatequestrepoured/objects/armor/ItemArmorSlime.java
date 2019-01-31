package com.teamcqr.chocolatequestrepoured.objects.armor;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.init.base.ArmorBase;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorSlime extends ArmorBase
{
	private AttributeModifier health;
	private AttributeModifier knockBack;
	
	public ItemArmorSlime(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) 
	{
		super(name, materialIn, renderIndexIn, equipmentSlotIn);
		
		this.health = new AttributeModifier("SlimeHealthModifier", 2D, 0);
		this.knockBack = new AttributeModifier("SlimeKnockbackModifier", -0.25D, 0);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		
		if((slot == EntityEquipmentSlot.FEET && stack.getItem() == ModItems.BOOTS_SLIME) || (slot == EntityEquipmentSlot.CHEST && stack.getItem() == ModItems.CHESTPLATE_SLIME) || (slot == EntityEquipmentSlot.LEGS && stack.getItem() == ModItems.LEGGINGS_SLIME) || (slot == EntityEquipmentSlot.HEAD && stack.getItem() == ModItems.HELMET_SLIME))
		{
			multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), this.health);
			multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), this.knockBack);
		}
		return multimap;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.slime_armor.name"));
		}
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
		if(toRepair.getItem() == ModItems.HELMET_SLIME || toRepair.getItem() == ModItems.CHESTPLATE_SLIME || toRepair.getItem() == ModItems.LEGGINGS_SLIME || toRepair.getItem() == ModItems.BOOTS_SLIME)
		{
			if(repair.getItem() == ModItems.SCALE_SLIME)
			{
				return true;
			}
		}
		return false;
    }
}