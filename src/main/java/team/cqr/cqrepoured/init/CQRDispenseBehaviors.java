package team.cqr.cqrepoured.init;

import java.util.Random;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileBubble;
import team.cqr.cqrepoured.objects.items.ItemSoulBottle;
import team.cqr.cqrepoured.objects.items.guns.ItemBubblePistol;

public class CQRDispenseBehaviors {

	private static final Random rng = new Random();
	public static final IBehaviorDispenseItem DISPENSE_BEHAVIOR_BUBBLE_GUN = new IBehaviorDispenseItem() {

		@Override
		public ItemStack dispense(IBlockSource source, ItemStack stack) {
			Vec3d velocity = new Vec3d(0, 0, 0);
			switch ((EnumFacing) source.getBlockState().getValue(BlockDispenser.FACING)) {
			case DOWN:
				velocity = new Vec3d(0, -1, 0);
				break;
			case EAST:
				velocity = new Vec3d(1, 0, 0);
				break;
			case NORTH:
				velocity = new Vec3d(0, 0, -1);
				break;
			case SOUTH:
				velocity = new Vec3d(0, 0, 1);
				break;
			case UP:
				velocity = new Vec3d(0, 1, 0);
				break;
			case WEST:
				velocity = new Vec3d(-1, 0, 0);
				break;
			default:
				break;

			}
			IPosition disPos = BlockDispenser.getDispensePosition(source);
			Vec3d startLoc = new Vec3d(disPos.getX(), disPos.getY(), disPos.getZ());
			Item item = stack.getItem();
			double acc = 0.5D;
			if (item instanceof ItemBubblePistol) {
				ItemBubblePistol pistol = (ItemBubblePistol) item;
				acc = pistol.getInaccurary();
			}
			Vec3d v = new Vec3d(-acc + velocity.x + (2 * acc * rng.nextDouble()), -acc + velocity.y + (2 * acc * rng.nextDouble()), -acc + velocity.z + (2 * acc * rng.nextDouble()));
			v = v.normalize();
			v = v.scale(1.4);

			ProjectileBubble bubble = new ProjectileBubble(source.getWorld(), startLoc.x, startLoc.y, startLoc.z);
			bubble.motionX = v.x;
			bubble.motionY = v.y;
			bubble.motionZ = v.z;
			bubble.velocityChanged = true;
			source.getWorld().spawnEntity(bubble);

			source.getWorld().playSound(disPos.getX(), disPos.getY(), disPos.getZ(), CQRSounds.BUBBLE_BUBBLE, SoundCategory.BLOCKS, 1, 0.75F + (0.5F * rng.nextFloat()), false);

			// DONE: FIgure out how to make the stack damaged
			stack.attemptDamageItem(1, source.getWorld().rand, null);
			if (stack.getItemDamage() >= stack.getMaxDamage()) {
				stack = ItemStack.EMPTY;
			}
			return stack;
		}
	};

	public static void registerDispenseBehaviors() {
		// Bubble Gun
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(CQRItems.BUBBLE_PISTOL, DISPENSE_BEHAVIOR_BUBBLE_GUN);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(CQRItems.BUBBLE_RIFLE, DISPENSE_BEHAVIOR_BUBBLE_GUN);

		// Soul bottle
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(CQRItems.SOUL_BOTTLE, DISPENSE_BEHAVIOR_SOUL_BOTTLE);
	}

	public static final IBehaviorDispenseItem DISPENSE_BEHAVIOR_SOUL_BOTTLE = new IBehaviorDispenseItem() {

		@Override
		public ItemStack dispense(IBlockSource source, ItemStack stack) {
			Vec3d velocity = new Vec3d(0, 0, 0);
			switch ((EnumFacing) source.getBlockState().getValue(BlockDispenser.FACING)) {
			case DOWN:
				velocity = new Vec3d(0, -1, 0);
				break;
			case EAST:
				velocity = new Vec3d(1, 0, 0);
				break;
			case NORTH:
				velocity = new Vec3d(0, 0, -1);
				break;
			case SOUTH:
				velocity = new Vec3d(0, 0, 1);
				break;
			case UP:
				velocity = new Vec3d(0, 2, 0);
				break;
			case WEST:
				velocity = new Vec3d(-1, 0, 0);
				break;
			default:
				break;

			}
			IPosition disPos = BlockDispenser.getDispensePosition(source);
			Vec3d pos = new Vec3d(disPos.getX(), disPos.getY(), disPos.getZ()).add(velocity);

			if (stack.hasTagCompound()) {
				NBTTagCompound bottle = stack.getTagCompound();

				if (bottle.hasKey(ItemSoulBottle.ENTITY_IN_TAG)) {
					if (!source.getWorld().isRemote) {
						NBTTagCompound entityTag = (NBTTagCompound) bottle.getTag(ItemSoulBottle.ENTITY_IN_TAG);
						((ItemSoulBottle) stack.getItem()).createEntityFromNBT(entityTag, source.getWorld(), pos.x, pos.y, pos.z);
					}
				}
			}

			stack.shrink(1);
			if (stack.isEmpty()) {
				stack = ItemStack.EMPTY;
			}
			return stack;
		}

	};

}
