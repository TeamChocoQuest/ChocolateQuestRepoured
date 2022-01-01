package team.cqr.cqrepoured.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class ItemStructureSelector extends Item {

	public ItemStructureSelector() {
		this.setMaxStackSize(1);
	}

	@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, PlayerEntity player) {
		Block block = world.getBlockState(pos).getBlock();
		return block != CQRBlocks.EXPORTER;
	}

	@Override
	public ActionResultType onItemUseFirst(PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (world.getTileEntity(pos) instanceof TileEntityExporter) {
			if (world.isRemote) {
				if (!this.hasFirstAndSecondPos(stack)) {
					player.sendMessage(new StringTextComponent("Set both positions before using on a exporter"));
					return ActionResultType.SUCCESS;
				}

				TileEntityExporter tileEntity = (TileEntityExporter) world.getTileEntity(pos);
				BlockPos pos1 = this.getFirstPos(stack);
				BlockPos pos2 = this.getSecondPos(stack);

				if (tileEntity.isRelativeMode()) {
					pos1 = pos1.subtract(pos);
					pos2 = pos2.subtract(pos);
				}

				tileEntity.setValues(tileEntity.getStructureName(), DungeonGenUtils.getMinPos(pos1, pos2), DungeonGenUtils.getMaxPos(pos1, pos2), tileEntity.isRelativeMode(), tileEntity.isIgnoreEntities(), tileEntity.getUnprotectedBlocks());
			}
		} else if (!world.isRemote) {
			if (player.isSneaking()) {
				BlockPos pos1 = new BlockPos(player);
				this.setSecondPos(stack, pos1);
				player.sendMessage(new StringTextComponent("Second position set to " + pos1));
			} else {
				this.setSecondPos(stack, pos);
				player.sendMessage(new StringTextComponent("Second position set to " + pos));
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (!playerIn.world.isRemote && playerIn.isSneaking()) {
			BlockPos pos1 = new BlockPos(playerIn);
			this.setSecondPos(stack, pos1);
			playerIn.sendMessage(new StringTextComponent("Second position set to " + pos1));
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		BlockPos pos1 = this.getFirstPos(stack);
		BlockPos pos2 = this.getSecondPos(stack);

		if (pos1 != null) {
			tooltip.add("First position: " + pos1);
		} else {
			tooltip.add("First position: not set");
		}
		if (pos2 != null) {
			tooltip.add("Second position: " + pos2);
		} else {
			tooltip.add("Second position: not set");
		}
	}

	public void setFirstPos(ItemStack stack, BlockPos pos) {
		CompoundNBT compound = stack.getTagCompound();
		if (compound == null) {
			compound = new CompoundNBT();
			stack.setTagCompound(compound);
		}
		compound.setTag("pos1", NBTUtil.createPosTag(pos));
	}

	public void setSecondPos(ItemStack stack, BlockPos pos) {
		CompoundNBT compound = stack.getTagCompound();
		if (compound == null) {
			compound = new CompoundNBT();
			stack.setTagCompound(compound);
		}
		compound.setTag("pos2", NBTUtil.createPosTag(pos));
	}

	public BlockPos getFirstPos(ItemStack stack) {
		CompoundNBT compound = stack.getTagCompound();
		if (compound == null || !compound.hasKey("pos1", Constants.NBT.TAG_COMPOUND)) {
			return null;
		}
		return NBTUtil.getPosFromTag(compound.getCompoundTag("pos1"));
	}

	public BlockPos getSecondPos(ItemStack stack) {
		CompoundNBT compound = stack.getTagCompound();
		if (compound == null || !compound.hasKey("pos2", Constants.NBT.TAG_COMPOUND)) {
			return null;
		}
		return NBTUtil.getPosFromTag(compound.getCompoundTag("pos2"));
	}

	public boolean hasFirstPos(ItemStack stack) {
		return this.getFirstPos(stack) != null;
	}

	public boolean hasSecondPos(ItemStack stack) {
		return this.getSecondPos(stack) != null;
	}

	public boolean hasFirstAndSecondPos(ItemStack stack) {
		return this.hasFirstPos(stack) && this.hasSecondPos(stack);
	}

}
