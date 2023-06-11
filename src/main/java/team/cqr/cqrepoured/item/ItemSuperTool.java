package team.cqr.cqrepoured.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ItemSuperTool extends Item {

	private static final String MODE_TAG = "Mode";
	private static final String BLOCK_TAG = "Block";

	public ItemSuperTool(Properties properties)
	{
		super(properties.stacksTo(1));
		//Max stack size 1 //Move
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (entity instanceof PartEntity<?>) {
			entity = ((PartEntity<?>) entity).getParent();
		}
		if (!player.level.isClientSide() && player.isCreative() && !(entity instanceof Player)) {
			entity.kill();
			return true;
		}

		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TextComponent("Mode: " + this.getModeName(this.getMode(stack))).withStyle(ChatFormatting.BLUE));
		//tooltip.add(new StringTextComponent("Block: " + this.getBlock(stack).asItem().toString()).withStyle(TextFormatting.BLUE));
		tooltip.add(new TextComponent("Block: " + this.getBlock(stack).getName().getString()).withStyle(ChatFormatting.BLUE));
	
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
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (!playerIn.isCreative()) {
			return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
		}

		if (playerIn.isCrouching()) {
			int mode = this.getMode(stack) + 1;

			if (mode < 0 || mode > 2) {
				mode = 0;
			}

			this.setMode(stack, mode);

			if (!worldIn.isClientSide) {
				playerIn.displayClientMessage(new TextComponent("Pickaxe Mode: " + this.getModeName(mode)), true);
			}

			return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
		}

		return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Player player = context.getPlayer();
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Direction facing = context.getClickedFace();
		ItemStack stack = context.getItemInHand();
		//ItemStack stack = player.getItemInHand(hand);

		if (!player.isCreative()) {
			return InteractionResult.PASS;
		}

		if (player.isCrouching()) {
			this.setBlock(stack, world.getBlockState(pos).getBlock());
			return InteractionResult.SUCCESS;
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

		return InteractionResult.SUCCESS;
	}

	private void performAction(Level worldIn, BlockPos pos, ItemStack stack) {
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
		CompoundTag stackTag = stack.getTag();

		if (stackTag == null) {
			stackTag = new CompoundTag();
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
		CompoundTag stackTag = stack.getTag();

		if (stackTag == null) {
			stackTag = new CompoundTag();
			stack.setTag(stackTag);
		}

		Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stackTag.getString(BLOCK_TAG)));
		//Block block = Block.REGISTRY.getObject(new ResourceLocation(stackTag.getString(BLOCK_TAG)));

		return block != Blocks.AIR ? block : Blocks.STONE;
	}

	public void setMode(ItemStack stack, int mode) {
		CompoundTag stackTag = stack.getTag();

		if (stackTag == null) {
			stackTag = new CompoundTag();
			stack.setTag(stackTag);
		}

		if (mode < 0 || mode > 2) {
			mode = 0;
		}

		stackTag.putInt(MODE_TAG, mode);
	}

	public void setBlock(ItemStack stack, Block blockIn) {
		CompoundTag stackTag = stack.getTag();

		if (stackTag == null) {
			stackTag = new CompoundTag();
			stack.setTag(stackTag);
		}

		stackTag.putString(BLOCK_TAG, blockIn.getRegistryName().toString());
	}

}
