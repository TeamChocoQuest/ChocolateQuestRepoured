package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockUnlitTorch;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAITorchIgniter extends EntityAIBase {

	protected static final int searchRadiusHorizontal = 5;
	protected static final int searchRadiusVertical = 1;
	protected static final int maxDistanceToEntity = 10;
	//Setting this to true crashes the game (!)
	protected static final boolean checkPosReachableBeforeSettingPos = false;
	World world;
	AbstractEntityCQR entity;
	BlockPos nearestTorch = null;
	int timerForPosConflicts = 10;

	public EntityAITorchIgniter(AbstractEntityCQR ent) {
		entity = ent;
		world = ent.getEntityWorld();
	}

	@Override
	public boolean shouldExecute() {
		int x = (int) Math.floor(entity.posX);
		int y = (int) Math.floor(entity.posY);
		int z = (int) Math.floor(entity.posZ);
		
		if(timerForPosConflicts > 0) {
			for(BlockPos posTmp : BlockPos.getAllInBox(x - searchRadiusHorizontal, y - searchRadiusVertical, z - searchRadiusHorizontal, x + searchRadiusHorizontal, y + searchRadiusVertical, z + searchRadiusHorizontal)) {
				if(Block.isEqualTo(world.getBlockState(posTmp).getBlock(), ModBlocks.UNLIT_TORCH) && (checkPosReachableBeforeSettingPos ? entity.getNavigator().getPathToPos(nearestTorch) != null : true)) {
					if(nearestTorch != null) {
						if(entity.getDistanceSq(posTmp) < entity.getDistanceSq(nearestTorch)) {
							nearestTorch = posTmp;
							timerForPosConflicts--;
						}
					} else {
						nearestTorch = posTmp;
					}
				}
			}
		}
		
		
		return nearestTorch != null;
	}
	
	@Override
	public void updateTask() {
		if(nearestTorch != null && Block.isEqualTo(world.getBlockState(nearestTorch).getBlock(), ModBlocks.UNLIT_TORCH)) {
			if(entity.getDistanceSq(nearestTorch) <= (maxDistanceToEntity * maxDistanceToEntity)) {
				entity.swingArm(EnumHand.MAIN_HAND);
				try {
					BlockUnlitTorch.lightUp(world, nearestTorch, world.getBlockState(nearestTorch).getValue(BlockUnlitTorch.FACING));
				} catch(IllegalArgumentException ex) {
					resetTask();
				}
				nearestTorch = null;
			} else if(entity.getNavigator().getPathToPos(nearestTorch) != null) {
				//If we are not close enough we need to walk to the torch, now check if there's a path to it
				entity.getNavigator().tryMoveToXYZ(nearestTorch.getX(), nearestTorch.getY(), nearestTorch.getZ(), 1.0);
			} else {
				this.nearestTorch = null;
			}
		}
	}
	
	@Override
	public void resetTask() {
		nearestTorch = null;
		timerForPosConflicts = 10;
	}

}
