package com.teamcqr.chocolatequestrepoured.objects.items.guns;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBullet;
import com.teamcqr.chocolatequestrepoured.util.handlers.SoundsHandler;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMusket extends ItemRevolver
{
	public ItemMusket(String name) 
	{
		super(name);
		setMaxDamage(300);
		setMaxStackSize(1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.BLUE + "5.0 " + I18n.format("description.bullet_damage.name"));
		tooltip.add(TextFormatting.RED + "-60 " + I18n.format("description.fire_rate.name"));
		tooltip.add(TextFormatting.RED + "-10" + "% " + I18n.format("description.accuracy.name"));
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.gun.name"));
		}		
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
		if(entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entityLiving;
			boolean flag = player.capabilities.isCreativeMode;
			ItemStack itemstack = findAmmo(player);
				
			if(!itemstack.isEmpty() || flag)
			{
				if(!worldIn.isRemote)
				{
					if(flag && itemstack.isEmpty())
					{
						ProjectileBullet bulletE = new ProjectileBullet(worldIn, player, 1);
						bulletE.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.5F, 2F);
						player.getCooldownTracker().setCooldown(player.getHeldItemMainhand().getItem(), 60);
						worldIn.spawnEntity(bulletE);
					}
					else
					{
						ProjectileBullet bulletE = new ProjectileBullet(worldIn, player, getBulletType(itemstack));
						bulletE.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.5F, 2F);
						player.getCooldownTracker().setCooldown(player.getHeldItemMainhand().getItem(), 60);
						worldIn.spawnEntity(bulletE);
						stack.damageItem(1, player);
					}
				}
				
				worldIn.playSound(player.posX, player.posY, player.posZ, SoundsHandler.GUN_SHOOT, SoundCategory.MASTER, 1.0F, 1.0F, false);
				entityLiving.rotationPitch -= worldIn.rand.nextFloat() * 10;
						
				if(!flag)
	            {
					itemstack.shrink(1);

					if(itemstack.isEmpty())
					{
						player.inventory.deleteStack(itemstack);
					}
				}
			}
		}
    }
}