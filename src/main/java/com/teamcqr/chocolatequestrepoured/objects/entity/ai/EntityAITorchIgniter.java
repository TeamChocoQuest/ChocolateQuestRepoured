package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockUnlitTorch;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.block.Block;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAITorchIgniter extends AbstractCQREntityAI {

	protected static final int searchRadiusHorizontal = 8;
	protected static final int searchRadiusVertical = 2;
	protected static final int maxDistanceToEntity = 3;
	// Setting this to true crashes the game (!)
	protected static final boolean checkPosReachableBeforeSettingPos = false;
	World world;
	BlockPos nearestTorch = null;
	int timerForPosConflicts = 10;

	public EntityAITorchIgniter(AbstractEntityCQR ent) {
		super(ent);
		this.world = ent.getEntityWorld();
	}

	@Override
	public boolean shouldExecute() {
		int x = (int) Math.floor(this.entity.posX);
		int y = (int) Math.floor(this.entity.posY);
		int z = (int) Math.floor(this.entity.posZ);

		if (this.timerForPosConflicts > 0) {
			for (BlockPos posTmp : BlockPos.getAllInBox(x - searchRadiusHorizontal, y - searchRadiusVertical, z - searchRadiusHorizontal, x + (searchRadiusHorizontal + 2), y + searchRadiusVertical, z + searchRadiusHorizontal)) {
				if (Block.isEqualTo(this.world.getBlockState(posTmp).getBlock(), ModBlocks.UNLIT_TORCH) && (checkPosReachableBeforeSettingPos ? this.entity.getNavigator().getPathToPos(this.nearestTorch) != null : true)) {
					if (this.nearestTorch != null) {
						if (this.entity.getDistanceSq(posTmp) < this.entity.getDistanceSq(this.nearestTorch)) {
							this.nearestTorch = posTmp;
							this.timerForPosConflicts--;
						}
					} else {
						this.nearestTorch = posTmp;
					}
				}
			}
		}

		return this.nearestTorch != null;
	}

	@Override
	public void updateTask() {
		if (this.nearestTorch != null && Block.isEqualTo(this.world.getBlockState(this.nearestTorch).getBlock(), ModBlocks.UNLIT_TORCH)) {
			if (this.entity.getDistanceSq(this.nearestTorch) <= (maxDistanceToEntity * maxDistanceToEntity)) {
				this.entity.swingArm(EnumHand.MAIN_HAND);
				try {
					BlockUnlitTorch.lightUp(this.world, this.nearestTorch, this.world.getBlockState(this.nearestTorch).getValue(BlockUnlitTorch.FACING));
				} catch (IllegalArgumentException ex) {
					this.resetTask();
				}
				this.nearestTorch = null;
			} else if (this.entity.getNavigator().getPathToPos(this.nearestTorch) != null) {
				// If we are not close enough we need to walk to the torch, now check if there's a path to it
				this.entity.getNavigator().tryMoveToXYZ(this.nearestTorch.getX(), this.nearestTorch.getY(), this.nearestTorch.getZ(), 1.0);
			} else {
				this.nearestTorch = null;
			}
		}
	}

	@Override
	public void resetTask() {
		this.nearestTorch = null;
		this.timerForPosConflicts = 10;
	}

}
