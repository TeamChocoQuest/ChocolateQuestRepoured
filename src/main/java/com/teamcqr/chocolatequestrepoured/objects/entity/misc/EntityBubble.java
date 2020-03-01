package com.teamcqr.chocolatequestrepoured.objects.entity.misc;

import java.util.HashSet;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityBubble extends EntityLiving {

	private int flyTicks = 0;
	private int riderLessTicks = 0;

	protected static final int FLY_TIME_MAX = 140;
	protected static final float BASE_SIZE = 0.5F;
	
	protected static final DataParameter<Float> PASSENGER_HEIGHT = EntityDataManager.<Float>createKey(EntityBubble.class, DataSerializers.FLOAT);

	public EntityBubble(World worldIn) {
		super(worldIn);
		setSize(BASE_SIZE, BASE_SIZE);
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
		return true;
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
	public boolean hasNoGravity() {
		return true;
	}
	
	@Override
	public void onLivingUpdate() {
		if(getLowestRidingEntity() == null || getPassengers().isEmpty()) {
			setDead();
			return;
		}
		super.onLivingUpdate();

		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;

		flyTicks++;

		if (isInLava() || !isBeingRidden() || getLowestRidingEntity() == null) {
			riderLessTicks++;
			if(riderLessTicks >= 20) {
				setDead();
				return;
			}
		}

		Material mat = world.getBlockState(new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY + getLowestRidingEntity().height), MathHelper.floor(posZ))).getMaterial();

		if ((mat == Material.AIR) || (mat.isLiquid())) {
			motionY = 0.05D;
		}
		if ((flyTicks >= FLY_TIME_MAX) || ((getLowestRidingEntity().collidedVertically) && (!getPassengers().get(0).onGround))) {
			setDead();
			return;
		}
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(PASSENGER_HEIGHT, 0F);
	}
	
	@Override
	protected void addPassenger(Entity passenger) {
		if(getPassengers().size() > 1) {
			return;
		}
		this.dataManager.set(PASSENGER_HEIGHT, passenger.height);
		float size = passenger.height > passenger.width ? passenger.height : passenger.width;
		this.resize(size / BASE_SIZE);
		super.addPassenger(passenger);
	}
	
	public float getPassengerHeight() {
		return this.dataManager.get(PASSENGER_HEIGHT);
	}
	
	@Override
	public double getMountedYOffset() {
		return 0;//this.height /4D;
	}
	
	public void resize(float scale) {
		this.setSize(this.width * scale, this.height * scale);
	}

}