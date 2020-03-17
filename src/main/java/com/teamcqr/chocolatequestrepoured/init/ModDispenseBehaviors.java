package com.teamcqr.chocolatequestrepoured.init;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBubble;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class ModDispenseBehaviors {

	private static final Random rng = new Random();
	public static final IBehaviorDispenseItem DISPENSE_BEHAVIOR_BUBBLE_GUN = new IBehaviorDispenseItem() {
		
		@Override
		public ItemStack dispense(IBlockSource source, ItemStack stack) {
			Vec3d velocity = new Vec3d(0,0,0);
			switch((EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING)) {
			case DOWN:
				velocity = new Vec3d(0,-1,0);
				break;
			case EAST:
				velocity = new Vec3d(1,0,0);
				break;
			case NORTH:
				velocity = new Vec3d(0,0,-1);
				break;
			case SOUTH:
				velocity = new Vec3d(0,0,1);
				break;
			case UP:
				velocity = new Vec3d(0,1,0);
				break;
			case WEST:
				velocity = new Vec3d(-1,0,0);
				break;
			default:
				break;
			
			}
			IPosition disPos = BlockDispenser.getDispensePosition(source);
			Vec3d startLoc = new Vec3d(disPos.getX(), disPos.getY(), disPos.getZ());
			Vec3d v = new Vec3d( -0.5D + velocity.x + rng.nextDouble(),  -0.5D + velocity.y + rng.nextDouble(),  -0.5D + velocity.z + rng.nextDouble());
			v = v.normalize();
			v = v.scale(1.4);
			
			ProjectileBubble bubble = new ProjectileBubble(source.getWorld(), startLoc.x, startLoc.y, startLoc.z);
			bubble.motionX = v.x;
			bubble.motionY = v.y;
			bubble.motionZ = v.z;
			bubble.velocityChanged = true;
			source.getWorld().spawnEntity(bubble);
			//TODO: FIgure out how to make the item not disappear
			return ItemStack.EMPTY;
		}
	};
	
	
	
	public static void registerDispenseBehaviors() {
		//Bubble Gun
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.BUBBLE_GUN, DISPENSE_BEHAVIOR_BUBBLE_GUN);
	}

}
