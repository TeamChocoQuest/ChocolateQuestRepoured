package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.init.base.SwordBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileEarthQuake;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBullBattleAxe extends SwordBase
{
	public ItemBullBattleAxe(String name, ToolMaterial material) 
	{
		super(name, material);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if(!playerIn.isSwingInProgress && playerIn.onGround)
		{
			playerIn.posY += 0.1D;
			playerIn.motionY += 0.35D;
			
			if(!worldIn.isRemote)
			{
				ProjectileEarthQuake quake = new ProjectileEarthQuake(worldIn, playerIn);
				quake.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 0.25F, 1.0F);
				worldIn.spawnEntity(quake);
				
				playerIn.getCooldownTracker().setCooldown(playerIn.getHeldItem(handIn).getItem(), 50);
		        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
			}
			playerIn.swingArm(handIn);
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.bull_battle_axe.name"));
		}		
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
	
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		return (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.PLANTS || state.getMaterial() == Material.VINE) ? 7.0F : super.getDestroySpeed(stack, state);
	}
}