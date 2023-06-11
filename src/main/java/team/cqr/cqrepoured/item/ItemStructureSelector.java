package team.cqr.cqrepoured.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Constants.NBT;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.Translator;

public class ItemStructureSelector extends Item {

	public ItemStructureSelector(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();

		if (world.getBlockEntity(pos) instanceof TileEntityExporter) {
			if (world.isClientSide) {
				if (!this.hasFirstPos(stack) || !this.hasSecondPos(stack)) {
					player.sendMessage(Translator.translateItem(this, ".error_set_both_before_use").withStyle(TextFormatting.RED), null);
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
			this.setSecondPos(stack, player.isCrouching() ? player.blockPosition() : pos, player);
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (!playerIn.level.isClientSide && playerIn.isCrouching()) {
			this.setSecondPos(stack, playerIn.blockPosition(), playerIn);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (this.hasFirstPos(stack)) {
			BlockPos pos = this.getFirstPos(stack);
			tooltip.add(Translator.translateItem(this, ".tooltip_first_set", pos.getX(), pos.getY(), pos.getZ()).withStyle(TextFormatting.BLUE));
		} else {
			tooltip.add(Translator.translateItem(this, ".tooltip_first_unkown").withStyle(TextFormatting.BLUE));
		}

		if (this.hasSecondPos(stack)) {
			BlockPos pos = this.getSecondPos(stack);
			tooltip.add(Translator.translateItem(this, ".tooltip_second_set", pos.getX(), pos.getY(), pos.getZ()).withStyle(TextFormatting.BLUE));
		} else {
			tooltip.add(Translator.translateItem(this, ".tooltip_second_unkown").withStyle(TextFormatting.BLUE));
		}
	}

	public void setFirstPos(ItemStack stack, BlockPos pos, @Nullable PlayerEntity player) {
		stack.addTagElement("pos1", NBTUtil.writeBlockPos(pos));
		player.sendMessage(Translator.translateItem(this, ".set_first", pos.getX(), pos.getY(), pos.getZ()), null);
	}

	public void setSecondPos(ItemStack stack, BlockPos pos, @Nullable PlayerEntity player) {
		stack.addTagElement("pos2", NBTUtil.writeBlockPos(pos));
		player.sendMessage(Translator.translateItem(this, ".set_second", pos.getX(), pos.getY(), pos.getZ()), null);
	}

	public BlockPos getFirstPos(ItemStack stack) {
		CompoundNBT compound = stack.getTag();
		if (compound == null || !compound.contains("pos1", NBT.TAG_COMPOUND)) {
			return null;
		}
		return NBTUtil.readBlockPos(compound.getCompound("pos1"));
	}

	public BlockPos getSecondPos(ItemStack stack) {
		CompoundNBT compound = stack.getTag();
		if (compound == null || !compound.contains("pos2", NBT.TAG_COMPOUND)) {
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

}
