package team.cqr.cqrepoured.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Constants.NBT;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.Translator;

public class ItemStructureSelector extends Item {

	public ItemStructureSelector(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
		Player player = context.getPlayer();
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();

		if (world.getBlockEntity(pos) instanceof TileEntityExporter) {
			if (world.isClientSide) {
				if (!this.hasFirstPos(stack) || !this.hasSecondPos(stack)) {
					player.sendMessage(Translator.translateItem(this, ".error_set_both_before_use").withStyle(ChatFormatting.RED), null);
					return InteractionResult.SUCCESS;
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

		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (!playerIn.level.isClientSide && playerIn.isCrouching()) {
			this.setSecondPos(stack, playerIn.blockPosition(), playerIn);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		if (this.hasFirstPos(stack)) {
			BlockPos pos = this.getFirstPos(stack);
			tooltip.add(Translator.translateItem(this, ".tooltip_first_set", pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.BLUE));
		} else {
			tooltip.add(Translator.translateItem(this, ".tooltip_first_unkown").withStyle(ChatFormatting.BLUE));
		}

		if (this.hasSecondPos(stack)) {
			BlockPos pos = this.getSecondPos(stack);
			tooltip.add(Translator.translateItem(this, ".tooltip_second_set", pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.BLUE));
		} else {
			tooltip.add(Translator.translateItem(this, ".tooltip_second_unkown").withStyle(ChatFormatting.BLUE));
		}
	}

	public void setFirstPos(ItemStack stack, BlockPos pos, @Nullable Player player) {
		stack.addTagElement("pos1", NbtUtils.writeBlockPos(pos));
		player.sendMessage(Translator.translateItem(this, ".set_first", pos.getX(), pos.getY(), pos.getZ()), null);
	}

	public void setSecondPos(ItemStack stack, BlockPos pos, @Nullable Player player) {
		stack.addTagElement("pos2", NbtUtils.writeBlockPos(pos));
		player.sendMessage(Translator.translateItem(this, ".set_second", pos.getX(), pos.getY(), pos.getZ()), null);
	}

	public BlockPos getFirstPos(ItemStack stack) {
		CompoundTag compound = stack.getTag();
		if (compound == null || !compound.contains("pos1", NBT.TAG_COMPOUND)) {
			return null;
		}
		return NbtUtils.readBlockPos(compound.getCompound("pos1"));
	}

	public BlockPos getSecondPos(ItemStack stack) {
		CompoundTag compound = stack.getTag();
		if (compound == null || !compound.contains("pos2", NBT.TAG_COMPOUND)) {
			return null;
		}
		return NbtUtils.readBlockPos(compound.getCompound("pos2"));
	}

	public boolean hasFirstPos(ItemStack stack) {
		return this.getFirstPos(stack) != null;
	}

	public boolean hasSecondPos(ItemStack stack) {
		return this.getSecondPos(stack) != null;
	}

}
