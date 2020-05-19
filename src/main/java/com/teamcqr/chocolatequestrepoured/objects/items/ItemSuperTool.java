package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemSuperTool extends Item {

	private static final String MODE_TAG = "Mode";
	private static final String BLOCK_TAG = "Block";

	public ItemSuperTool() {
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (player.isCreative() && !(entity instanceof EntityPlayer)) {
			entity.onKillCommand();
			return true;
		}

		return false;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(TextFormatting.BLUE + "Mode: " + this.getModeName(this.getMode(stack)));
		tooltip.add(TextFormatting.BLUE + "Block: " + this.getBlock(stack).getLocalizedName());
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (!playerIn.isCreative()) {
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}

		if (playerIn.isSneaking()) {
			int mode = this.getMode(stack) + 1;

			if (mode < 0 || mode > 2) {
				mode = 0;
			}

			this.setMode(stack, mode);

			if (!worldIn.isRemote) {
				playerIn.sendStatusMessage(new TextComponentString("Pickaxe Mode: " + this.getModeName(mode)), true);
			}

			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}

		return new ActionResult<>(EnumActionResult.FAIL, stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);

		if (!player.isCreative()) {
			return EnumActionResult.PASS;
		}

		if (player.isSneaking()) {
			this.setBlock(stack, worldIn.getBlockState(pos).getBlock());
			return EnumActionResult.SUCCESS;
		}

		int size = 1;
		EnumFacing facing1;
		EnumFacing facing2;

		if (facing.getAxis() == EnumFacing.Axis.Y) {
			facing1 = EnumFacing.NORTH;
			facing2 = EnumFacing.EAST;
		} else {
			facing1 = EnumFacing.UP;
			facing2 = facing.rotateY();
		}

		for (int i = -size; i <= size; i++) {
			for (int j = -size; j <= size; j++) {
				this.performAction(worldIn, pos.offset(facing1, i).offset(facing2, j), stack);
			}
		}

		return EnumActionResult.SUCCESS;
	}

	private void performAction(World worldIn, BlockPos pos, ItemStack stack) {
		int mode = this.getMode(stack);

		if (mode == 0) {
			worldIn.setBlockState(pos, this.getBlock(stack).getDefaultState());
		} else if (mode == 1) {
			Block block = worldIn.getBlockState(pos).getBlock();

			if (block != Blocks.AIR) {
				worldIn.setBlockState(pos, this.getBlock(stack).getDefaultState());
			}
		} else if (mode == 2) {
			Block block = worldIn.getBlockState(pos).getBlock();

			if (block != Blocks.AIR) {
				worldIn.setBlockToAir(pos);

				for (int i = 0; i < 5; i++) {
					worldIn.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0, 0, 0, Block.getIdFromBlock(block));
				}
			}
		}
	}

	public int getMode(ItemStack stack) {
		NBTTagCompound stackTag = stack.getTagCompound();

		if (stackTag == null) {
			stackTag = new NBTTagCompound();
			stack.setTagCompound(stackTag);
		}

		int mode = stackTag.getInteger(MODE_TAG);

		if (mode < 0 || mode > 2) {
			mode = 0;
			this.setMode(stack, mode);
		}

		return mode;
	}

	public Block getBlock(ItemStack stack) {
		NBTTagCompound stackTag = stack.getTagCompound();

		if (stackTag == null) {
			stackTag = new NBTTagCompound();
			stack.setTagCompound(stackTag);
		}

		Block block = Block.REGISTRY.getObject(new ResourceLocation(stackTag.getString(BLOCK_TAG)));

		return block != Blocks.AIR ? block : Blocks.STONE;
	}

	public void setMode(ItemStack stack, int mode) {
		NBTTagCompound stackTag = stack.getTagCompound();

		if (stackTag == null) {
			stackTag = new NBTTagCompound();
			stack.setTagCompound(stackTag);
		}

		if (mode < 0 || mode > 2) {
			mode = 0;
		}

		stackTag.setInteger(MODE_TAG, mode);
	}

	public void setBlock(ItemStack stack, Block blockIn) {
		NBTTagCompound stackTag = stack.getTagCompound();

		if (stackTag == null) {
			stackTag = new NBTTagCompound();
			stack.setTagCompound(stackTag);
		}

		stackTag.setString(BLOCK_TAG, blockIn.getRegistryName().toString());
	}

}
