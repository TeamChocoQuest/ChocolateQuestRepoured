package team.cqr.cqrepoured.objects.items;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.collect.Streams;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;

public class ItemUnprotectedPositionTool extends ItemLore {

	private static final String POSITIONS_NBT_KEY = "positions";

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!this.removePosition(stack, pos)) {
			this.addPosition(stack, pos);
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (playerIn.isSneaking()) {
			ItemStack stack = playerIn.getHeldItem(handIn);
			this.clearPositions(stack);
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
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
		this.getOrCreatePositionTagList(stack).appendTag(new NBTTagIntArray(data));
	}

	public boolean removePosition(ItemStack stack, BlockPos pos) {
		NBTTagList tagList = this.getPositionTagList(stack);
		if (tagList == null) {
			return false;
		}
		Iterator<NBTBase> iterator = tagList.iterator();
		while (iterator.hasNext()) {
			NBTBase tag = iterator.next();
			int[] data = ((NBTTagIntArray) tag).getIntArray();
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
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.removeTag(POSITIONS_NBT_KEY);
	}

	public Stream<BlockPos> getPositions(ItemStack stack) {
		NBTTagList tagList = this.getPositionTagList(stack);
		if (tagList == null) {
			return Stream.empty();
		}
		return Streams.stream(tagList).map(tag -> {
			int[] data = ((NBTTagIntArray) tag).getIntArray();
			return new BlockPos(data[0], data[1], data[2]);
		});
	}

	@Nullable
	private NBTTagList getPositionTagList(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return null;
		}
		NBTTagCompound nbt = stack.getTagCompound();
		if (!nbt.hasKey(POSITIONS_NBT_KEY, Constants.NBT.TAG_LIST)) {
			return null;
		}
		return nbt.getTagList(POSITIONS_NBT_KEY, Constants.NBT.TAG_INT_ARRAY);
	}

	private NBTTagList getOrCreatePositionTagList(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = stack.getTagCompound();
		if (!nbt.hasKey(POSITIONS_NBT_KEY, Constants.NBT.TAG_LIST)) {
			nbt.setTag(POSITIONS_NBT_KEY, new NBTTagList());
		}
		return nbt.getTagList(POSITIONS_NBT_KEY, Constants.NBT.TAG_INT_ARRAY);
	}

}
