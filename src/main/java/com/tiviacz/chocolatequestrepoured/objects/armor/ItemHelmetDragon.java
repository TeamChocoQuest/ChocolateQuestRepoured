package com.tiviacz.chocolatequestrepoured.objects.armor;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;
import com.tiviacz.chocolatequestrepoured.init.ModItems;
import com.tiviacz.chocolatequestrepoured.init.base.ArmorBase;
import com.tiviacz.chocolatequestrepoured.util.Reference;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHelmetDragon extends ArmorBase
{
	private AttributeModifier attackDamage;
	private AttributeModifier health;
	
	public ItemHelmetDragon(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) 
	{
		super(name, materialIn, renderIndexIn, equipmentSlotIn);
		
		this.health = new AttributeModifier("DragonHelmetHealthModifier", 10D, 0);
		this.attackDamage = new AttributeModifier("DragonHelmetDamageModifier", 1D, 2);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		
		if(slot == EntityEquipmentSlot.HEAD && stack.getItem() == ModItems.HELMET_DRAGON)
		{
			multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), this.health);
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), this.attackDamage);
		}
		return multimap;
    }
	
/*	@SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
    {
		if(itemStack != null)
		{
			if(itemStack.getItem() instanceof ItemArmor)
			{
				ModelHelmetDragon model = new ModelHelmetDragon();
				
				model.bipedHead.showModel = armorSlot == EntityEquipmentSlot.HEAD;
                model.bipedHeadwear.showModel = armorSlot == EntityEquipmentSlot.HEAD;
				
				model.isSneak = _default.isSneak;
				model.isRiding = _default.isRiding;
				model.isChild = _default.isChild;
				model.rightArmPose = _default.rightArmPose;
				model.leftArmPose = _default.leftArmPose;
				model.swingProgress = _default.swingProgress;
				
				return model;
			}
		}
		return null;
    } */
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.dragon_helmet.name"));
		}
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
}