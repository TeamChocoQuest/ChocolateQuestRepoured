package com.teamcqr.chocolatequestrepoured.objects.items.guns;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBubble;
import com.teamcqr.chocolatequestrepoured.util.IRangedWeapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemBubbleGun extends Item implements IRangedWeapon {
	
	private final Random rng = new Random();

	public ItemBubbleGun() {
		super();
		setMaxDamage(200);
		setMaxStackSize(1);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 100;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if(entityLiving instanceof EntityPlayer) {
			((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, 30);
		}
		stack.damageItem(1, entityLiving);
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(entityIn instanceof EntityLivingBase && ((EntityLivingBase) entityIn).isHandActive() && ((EntityLivingBase) entityIn).getActiveItemStack() == stack) {
			shootBubbles((EntityLivingBase) entityIn);
		}
	}
	
	private void shootBubbles(EntityLivingBase entity) {
		double x = -Math.sin(Math.toRadians(entity.rotationYaw));
		double z = Math.cos(Math.toRadians(entity.rotationYaw));
		double y = -Math.sin(Math.toRadians(entity.rotationPitch));
		shootBubbles(new Vec3d(x,y,z), entity);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	private void shootBubbles(Vec3d velocity, EntityLivingBase shooter) {
		Vec3d v = new Vec3d( -0.55D + velocity.x + rng.nextDouble(),  -0.55D + velocity.y + rng.nextDouble(),  -0.55D + velocity.z + rng.nextDouble());
		v = v.normalize();
		v = v.scale(1.4);
		
		ProjectileBubble bubble = new ProjectileBubble(shooter.world, shooter);
		bubble.motionX = v.x;
		bubble.motionY = v.y;
		bubble.motionZ = v.z;
		bubble.velocityChanged = true;
		shooter.world.spawnEntity(bubble);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public SoundEvent getShootSound() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void shoot(World world, EntityLivingBase shooter, Entity target, EnumHand hand) {
		// TODO Auto-generated method stub
	}

}
