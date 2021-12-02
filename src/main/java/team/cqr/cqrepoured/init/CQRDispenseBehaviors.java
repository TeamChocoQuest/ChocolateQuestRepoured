package team.cqr.cqrepoured.init;

import java.util.Random;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap.BehaviorDispenseOptional;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.objects.blocks.BlockTNTCQR;
import team.cqr.cqrepoured.objects.entity.misc.EntityTNTPrimedCQR;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileBubble;
import team.cqr.cqrepoured.objects.items.ItemSoulBottle;
import team.cqr.cqrepoured.objects.items.guns.ItemBubblePistol;

public class CQRDispenseBehaviors {

	private static final Random rng = new Random();

	public static void registerDispenseBehaviors() {
		// Bubble Gun
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(CQRItems.BUBBLE_PISTOL, DISPENSE_BEHAVIOR_BUBBLE_GUN);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(CQRItems.BUBBLE_RIFLE, DISPENSE_BEHAVIOR_BUBBLE_GUN);

		// Soul bottle
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(CQRItems.SOUL_BOTTLE, DISPENSE_BEHAVIOR_SOUL_BOTTLE);

		// CQR TNT
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(CQRBlocks.TNT), DISPENSE_BEHAVIOR_TNT_CQR);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FLINT_AND_STEEL, DISPENSE_BEHAVIOR_IGNITE_TNT_CQR);
	}

	public static final IBehaviorDispenseItem DISPENSE_BEHAVIOR_BUBBLE_GUN = (source, stack) -> {
		Vec3d velocity = new Vec3d(0, 0, 0);
		switch (source.getBlockState().getValue(BlockDispenser.FACING)) {
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
	};

	public static final IBehaviorDispenseItem DISPENSE_BEHAVIOR_SOUL_BOTTLE = (source, stack) -> {
		Vec3d velocity = new Vec3d(0, 0, 0);
		switch (source.getBlockState().getValue(BlockDispenser.FACING)) {
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
	};

	public static final IBehaviorDispenseItem DISPENSE_BEHAVIOR_TNT_CQR = (source, stack) -> {
		World world = source.getWorld();
		BlockPos blockpos = source.getBlockPos().offset((EnumFacing) source.getBlockState().getValue(BlockDispenser.FACING));
		EntityTNTPrimedCQR entitytntprimed = new EntityTNTPrimedCQR(world, (double) blockpos.getX() + 0.5D, (double) blockpos.getY(), (double) blockpos.getZ() + 0.5D, (EntityLivingBase) null);
		world.spawnEntity(entitytntprimed);
		world.playSound((EntityPlayer) null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		stack.shrink(1);
		return stack;
	};
	
	public static final BehaviorDispenseOptional DISPENSE_BEHAVIOR_IGNITE_TNT_CQR =new BehaviorDispenseOptional() {
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			World world = source.getWorld();
	        this.successful = true;
	        BlockPos blockpos = source.getBlockPos().offset((EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING));

	        if (world.isAirBlock(blockpos))
	        {
	            world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());

	            if (stack.attemptDamageItem(1, world.rand, (EntityPlayerMP)null))
	            {
	                stack.setCount(0);
	            }
	        }
	        else if (world.getBlockState(blockpos).getBlock() == CQRBlocks.TNT)
	        {
	        	CQRBlocks.TNT.onPlayerDestroy(world, blockpos, CQRBlocks.TNT.getDefaultState().withProperty(BlockTNTCQR.EXPLODE, Boolean.valueOf(true)));
	            world.setBlockToAir(blockpos);
	        }
	        else
	        {
	            this.successful = false;
	        }

	        return stack;
		}
	};

}
