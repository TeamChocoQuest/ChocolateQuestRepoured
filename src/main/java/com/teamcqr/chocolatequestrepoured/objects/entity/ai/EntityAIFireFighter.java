package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIFireFighter extends AbstractCQREntityAI {
	
	protected static final int searchRadiusHorizontal = 8;
	protected static final int searchRadiusVertical = 2;
	protected static final int maxDistanceToEntity = 3;
	protected static final boolean checkPosReachableBeforeSettingPos = false;
	protected static final float speedToWalkToFire = 1.15F;
	World world;
	BlockPos nearestFire = null;
	int timerForPosConflicts = 10;

	public EntityAIFireFighter(AbstractEntityCQR ent) {
		super(ent);
		world = ent.getEntityWorld();
	}

	@Override
	public boolean shouldExecute() {
		int x = (int) Math.floor(entity.posX);
		int y = (int) Math.floor(entity.posY);
		int z = (int) Math.floor(entity.posZ);
		
		if(timerForPosConflicts > 0) {
			for(BlockPos posTmp : BlockPos.getAllInBox(x - searchRadiusHorizontal, y - searchRadiusVertical, z - searchRadiusHorizontal, x + searchRadiusHorizontal, y + searchRadiusVertical, z + searchRadiusHorizontal)) {
				if(isSuitableFire(posTmp)) {
					if(nearestFire != null) {
						if(entity.getDistanceSq(posTmp) < entity.getDistanceSq(nearestFire)) {
							nearestFire = posTmp;
							timerForPosConflicts--;
						}
					} else {
						nearestFire = posTmp;
					}
				}
			}
		}
		
		return nearestFire != null;
	}
	
	private boolean isSuitableFire(BlockPos posTmp) {
		if(world.getBlockState(posTmp).getMaterial() == Material.FIRE && (checkPosReachableBeforeSettingPos ? entity.getNavigator().getPathToPos(nearestFire) != null : true)) {
			Block block = world.getBlockState(posTmp.down()).getBlock();
			
			if(block != null && (block == Blocks.NETHERRACK || block == Blocks.MAGMA)) {
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void updateTask() {
		if(nearestFire != null) {
			if(entity.getDistanceSq(nearestFire) <= (maxDistanceToEntity * maxDistanceToEntity)) {
				entity.swingArm(EnumHand.MAIN_HAND);
				world.setBlockToAir(nearestFire);
				//DONE: Particles and sounds
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, nearestFire.getX() -0.5d, nearestFire.getY() -0.5D, nearestFire.getZ() -0.5D, 1.0, 1.0, 1.0, 1);
				world.playSound(nearestFire.getX(), nearestFire.getY(), nearestFire.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1.0F, 1.0F, true);
				nearestFire = null;
			} else if(entity.getNavigator().getPathToPos(nearestFire) != null) {
				//If we are not close enough we need to walk to the fire, now check if there's a path to it
				entity.getNavigator().tryMoveToXYZ(nearestFire.getX(), nearestFire.getY(), nearestFire.getZ(), speedToWalkToFire);
			} else {
				this.nearestFire = null;
			}
		}
	}
	
	@Override
	public void resetTask() {
		nearestFire = null;
		timerForPosConflicts = 10;
	}

}
