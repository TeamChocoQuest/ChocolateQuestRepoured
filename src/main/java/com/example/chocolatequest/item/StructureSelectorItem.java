package com.example.chocolatequest.item;

import com.example.chocolatequest.block.entity.ExporterBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

/** Selects two structure corners and transfers them to an Exporter block. */
public class StructureSelectorItem extends Item {
    private static final String FIRST_TAG = "StructureFirstPos";
    private static final String SECOND_TAG = "StructureSecondPos";

    public StructureSelectorItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !player.isCreative()) return InteractionResult.PASS;
        if (context.getLevel().isClientSide) return InteractionResult.SUCCESS;

        ItemStack stack = context.getItemInHand();
        BlockPos clicked = context.getClickedPos();
        if (context.getLevel().getBlockEntity(clicked) instanceof ExporterBlockEntity exporter) {
            BlockPos first = getFirst(stack);
            BlockPos second = getSecond(stack);
            if (first == null || second == null) {
                player.displayClientMessage(Component.translatable("message.cqrepoured.structure_selector.missing"), true);
                return InteractionResult.SUCCESS;
            }

            if (exporter.isRelativeMode()) {
                first = first.subtract(clicked);
                second = second.subtract(clicked);
            }
            exporter.setValues(exporter.getStructureName(), first.getX(), first.getY(), first.getZ(),
                    second.getX(), second.getY(), second.getZ(), exporter.isRelativeMode(), exporter.isIgnoreEntities());
            player.displayClientMessage(Component.translatable("message.cqrepoured.structure_selector.applied"), true);
        } else {
            BlockPos second = player.isCrouching() ? player.blockPosition() : clicked;
            setPosition(stack, SECOND_TAG, second);
            player.displayClientMessage(Component.translatable("message.cqrepoured.structure_selector.second", format(second)), true);
        }
        return InteractionResult.SUCCESS;
    }

    public static void setFirst(ItemStack stack, BlockPos pos) {
        setPosition(stack, FIRST_TAG, pos);
    }

    public static BlockPos getFirst(ItemStack stack) {
        return getPosition(stack, FIRST_TAG);
    }

    public static BlockPos getSecond(ItemStack stack) {
        return getPosition(stack, SECOND_TAG);
    }

    private static CompoundTag getData(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? new CompoundTag() : data.copyTag();
    }

    private static void setPosition(ItemStack stack, String key, BlockPos pos) {
        CompoundTag data = getData(stack);
        data.putLong(key, pos.asLong());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
    }

    private static BlockPos getPosition(ItemStack stack, String key) {
        CompoundTag data = getData(stack);
        return data.contains(key) ? BlockPos.of(data.getLong(key)) : null;
    }

    private static String format(BlockPos pos) {
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        BlockPos first = getFirst(stack);
        BlockPos second = getSecond(stack);
        tooltip.add(Component.translatable("item.cqrepoured.structure_selector.tooltip").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("item.cqrepoured.structure_selector.first", first == null ? "--" : format(first)).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.cqrepoured.structure_selector.second", second == null ? "--" : format(second)).withStyle(ChatFormatting.GRAY));
    }
}
