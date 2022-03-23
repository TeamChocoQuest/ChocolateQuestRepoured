package team.cqr.cqrepoured.item;

import com.google.common.collect.Streams;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

public class ItemUnprotectedPositionTool extends ItemLore {

	private static final String POSITIONS_NBT_KEY = "positions";

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

	@Override
	public ActionResultType onItemUseFirst(PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!this.removePosition(stack, pos)) {
			this.addPosition(stack, pos);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (playerIn.isSneaking()) {
			ItemStack stack = playerIn.getHeldItem(handIn);
			this.clearPositions(stack);
			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
		TileEntity tileEntity = player.world.getTileEntity(pos);
		if (!(tileEntity instanceof TileEntityExporter)) {
			return super.onBlockStartBreak(itemstack, pos, player);
		}
		TileEntityExporter exporter = (TileEntityExporter) tileEntity;
		if (!player.isSneaking()) {
			exporter.setUnprotectedBlocks(this.getPositions(itemstack).toArray(BlockPos[]::new));
		} else {
			BlockPos[] exporterPositions = exporter.getUnprotectedBlocks();
			this.clearPositions(itemstack);
			Arrays.stream(exporterPositions).forEach(p -> this.addPosition(itemstack, p));
		}
		return true;
	}

	public boolean hasPosition(ItemStack stack, BlockPos pos) {
		return this.getPositions(stack).anyMatch(pos::equals);
	}

	public void addPosition(ItemStack stack, BlockPos pos) {
		int[] data = new int[] { pos.getX(), pos.getY(), pos.getZ() };
		this.getOrCreatePositionTagList(stack).appendTag(new IntArrayNBT(data));
	}

	public boolean removePosition(ItemStack stack, BlockPos pos) {
		ListNBT tagList = this.getPositionTagList(stack);
		if (tagList == null) {
			return false;
		}
		Iterator<INBT> iterator = tagList.iterator();
		while (iterator.hasNext()) {
			INBT tag = iterator.next();
			int[] data = ((IntArrayNBT) tag).getIntArray();
			if (data[0] == pos.getX() && data[1] == pos.getY() && data[2] == pos.getZ()) {
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	public void clearPositions(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return;
		}
		CompoundNBT nbt = stack.getTagCompound();
		nbt.removeTag(POSITIONS_NBT_KEY);
	}

	public Stream<BlockPos> getPositions(ItemStack stack) {
		ListNBT tagList = this.getPositionTagList(stack);
		if (tagList == null) {
			return Stream.empty();
		}
		return Streams.stream(tagList).map(tag -> {
			int[] data = ((IntArrayNBT) tag).getIntArray();
			return new BlockPos(data[0], data[1], data[2]);
		});
	}

	@Nullable
	private ListNBT getPositionTagList(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return null;
		}
		CompoundNBT nbt = stack.getTagCompound();
		if (!nbt.hasKey(POSITIONS_NBT_KEY, Constants.NBT.TAG_LIST)) {
			return null;
		}
		return nbt.getTagList(POSITIONS_NBT_KEY, Constants.NBT.TAG_INT_ARRAY);
	}

	private ListNBT getOrCreatePositionTagList(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new CompoundNBT());
		}
		CompoundNBT nbt = stack.getTagCompound();
		if (!nbt.hasKey(POSITIONS_NBT_KEY, Constants.NBT.TAG_LIST)) {
			nbt.setTag(POSITIONS_NBT_KEY, new ListNBT());
		}
		return nbt.getTagList(POSITIONS_NBT_KEY, Constants.NBT.TAG_INT_ARRAY);
	}

}
