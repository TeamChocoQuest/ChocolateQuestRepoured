package team.cqr.cqrepoured.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemSuperTool extends Item {

	private static final String MODE_TAG = "Mode";
	private static final String BLOCK_TAG = "Block";

	public ItemSuperTool(Properties properties)
	{
		super(properties.stacksTo(1));
		//Max stack size 1 //Move
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		if (player.isCreative() && !(entity instanceof PlayerEntity)) {
			entity.kill();
			return true;
		}

		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(new StringTextComponent(TextFormatting.BLUE + "Mode: " + this.getModeName(this.getMode(stack))));
		tooltip.add(new StringTextComponent(TextFormatting.BLUE + "Block: " + this.getBlock(stack).getName()));
	}

	public String getModeName(int mode) {
		if (mode == 0) {
			return "Build";
		} else if (mode == 1) {
			return "Fill";
		} else if (mode == 2) {
			return "Mine";
		}

		return "";
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (!playerIn.isCreative()) {
			return new ActionResult<>(ActionResultType.FAIL, stack);
		}

		if (playerIn.isCrouching()) {
			int mode = this.getMode(stack) + 1;

			if (mode < 0 || mode > 2) {
				mode = 0;
			}

			this.setMode(stack, mode);

			if (!worldIn.isClientSide) {
				playerIn.displayClientMessage(new StringTextComponent("Pickaxe Mode: " + this.getModeName(mode)), true);
			}

			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		}

		return new ActionResult<>(ActionResultType.FAIL, stack);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context)
	{
		PlayerEntity player = context.getPlayer();
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Direction facing = context.getClickedFace();
		ItemStack stack = context.getItemInHand();
		//ItemStack stack = player.getItemInHand(hand);

		if (!player.isCreative()) {
			return ActionResultType.PASS;
		}

		if (player.isCrouching()) {
			this.setBlock(stack, world.getBlockState(pos).getBlock());
			return ActionResultType.SUCCESS;
		}

		int size = 1;
		Direction facing1;
		Direction facing2;

		if (facing.getAxis() == Direction.Axis.Y) {
			facing1 = Direction.NORTH;
			facing2 = Direction.EAST;
		} else {
			facing1 = Direction.UP;
			facing2 = facing.getClockWise();
		}

		for (int i = -size; i <= size; i++) {
			for (int j = -size; j <= size; j++) {
				this.performAction(world, pos.relative(facing1, i).relative(facing2, j), stack);
			}
		}

		return ActionResultType.SUCCESS;
	}

	private void performAction(World worldIn, BlockPos pos, ItemStack stack) {
		int mode = this.getMode(stack);

		if (mode == 0) {
			worldIn.setBlockAndUpdate(pos, this.getBlock(stack).defaultBlockState());
		} else if (mode == 1) {
			Block block = worldIn.getBlockState(pos).getBlock();

			if (block != Blocks.AIR) {
				worldIn.setBlockAndUpdate(pos, this.getBlock(stack).defaultBlockState());
			}
		} else if (mode == 2) {
			Block block = worldIn.getBlockState(pos).getBlock();

			if (block != Blocks.AIR) {
				worldIn.removeBlock(pos, false);

				for (int i = 0; i < 5; i++) {
					worldIn.addParticle(new BlockParticleData(ParticleTypes.BLOCK, block.defaultBlockState()), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0, 0, 0);
				}
			}
		}
	}

	public int getMode(ItemStack stack) {
		CompoundNBT stackTag = stack.getTag();

		if (stackTag == null) {
			stackTag = new CompoundNBT();
			stack.setTag(stackTag);
		}

		int mode = stackTag.getInt(MODE_TAG);

		if (mode < 0 || mode > 2) {
			mode = 0;
			this.setMode(stack, mode);
		}

		return mode;
	}

	public Block getBlock(ItemStack stack) {
		CompoundNBT stackTag = stack.getTag();

		if (stackTag == null) {
			stackTag = new CompoundNBT();
			stack.setTag(stackTag);
		}

		Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stackTag.getString(BLOCK_TAG)));
		//Block block = Block.REGISTRY.getObject(new ResourceLocation(stackTag.getString(BLOCK_TAG)));

		return block != Blocks.AIR ? block : Blocks.STONE;
	}

	public void setMode(ItemStack stack, int mode) {
		CompoundNBT stackTag = stack.getTag();

		if (stackTag == null) {
			stackTag = new CompoundNBT();
			stack.setTag(stackTag);
		}

		if (mode < 0 || mode > 2) {
			mode = 0;
		}

		stackTag.putInt(MODE_TAG, mode);
	}

	public void setBlock(ItemStack stack, Block blockIn) {
		CompoundNBT stackTag = stack.getTag();

		if (stackTag == null) {
			stackTag = new CompoundNBT();
			stack.setTag(stackTag);
		}

		stackTag.putString(BLOCK_TAG, blockIn.getRegistryName().toString());
	}

}
