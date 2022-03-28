package team.cqr.cqrepoured.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;
import team.cqr.cqrepoured.util.DungeonGenUtils;

import java.util.List;

public class ItemStructureSelector extends Item {

	public ItemStructureSelector(Properties properties)
	{
		super(properties.stacksTo(1));
		//this.setMaxStackSize(1);
	}

	@Override
	public boolean canAttackBlock(BlockState state, World level, BlockPos pos, PlayerEntity player) {
		Block block = level.getBlockState(pos).getBlock();
		return block != CQRBlocks.EXPORTER.get();
	}

	@Override
	public ActionResultType useOn(ItemUseContext pContext) {
		PlayerEntity player = pContext.getPlayer();
		Hand hand = pContext.getHand();
		World world = pContext.getLevel();
		BlockPos pos = pContext.getClickedPos();

		ItemStack stack = player.getItemInHand(hand);

		if (world.getBlockEntity(pos) instanceof TileEntityExporter) {
			if (world.isClientSide) {
				if (!this.hasFirstAndSecondPos(stack)) {
					player.sendMessage(new StringTextComponent("Set both positions before using on a exporter"), player.getUUID());
					return ActionResultType.SUCCESS;
				}

				TileEntityExporter tileEntity = (TileEntityExporter) world.getBlockEntity(pos);
				BlockPos pos1 = this.getFirstPos(stack);
				BlockPos pos2 = this.getSecondPos(stack);

				if (tileEntity.isRelativeMode()) {
					pos1 = pos1.subtract(pos);
					pos2 = pos2.subtract(pos);
				}

				tileEntity.setValues(tileEntity.getStructureName(), DungeonGenUtils.getMinPos(pos1, pos2), DungeonGenUtils.getMaxPos(pos1, pos2), tileEntity.isRelativeMode(), tileEntity.isIgnoreEntities(), tileEntity.getUnprotectedBlocks());
			}
		} else if (!world.isClientSide) {
			if (player.isCrouching()) {
				BlockPos pos1 = player.blockPosition();
				this.setSecondPos(stack, pos1);
				player.sendMessage(new StringTextComponent("Second position set to " + pos1), player.getUUID());
			} else {
				this.setSecondPos(stack, pos);
				player.sendMessage(new StringTextComponent("Second position set to " + pos), player.getUUID());
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (!playerIn.level.isClientSide && playerIn.isCrouching()) {
			BlockPos pos1 = playerIn.blockPosition();
			this.setSecondPos(stack, pos1);
			playerIn.sendMessage(new StringTextComponent("Second position set to " + pos1), playerIn.getUUID());
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		BlockPos pos1 = this.getFirstPos(stack);
		BlockPos pos2 = this.getSecondPos(stack);

		if (pos1 != null) {
			tooltip.add(new StringTextComponent("First position: " + pos1));
		} else {
			tooltip.add(new StringTextComponent("First position: not set"));
		}
		if (pos2 != null) {
			tooltip.add(new StringTextComponent("Second position: " + pos2));
		} else {
			tooltip.add(new StringTextComponent("Second position: not set"));
		}
	}

	public void setFirstPos(ItemStack stack, BlockPos pos) {
		CompoundNBT compound = stack.getTag();
		if (compound == null) {
			compound = new CompoundNBT();
			stack.setTag(compound);
		}
		compound.put("pos1", NBTUtil.writeBlockPos(pos));
	}

	public void setSecondPos(ItemStack stack, BlockPos pos) {
		CompoundNBT compound = stack.getTag();
		if (compound == null) {
			compound = new CompoundNBT();
			stack.setTag(compound);
		}
		compound.put("pos2", NBTUtil.writeBlockPos(pos));
	}

	public BlockPos getFirstPos(ItemStack stack) {
		CompoundNBT compound = stack.getTag();
		if (compound == null || !compound.contains("pos1", Constants.NBT.TAG_COMPOUND)) {
			return null;
		}
		return NBTUtil.readBlockPos(compound.getCompound("pos1"));
	}

	public BlockPos getSecondPos(ItemStack stack) {
		CompoundNBT compound = stack.getTag();
		if (compound == null || !compound.contains("pos2", Constants.NBT.TAG_COMPOUND)) {
			return null;
		}
		return NBTUtil.readBlockPos(compound.getCompound("pos2"));
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
