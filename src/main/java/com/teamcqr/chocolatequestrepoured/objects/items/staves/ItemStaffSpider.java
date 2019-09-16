package com.teamcqr.chocolatequestrepoured.objects.items.staves;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileSpiderBall;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStaffSpider extends Item {

	public ItemStaffSpider() {
		setMaxDamage(2048);
		setMaxStackSize(1);
	}

	/*
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}
	*/

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		shoot(worldIn, playerIn, stack, handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	public void shoot(World worldIn, EntityPlayer playerIn, ItemStack stack, EnumHand handIn) {
		worldIn.playSound(playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SLIME_SQUISH,
				SoundCategory.MASTER, 4.0F, (1.0F + (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F) * 0.7F,
				false);
		playerIn.swingArm(handIn);

		if (!worldIn.isRemote) {
			ProjectileSpiderBall ball = new ProjectileSpiderBall(worldIn, playerIn);
			ball.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 0F);
			worldIn.spawnEntity(ball);
			stack.damageItem(1, playerIn);
			playerIn.getCooldownTracker().setCooldown(stack.getItem(), 20);
		}
	}

	/*
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;

			worldIn.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SLIME_SQUISH,
					SoundCategory.MASTER, 4.0F, (1.0F + (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F) * 0.7F,
					false);

			if (!worldIn.isRemote) {
				ProjectileSpiderBall ball = new ProjectileSpiderBall(worldIn, player);
				ball.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 0F);
				worldIn.spawnEntity(ball);
				stack.damageItem(1, player);
				player.getCooldownTracker().setCooldown(stack.getItem(), 20);
			}
		}
	}
	*/

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.staff_spider.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

}