package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import net.minecraft.block.Block;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;

public class EntityCQRNetherDragonSegment extends MultiPartEntityPart {

	private final EntityCQRNetherDragon dragon;
	private final int partID;
	
	public EntityCQRNetherDragonSegment(EntityCQRNetherDragon dragon, int partID) {
		super((IEntityMultiPart)dragon, "dragonPart" + partID, 0.5F, 0.5F);
		
		setSize(1.5F, 1.5F);
		
		this.dragon = dragon;
		this.partID = partID;
		//String partName, float width, float height
		this.setInvisible(false);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return this.dragon.attackEntityFromPart(this, source, amount);
		//return super.attackEntityFrom(source, amount);
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		++this.ticksExisted;
	}
	
	@Override
	public boolean isNonBoss() {
		return dragon.isNonBoss();
	}

	//As this is a part it does not make any noises
	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
	}
	
	@Override
	public void setRotation(float yaw, float pitch) {
		super.setRotation(yaw, pitch);
	}

}
