package com.tiviacz.chocolatequestrepoured.objects.armor;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;
import com.tiviacz.chocolatequestrepoured.init.ModItems;
import com.tiviacz.chocolatequestrepoured.init.base.ArmorBase;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorBull extends ArmorBase
{
	private AttributeModifier strength;
	
	public ItemArmorBull(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) 
	{
		super(name, materialIn, renderIndexIn, equipmentSlotIn);
		
		this.strength = new AttributeModifier("BullArmorModifier", 1.5D, 0);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		
		if((slot == EntityEquipmentSlot.FEET && stack.getItem() == ModItems.BOOTS_BULL) || (slot == EntityEquipmentSlot.CHEST && stack.getItem() == ModItems.CHESTPLATE_BULL) || (slot == EntityEquipmentSlot.LEGS && stack.getItem() == ModItems.LEGGINGS_BULL) || (slot == EntityEquipmentSlot.HEAD && stack.getItem() == ModItems.HELMET_BULL))
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), this.strength);
		}
		return multimap;
    }
	
	@Override
	public void onArmorTick(World worldIn, EntityPlayer player, ItemStack stack)
	{	
		if(isFullSet(player, stack))
		{
			if(player.isSprinting())
			{
				player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 0, 1, false, false));
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.bull_armor.name"));
		}
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
		if(toRepair.getItem() == ModItems.HELMET_BULL || toRepair.getItem() == ModItems.CHESTPLATE_BULL || toRepair.getItem() == ModItems.LEGGINGS_BULL || toRepair.getItem() == ModItems.BOOTS_BULL)
		{
			if(repair.getItem() == ModItems.LEATHER_BULL)
			{
				return true;
			}
		}
		return false;
    }
}