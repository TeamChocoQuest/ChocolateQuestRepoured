package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelBackpack;
import com.teamcqr.chocolatequestrepoured.objects.base.ArmorBase;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBackpack extends ArmorBase
{
	public ItemBackpack(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) 
	{
		super(name, materialIn, renderIndexIn, equipmentSlotIn);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
        EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
        ItemStack itemstack1 = playerIn.getItemStackFromSlot(entityequipmentslot);

        if((itemstack1.isEmpty() && (playerIn.getHeldItemMainhand().getItem() != this)) || (itemstack1.isEmpty() && (playerIn.getHeldItemMainhand().getItem() == this && playerIn.isSneaking())))
        {
            playerIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            itemstack.setCount(0);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        if(!worldIn.isRemote)
        {
        	if(playerIn.getHeldItemMainhand().getItem() == this && !playerIn.isSneaking())
        	{
        		playerIn.openGui(CQRMain.INSTANCE, Reference.BACKPACK_GUI_ID, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
        		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        	}
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);    
    }
	
	@SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
    {
		if(itemStack != null)
		{
			if(itemStack.getItem() instanceof ItemArmor)
			{
				ModelBackpack model = new ModelBackpack();
				
				model.bipedBody.showModel = armorSlot == EntityEquipmentSlot.CHEST;
				
			    model.isChild = _default.isChild;
			    model.isRiding = _default.isRiding;
			    model.isSneak = _default.isSneak;
			    model.rightArmPose = _default.rightArmPose;
			    model.leftArmPose = _default.leftArmPose;
			     
			    return model;
			}
		}
		return null;
    }
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		return "cqrepoured:textures/models/armor/backpack_layer_1.png";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.backpack.name"));
		}		
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
}