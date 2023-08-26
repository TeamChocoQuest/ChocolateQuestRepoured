package team.cqr.cqrepoured.init;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.TNTBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.misc.EntityTNTPrimedCQR;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBubble;
import team.cqr.cqrepoured.item.ItemSoulBottle;
import team.cqr.cqrepoured.item.gun.ItemBubblePistol;

public class CQRDispenseBehaviors {

	private static final Random rng = new Random();

	public static void registerDispenseBehaviors() {
		// Bubble Gun
		DispenserBlock.registerBehavior(CQRItems.BUBBLE_PISTOL.get(), DISPENSE_BEHAVIOR_BUBBLE_GUN);
		DispenserBlock.registerBehavior(CQRItems.BUBBLE_RIFLE.get(), DISPENSE_BEHAVIOR_BUBBLE_GUN);

		// Soul bottle
		DispenserBlock.registerBehavior(CQRItems.SOUL_BOTTLE.get(), DISPENSE_BEHAVIOR_SOUL_BOTTLE);

		// CQR TNT
		DispenserBlock.registerBehavior(CQRBlocks.TNT.get(), DISPENSE_BEHAVIOR_TNT_CQR);
		//DispenserBlock.registerBehavior(Items.FLINT_AND_STEEL, DISPENSE_BEHAVIOR_IGNITE_TNT_CQR);
	}

	public static final IDispenseItemBehavior DISPENSE_BEHAVIOR_BUBBLE_GUN = new DefaultDispenseItemBehavior() {
		
		protected ItemStack execute(IBlockSource source, ItemStack stack) {
			Vec3 velocity = new Vec3(0, 0, 0);
			switch (source.getBlockState().getValue(DispenserBlock.FACING)) {
			case DOWN:
				velocity = new Vec3(0, -1, 0);
				break;
			case EAST:
				velocity = new Vec3(1, 0, 0);
				break;
			case NORTH:
				velocity = new Vec3(0, 0, -1);
				break;
			case SOUTH:
				velocity = new Vec3(0, 0, 1);
				break;
			case UP:
				velocity = new Vec3(0, 1, 0);
				break;
			case WEST:
				velocity = new Vec3(-1, 0, 0);
				break;
			default:
				break;
	
			}
			IPosition disPos = DispenserBlock.getDispensePosition(source);
			Vec3 startLoc = new Vec3(disPos.x(), disPos.y(), disPos.z());
			Item item = stack.getItem();
			double acc = 0.5D;
			if (item instanceof ItemBubblePistol) {
				ItemBubblePistol pistol = (ItemBubblePistol) item;
				acc = pistol.getInaccurary();
			}
			Vec3 v = new Vec3(-acc + velocity.x + (2 * acc * rng.nextDouble()), -acc + velocity.y + (2 * acc * rng.nextDouble()), -acc + velocity.z + (2 * acc * rng.nextDouble()));
			v = v.normalize();
			v = v.scale(1.4);
	
			ProjectileBubble bubble = new ProjectileBubble(startLoc.x, startLoc.y, startLoc.z, source.getLevel());
			/*bubble.motionX = v.x;
			bubble.motionY = v.y;
			bubble.motionZ = v.z;
			bubble.velocityChanged = true;*/
			bubble.setDeltaMovement(v);
			source.getLevel().addFreshEntity(bubble);
	
			source.getLevel().playLocalSound(disPos.x(), disPos.y(), disPos.z(), CQRSounds.BUBBLE_BUBBLE, SoundSource.BLOCKS, 1, 0.75F + (0.5F * rng.nextFloat()), false);
	
			// DONE: FIgure out how to make the stack damaged
			if (stack.hurt(1, source.getLevel().random, (ServerPlayer) null)) {
				stack.setCount(0);
			}
			return stack;
		}
	};

	public static final IDispenseItemBehavior DISPENSE_BEHAVIOR_SOUL_BOTTLE = new DefaultDispenseItemBehavior() {
		
		protected ItemStack execute(IBlockSource source, ItemStack stack) {
			Vec3 velocity = new Vec3(0, 0, 0);
			switch (source.getBlockState().getValue(DispenserBlock.FACING)) {
			case DOWN:
				velocity = new Vec3(0, -1, 0);
				break;
			case EAST:
				velocity = new Vec3(1, 0, 0);
				break;
			case NORTH:
				velocity = new Vec3(0, 0, -1);
				break;
			case SOUTH:
				velocity = new Vec3(0, 0, 1);
				break;
			case UP:
				velocity = new Vec3(0, 2, 0);
				break;
			case WEST:
				velocity = new Vec3(-1, 0, 0);
				break;
			default:
				break;
	
			}
			IPosition disPos = DispenserBlock.getDispensePosition(source);
			Vec3 pos = new Vec3(disPos.x(), disPos.y(), disPos.z()).add(velocity);
	
			if (stack.hasTag()) {
				CompoundTag bottle = stack.getTag();
	
				if (bottle.contains(ItemSoulBottle.ENTITY_IN_TAG)) {
					if (!source.getLevel().isClientSide) {
						CompoundTag entityTag = (CompoundTag) bottle.get(ItemSoulBottle.ENTITY_IN_TAG);
						((ItemSoulBottle) stack.getItem()).createEntityFromNBT(entityTag, source.getLevel(), pos.x, pos.y, pos.z);
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

	public static final IDispenseItemBehavior DISPENSE_BEHAVIOR_TNT_CQR = new DefaultDispenseItemBehavior() {
		
		protected ItemStack execute(IBlockSource source, ItemStack stack) {
			Level world = source.getLevel();
			BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
			EntityTNTPrimedCQR entitytntprimed = new EntityTNTPrimedCQR(world, blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D, (LivingEntity) null);
			world.addFreshEntity(entitytntprimed);
			world.playSound((Player)null, entitytntprimed.getX(), entitytntprimed.getY(), entitytntprimed.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
			stack.shrink(1);
			return stack;
		}
		
	};

	public static final IDispenseItemBehavior DISPENSE_BEHAVIOR_IGNITE_TNT_CQR = new OptionalDispenseBehavior() {
		/*
		 *             World world = source.getWorld();
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
		 */
        /**
         * Dispense the specified stack, play the dispense sound and spawn particles.
         */
        protected ItemStack execute(IBlockSource pSource, ItemStack pStack) {
           Level world = pSource.getLevel();
           this.setSuccess(true);
           Direction direction = pSource.getBlockState().getValue(DispenserBlock.FACING);
           BlockPos blockpos = pSource.getPos().relative(direction);
           BlockState blockstate = world.getBlockState(blockpos);
           if (AbstractFireBlock.canBePlacedAt(world, blockpos, direction)) {
              world.setBlockAndUpdate(blockpos, AbstractFireBlock.getState(world, blockpos));
           } else if (CampfireBlock.canLight(blockstate)) {
              world.setBlockAndUpdate(blockpos, blockstate.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)));
           } else if (blockstate.isFlammable(world, blockpos, pSource.getBlockState().getValue(DispenserBlock.FACING).getOpposite())) {
              blockstate.catchFire(world, blockpos, pSource.getBlockState().getValue(DispenserBlock.FACING).getOpposite(), null);
              if (blockstate.getBlock() instanceof TNTBlock)
              world.removeBlock(blockpos, false);
           } else {
              this.setSuccess(false);
           }

           if (this.isSuccess() && pStack.hurt(1, world.random, (ServerPlayer)null)) {
              pStack.setCount(0);
           }

           return pStack;
        }
     };

}
