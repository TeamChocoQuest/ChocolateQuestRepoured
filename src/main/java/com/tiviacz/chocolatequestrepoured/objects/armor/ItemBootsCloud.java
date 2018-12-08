package com.tiviacz.chocolatequestrepoured.objects.armor;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.tiviacz.chocolatequestrepoured.CQRMain;
import com.tiviacz.chocolatequestrepoured.init.ModItems;
import com.tiviacz.chocolatequestrepoured.init.base.ArmorBase;
import com.tiviacz.chocolatequestrepoured.util.Reference;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBootsCloud extends ArmorBase
{
	public ItemBootsCloud(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn)
	{ 		
		super(name, materialIn, renderIndexIn, equipmentSlotIn);
	}
			
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) 
	{				
		player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 0, 1, false, false));
		player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 0, 4, false, false));
		        
		if(!player.onGround)
		{		            
			player.jumpMovementFactor += 0.04F;
			world.spawnParticle(EnumParticleTypes.CLOUD, player.getPosition().getX(), player.getPosition().getY() - 0.5F, player.getPosition().getZ(), 0, 1, 0);				
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.cloud_boots.name"));
		}
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
}