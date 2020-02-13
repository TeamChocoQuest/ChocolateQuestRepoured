package com.teamcqr.chocolatequestrepoured.objects.entity.misc;

import java.util.HashSet;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityBubble extends EntityLivingBase {

	private int flyTicks = 0;

	protected static final int FLY_TIME_MAX = 70;

	public EntityBubble(World worldIn) {
		super(worldIn);
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return new HashSet<ItemStack>();
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {

	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return EnumHandSide.LEFT;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		flyTicks += 5;
		return super.attackEntityFrom(source, amount);
	}
	
	@Override
	public boolean shouldDismountInWater(Entity rider) {
		return false;
	}
	
	@Override
	public boolean shouldRiderSit() {
		return false;
	}
	
	@Override
	public boolean canBePushed() {
		return true;
	}
	
	@Override
	public boolean getIsInvulnerable() {
		return true;
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;

		flyTicks++;

		if (!isBeingRidden() || getLowestRidingEntity() == null) {
			setDead();
		}

		Material mat = world.getBlockState(new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY + getLowestRidingEntity().height), MathHelper.floor(posZ))).getMaterial();

		if ((mat == Material.AIR) || (mat.isLiquid())) {
			motionY = 0.05D;
		}
		if ((flyTicks >= FLY_TIME_MAX) || ((getLowestRidingEntity().collidedVertically) && (!getPassengers().get(0).onGround))) {
			setDead();
		}
	}

}