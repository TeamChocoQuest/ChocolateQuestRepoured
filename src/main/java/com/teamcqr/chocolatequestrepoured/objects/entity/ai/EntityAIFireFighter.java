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
		this.world = ent.getEntityWorld();
	}

	@Override
	public boolean shouldExecute() {
		int x = (int) Math.floor(this.entity.posX);
		int y = (int) Math.floor(this.entity.posY);
		int z = (int) Math.floor(this.entity.posZ);

		if (this.timerForPosConflicts > 0) {
			for (BlockPos posTmp : BlockPos.getAllInBox(x - searchRadiusHorizontal, y - searchRadiusVertical, z - searchRadiusHorizontal, x + searchRadiusHorizontal, y + searchRadiusVertical, z + searchRadiusHorizontal)) {
				if (this.isSuitableFire(posTmp)) {
					if (this.nearestFire != null) {
						if (this.entity.getDistanceSq(posTmp) < this.entity.getDistanceSq(this.nearestFire)) {
							this.nearestFire = posTmp;
							this.timerForPosConflicts--;
						}
					} else {
						this.nearestFire = posTmp;
					}
				}
			}
		}

		return this.nearestFire != null;
	}

	private boolean isSuitableFire(BlockPos posTmp) {
		if (this.world.getBlockState(posTmp).getMaterial() == Material.FIRE && (checkPosReachableBeforeSettingPos ? this.entity.getNavigator().getPathToPos(this.nearestFire) != null : true)) {
			Block block = this.world.getBlockState(posTmp.down()).getBlock();

			if (block != null && (block == Blocks.NETHERRACK || block == Blocks.MAGMA)) {
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void updateTask() {
		if (this.nearestFire != null) {
			if (this.entity.getDistanceSq(this.nearestFire) <= (maxDistanceToEntity * maxDistanceToEntity)) {
				this.entity.swingArm(EnumHand.MAIN_HAND);
				this.world.setBlockToAir(this.nearestFire);
				// DONE: Particles and sounds
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.nearestFire.getX() - 0.5d, this.nearestFire.getY() - 0.5D, this.nearestFire.getZ() - 0.5D, 1.0, 1.0, 1.0, 1);
				this.world.playSound(this.nearestFire.getX(), this.nearestFire.getY(), this.nearestFire.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1.0F, 1.0F, true);
				this.nearestFire = null;
			} else if (this.entity.getNavigator().getPathToPos(this.nearestFire) != null) {
				// If we are not close enough we need to walk to the fire, now check if there's a path to it
				this.entity.getNavigator().tryMoveToXYZ(this.nearestFire.getX(), this.nearestFire.getY(), this.nearestFire.getZ(), speedToWalkToFire);
			} else {
				this.nearestFire = null;
			}
		}
	}

	@Override
	public void resetTask() {
		this.nearestFire = null;
		this.timerForPosConflicts = 10;
	}

}
