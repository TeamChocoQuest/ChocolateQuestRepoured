package com.tiviacz.chocolatequestrepoured.objects.armor;

import com.tiviacz.chocolatequestrepoured.CQRMain;
import com.tiviacz.chocolatequestrepoured.init.ModItems;
import com.tiviacz.chocolatequestrepoured.init.base.ArmorBase;
import com.tiviacz.chocolatequestrepoured.util.IHasModel;
import com.tiviacz.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public class ItemCloudBoots extends ArmorBase implements IHasModel
{
		
	public static final ArmorMaterial CLOUD = EnumHelper.addArmorMaterial("cloud_boots", Reference.MODID + ":cloud", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F);
			
			
	public ItemCloudBoots(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn)
	{ 		
		super(name, materialIn, renderIndexIn, equipmentSlotIn);
	}
			
	@Override
	public void registerModels()
	{
		CQRMain.proxy.registerItemRenderer(this, 0, "inventory");			
	}
			
			
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) 
	{	
				
		player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 0, 1, false, false));
		player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 0, 4, false, false));
		        
		float x = player.getPosition().getX();
		float y = player.getPosition().getY() - 0.5F;
		float z = player.getPosition().getZ();
		        
		if(!player.onGround)
		{
			if(player instanceof EntityPlayer)
			{			            
				player.jumpMovementFactor += 0.04F;
				world.spawnParticle(EnumParticleTypes.CLOUD, x, y, z, 0, 0, 0);
			}				
		}
	}
}