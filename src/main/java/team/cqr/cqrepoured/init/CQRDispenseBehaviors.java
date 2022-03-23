package team.cqr.cqrepoured.init;

import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.TNTBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Bootstrap.BehaviorDispenseOptional;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.misc.EntityTNTPrimedCQR;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBubble;
import team.cqr.cqrepoured.item.ItemSoulBottle;
import team.cqr.cqrepoured.item.gun.ItemBubblePistol;

import java.util.Random;

public class CQRDispenseBehaviors {

	private static final Random rng = new Random();

	public static void registerDispenseBehaviors() {
		// Bubble Gun
		DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(CQRItems.BUBBLE_PISTOL, DISPENSE_BEHAVIOR_BUBBLE_GUN);
		DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(CQRItems.BUBBLE_RIFLE, DISPENSE_BEHAVIOR_BUBBLE_GUN);

		// Soul bottle
		DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(CQRItems.SOUL_BOTTLE, DISPENSE_BEHAVIOR_SOUL_BOTTLE);

		// CQR TNT
		DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(CQRBlocks.TNT), DISPENSE_BEHAVIOR_TNT_CQR);
		DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FLINT_AND_STEEL, DISPENSE_BEHAVIOR_IGNITE_TNT_CQR);
	}

	public static final IDispenseItemBehavior DISPENSE_BEHAVIOR_BUBBLE_GUN = (source, stack) -> {
		Vector3d velocity = new Vector3d(0, 0, 0);
		switch (source.getBlockState().getValue(DispenserBlock.FACING)) {
		case DOWN:
			velocity = new Vector3d(0, -1, 0);
			break;
		case EAST:
			velocity = new Vector3d(1, 0, 0);
			break;
		case NORTH:
			velocity = new Vector3d(0, 0, -1);
			break;
		case SOUTH:
			velocity = new Vector3d(0, 0, 1);
			break;
		case UP:
			velocity = new Vector3d(0, 1, 0);
			break;
		case WEST:
			velocity = new Vector3d(-1, 0, 0);
			break;
		default:
			break;

		}
		IPosition disPos = DispenserBlock.getDispensePosition(source);
		Vector3d startLoc = new Vector3d(disPos.getX(), disPos.getY(), disPos.getZ());
		Item item = stack.getItem();
		double acc = 0.5D;
		if (item instanceof ItemBubblePistol) {
			ItemBubblePistol pistol = (ItemBubblePistol) item;
			acc = pistol.getInaccurary();
		}
		Vector3d v = new Vector3d(-acc + velocity.x + (2 * acc * rng.nextDouble()), -acc + velocity.y + (2 * acc * rng.nextDouble()), -acc + velocity.z + (2 * acc * rng.nextDouble()));
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
	};

	public static final IDispenseItemBehavior DISPENSE_BEHAVIOR_SOUL_BOTTLE = (source, stack) -> {
		Vector3d velocity = new Vector3d(0, 0, 0);
		switch (source.getBlockState().getValue(DispenserBlock.FACING)) {
		case DOWN:
			velocity = new Vector3d(0, -1, 0);
			break;
		case EAST:
			velocity = new Vector3d(1, 0, 0);
			break;
		case NORTH:
			velocity = new Vector3d(0, 0, -1);
			break;
		case SOUTH:
			velocity = new Vector3d(0, 0, 1);
			break;
		case UP:
			velocity = new Vector3d(0, 2, 0);
			break;
		case WEST:
			velocity = new Vector3d(-1, 0, 0);
			break;
		default:
			break;

		}
		IPosition disPos = DispenserBlock.getDispensePosition(source);
		Vector3d pos = new Vector3d(disPos.getX(), disPos.getY(), disPos.getZ()).add(velocity);

		if (stack.hasTagCompound()) {
			CompoundNBT bottle = stack.getTagCompound();

			if (bottle.hasKey(ItemSoulBottle.ENTITY_IN_TAG)) {
				if (!source.getWorld().isRemote) {
					CompoundNBT entityTag = (CompoundNBT) bottle.getTag(ItemSoulBottle.ENTITY_IN_TAG);
					((ItemSoulBottle) stack.getItem()).createEntityFromNBT(entityTag, source.getWorld(), pos.x, pos.y, pos.z);
				}
			}
		}

		stack.shrink(1);
		if (stack.isEmpty()) {
			stack = ItemStack.EMPTY;
		}
		return stack;
	};

	public static final IDispenseItemBehavior DISPENSE_BEHAVIOR_TNT_CQR = (source, stack) -> {
		World world = source.getWorld();
		BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue(DispenserBlock.FACING));
		EntityTNTPrimedCQR entitytntprimed = new EntityTNTPrimedCQR(world, blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D, (LivingEntity) null);
		world.spawnEntity(entitytntprimed);
		world.playSound((PlayerEntity) null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		stack.shrink(1);
		return stack;
	};

	public static final BehaviorDispenseOptional DISPENSE_BEHAVIOR_IGNITE_TNT_CQR = new BehaviorDispenseOptional() {
		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			World world = source.getWorld();
			this.successful = true;
			BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue(DispenserBlock.FACING));

			if (world.isAirBlock(blockpos)) {
				world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());

				if (stack.attemptDamageItem(1, world.rand, (ServerPlayerEntity) null)) {
					stack.setCount(0);
				}
			} else if (world.getBlockState(blockpos).getBlock() == CQRBlocks.TNT) {
				CQRBlocks.TNT.onPlayerDestroy(world, blockpos, CQRBlocks.TNT.getDefaultState().withProperty(TNTBlock.EXPLODE, true));
				world.setBlockToAir(blockpos);
			} else {
				this.successful = false;
			}

			return stack;
		}
	};

}
