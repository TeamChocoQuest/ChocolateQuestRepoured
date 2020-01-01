package com.teamcqr.chocolatequestrepoured.objects.items.staves;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileSpiderBall;
import com.teamcqr.chocolatequestrepoured.util.IRangedWeapon;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStaffSpider extends Item implements IRangedWeapon{

	public ItemStaffSpider() {
		setMaxDamage(2048);
		setMaxStackSize(1);
	}

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

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.staff_spider.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}


	@Override
	public void shoot(World worldIn, EntityLivingBase shooter, Entity target, EnumHand handIn) {
		shooter.swingArm(handIn);

		if (!worldIn.isRemote) {
			ProjectileSpiderBall ball = new ProjectileSpiderBall(worldIn, shooter);
			Vec3d v = target.getPositionVector().subtract(shooter.getPositionVector());
			v = v.normalize();
			v = v.scale(0.5D);
			ball.setVelocity(v.x, v.y, v.z);
			worldIn.spawnEntity(ball);
		}
	}
	
	@Override
	public SoundEvent getShootSound() {
		return SoundEvents.ENTITY_SLIME_SQUISH;
	}

}