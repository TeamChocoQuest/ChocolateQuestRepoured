package com.example.chocolatequest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class SuperToolItem extends Item {
    private static final String MODE_TAG = "SuperToolMode";
    private static final String BLOCK_TAG = "SuperToolBlock";

    public SuperToolItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isCreative() || !player.isCrouching()) {
            return InteractionResultHolder.pass(stack);
        }

        int mode = (getMode(stack) + 1) % 3;
        setInt(stack, MODE_TAG, mode);
        if (!level.isClientSide) {
            player.displayClientMessage(Component.translatable("message.cqrepoured.super_tool.mode", modeName(mode)), true);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !player.isCreative()) return InteractionResult.PASS;

        ItemStack stack = context.getItemInHand();
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        if (player.isCrouching()) {
            Block selected = level.getBlockState(clickedPos).getBlock();
            setString(stack, BLOCK_TAG, BuiltInRegistries.BLOCK.getKey(selected).toString());
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("message.cqrepoured.super_tool.block", selected.getName()), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!level.isClientSide) {
            Direction face = context.getClickedFace();
            Direction first = face.getAxis() == Direction.Axis.Y ? Direction.NORTH : Direction.UP;
            Direction second = face.getAxis() == Direction.Axis.Y ? Direction.EAST : face.getClockWise();
            BlockPos center = getMode(stack) == 0 ? clickedPos.relative(face) : clickedPos;
            Block replacement = getSelectedBlock(stack);

            for (int a = -1; a <= 1; a++) {
                for (int b = -1; b <= 1; b++) {
                    edit(level, center.relative(first, a).relative(second, b), replacement, getMode(stack));
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void edit(Level level, BlockPos pos, Block replacement, int mode) {
        if (!level.isInWorldBounds(pos)) return;
        if (mode == 0) {
            level.setBlock(pos, replacement.defaultBlockState(), Block.UPDATE_ALL);
        } else if (mode == 1) {
            if (!level.isEmptyBlock(pos)) level.setBlock(pos, replacement.defaultBlockState(), Block.UPDATE_ALL);
        } else if (!level.isEmptyBlock(pos)) {
            level.destroyBlock(pos, false);
        }
    }

    public static int getMode(ItemStack stack) {
        CompoundTag tag = getData(stack);
        return Math.max(0, Math.min(2, tag.getInt(MODE_TAG)));
    }

    private static Block getSelectedBlock(ItemStack stack) {
        ResourceLocation id = ResourceLocation.tryParse(getData(stack).getString(BLOCK_TAG));
        return id != null && BuiltInRegistries.BLOCK.containsKey(id) ? BuiltInRegistries.BLOCK.get(id) : Blocks.STONE;
    }

    private static Component modeName(int mode) {
        return Component.translatable("item.cqrepoured.super_tool.mode." + mode);
    }

    private static CompoundTag getData(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? new CompoundTag() : data.copyTag();
    }

    private static void setInt(ItemStack stack, String key, int value) {
        CompoundTag tag = getData(stack);
        tag.putInt(key, value);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private static void setString(ItemStack stack, String key, String value) {
        CompoundTag tag = getData(stack);
        tag.putString(key, value);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.cqrepoured.super_tool.tooltip").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("item.cqrepoured.super_tool.current_mode", modeName(getMode(stack))).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.cqrepoured.super_tool.current_block", getSelectedBlock(stack).getName()).withStyle(ChatFormatting.GRAY));
    }
}
