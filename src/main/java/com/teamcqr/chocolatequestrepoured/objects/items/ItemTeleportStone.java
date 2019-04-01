package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.init.base.ItemBase;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTeleportStone extends ItemBase
{
	private String X = "x";
	private String Y = "y";
	private String Z = "z";
	
	public ItemTeleportStone(String name) 
	{
		super(name);
		setMaxDamage(100);
	}
	
	@Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.getCooldownTracker().setCooldown(stack.getItem(), 30);
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		if(entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entityLiving;
			
			if(stack.hasTagCompound() && !player.isSneaking())
			{
				if(stack.getTagCompound().hasKey(X) && stack.getTagCompound().hasKey(Y) && stack.getTagCompound().hasKey(Z) && getMaxItemUseDuration(stack) - timeLeft >= 30)
				{
					player.setPosition(stack.getTagCompound().getDouble(X), stack.getTagCompound().getDouble(Y), stack.getTagCompound().getDouble(Z));
					for(int i = 0; i < 30; i++)
					{
						worldIn.spawnParticle(EnumParticleTypes.PORTAL, player.posX + worldIn.rand.nextDouble() - 0.5D, player.posY + 0.5D, player.posZ + worldIn.rand.nextDouble() - 0.5D, 0D, 0D, 0D);
					}
					worldIn.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
					
					if(!player.capabilities.isCreativeMode)
					{
						stack.setItemDamage(stack.getItemDamage() + 1);
					}
				}
			}
			
			if(getPoint(stack) == null && getMaxItemUseDuration(stack) - timeLeft >= 30)
			{
				setPoint(stack, player);
				for(int i = 0; i < 10; i++)
				{
					worldIn.spawnParticle(EnumParticleTypes.FLAME, player.posX + worldIn.rand.nextDouble() - 0.5D, player.posY + 0.5D, player.posZ + worldIn.rand.nextDouble() - 0.5D, 0D, 0D, 0D);
				}
				worldIn.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
			}
			
			if(player.isSneaking() && stack.hasTagCompound() && getMaxItemUseDuration(stack) - timeLeft >= 30)
			{
				stack.getTagCompound().removeTag(X);
				stack.getTagCompound().removeTag(Y);
				stack.getTagCompound().removeTag(Z);
				worldIn.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
				for(int i = 0; i < 10; i++)
				{
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, player.posX + worldIn.rand.nextDouble() - 0.5D, player.posY + 0.5D, player.posZ + worldIn.rand.nextDouble() - 0.5D, 0D, 0D, 0D);
				}
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.teleport_stone.name"));
			
			if(stack.hasTagCompound())
			{
				if(stack.getTagCompound().hasKey(X) && stack.getTagCompound().hasKey(Y) && stack.getTagCompound().hasKey(Z))
				{
					tooltip.add(TextFormatting.BLUE + I18n.format("description.teleport_stone_position.name"));
					tooltip.add(TextFormatting.BLUE + I18n.format("X: " + (int)stack.getTagCompound().getDouble(X)));
					tooltip.add(TextFormatting.BLUE + I18n.format("Y: " + (int)stack.getTagCompound().getDouble(Y)));
					tooltip.add(TextFormatting.BLUE + I18n.format("Z: " + (int)stack.getTagCompound().getDouble(Z)));
				}
			}
		}		
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
	
	private void setPoint(ItemStack stack, EntityPlayer player) 
	{
		NBTTagCompound stone = stack.getTagCompound();
		
		if(stone == null)
		{
			stone = new NBTTagCompound();
			stack.setTagCompound(stone);
		}
		
		if(!stone.hasKey(X))
		{
			stone.setDouble(X, player.posX);
		}
		
		if(!stone.hasKey(Y))
		{
			stone.setDouble(Y, player.posY);
		}
		
		if(!stone.hasKey(Z))
		{
			stone.setDouble(Z, player.posZ);
		}
	}
	
	private BlockPos getPoint(ItemStack stack) 
	{
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().hasKey(X) && stack.getTagCompound().hasKey(Y) && stack.getTagCompound().hasKey(Z)) 
			{
				NBTTagCompound stone = stack.getTagCompound();
	
				double x = stone.getDouble(X);
				double y = stone.getDouble(Y);
				double z = stone.getDouble(Z);
				
				return new BlockPos(x,y,z);
			}
		}
		return null;
	}
}