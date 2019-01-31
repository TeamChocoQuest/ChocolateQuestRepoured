package com.teamcqr.chocolatequestrepoured.objects.armor;

import com.google.common.collect.Multimap;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.init.base.ArmorBase;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
	
public class ItemArmorHeavy extends ArmorBase
{
	private Item item;
	private AttributeModifier movementSpeed;
	private AttributeModifier knockbackResistance;
	
	public ItemArmorHeavy(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, Item repairItem) 
	{
		super(name, materialIn, renderIndexIn, equipmentSlotIn);
		
		this.movementSpeed = new AttributeModifier("HeavySpeedModifier", -0.06D, 2);
		this.knockbackResistance = new AttributeModifier("HeavyKnockbackModifier", 0.25D, 0);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		
		if((slot == EntityEquipmentSlot.HEAD && (stack.getItem() == ModItems.HELMET_HEAVY_DIAMOND || stack.getItem() == ModItems.HELMET_HEAVY_IRON)) 
				|| (slot == EntityEquipmentSlot.CHEST && (stack.getItem() == ModItems.CHESTPLATE_HEAVY_DIAMOND || stack.getItem() == ModItems.CHESTPLATE_HEAVY_IRON)) 
				|| (slot == EntityEquipmentSlot.LEGS && (stack.getItem() == ModItems.LEGGINGS_HEAVY_DIAMOND || stack.getItem() == ModItems.LEGGINGS_HEAVY_IRON)) 
				|| (slot == EntityEquipmentSlot.FEET && (stack.getItem() == ModItems.BOOTS_HEAVY_DIAMOND || stack.getItem() == ModItems.BOOTS_HEAVY_IRON)))
		{
			multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), this.knockbackResistance);
			multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), this.movementSpeed);
		}
		return multimap;
    }
	
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
	{
		if(!world.isRemote)
		{
			if(!player.onGround)
			{
				player.jumpMovementFactor = ((float)Math.max(0.015D, player.jumpMovementFactor - 0.01F));
			}
		}
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
		if(toRepair.getItem() == ModItems.HELMET_HEAVY_IRON || toRepair.getItem() == ModItems.CHESTPLATE_HEAVY_IRON || toRepair.getItem() == ModItems.LEGGINGS_HEAVY_IRON || toRepair.getItem() == ModItems.BOOTS_HEAVY_IRON)
		{
			if(repair.getItem() == Items.IRON_INGOT)
			{
				return true;
			}
		}
		
		if(toRepair.getItem() == ModItems.HELMET_HEAVY_DIAMOND || toRepair.getItem() == ModItems.CHESTPLATE_HEAVY_DIAMOND || toRepair.getItem() == ModItems.LEGGINGS_HEAVY_DIAMOND || toRepair.getItem() == ModItems.BOOTS_HEAVY_DIAMOND)
		{
			if(repair.getItem() == Items.DIAMOND)
			{
				return true;
			}
		}
		return false;
    }
}