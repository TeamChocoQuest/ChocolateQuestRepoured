package com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;

import net.minecraft.block.Block;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class EntityCQRNetherDragonSegment extends MultiPartEntityPart {
	
	//TODO: Add the tail peek

	private final EntityCQRNetherDragon dragon;
	private final int partID;
	
	private boolean tailPeek = false;
	private int tailPart = -1;
	
	public EntityCQRNetherDragonSegment(EntityCQRNetherDragon dragon, int partID) {
		super((IEntityMultiPart)dragon, "dragonPart" + partID, 0.5F, 0.5F);
		
		setSize(1.25F, 1.25F);
		
		this.dragon = dragon;
		this.partID = partID;
		
		if(partID > EntityCQRNetherDragon.SEGMENT_COUNT -3) {
			this.tailPeek = true;
			
			this.tailPart = EntityCQRNetherDragon.SEGMENT_COUNT - this.partID +1;
			//System.out.println("part id: " + this.tailPart);
		}
		
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
	
	public boolean isTailPeek() {
		return this.tailPeek;
	}
	
	public int getTailPartIndex() {
		return this.tailPart;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if(this.dragon.isDead) {
			return false;
		}
		return this.dragon.processInitialInteract(player, hand);
	}
	
}
