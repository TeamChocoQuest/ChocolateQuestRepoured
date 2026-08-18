package com.example.chocolatequest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class PathToolItem extends Item {
    private static final String PATH_TAG = "CQRPathNodes";
    private static final int MAX_NODES = 64;

    public PathToolItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !player.isCreative()) return InteractionResult.PASS;
        ItemStack stack = context.getItemInHand();
        BlockPos node = context.getClickedPos().relative(context.getClickedFace());
        if (!context.getLevel().isClientSide) {
            List<BlockPos> nodes = getNodes(stack);
            if (player.isCrouching()) {
                boolean removed = nodes.remove(node);
                if (!removed && !nodes.isEmpty()) nodes.remove(nodes.size() - 1);
                setNodes(stack, nodes);
                player.displayClientMessage(Component.translatable("message.cqrepoured.path_tool.removed", nodes.size()), true);
            } else if (nodes.size() < MAX_NODES && !nodes.contains(node)) {
                nodes.add(node.immutable());
                setNodes(stack, nodes);
                player.displayClientMessage(Component.translatable("message.cqrepoured.path_tool.added",
                        node.getX(), node.getY(), node.getZ(), nodes.size()), true);
            }
        }
        return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isCreative() || !player.isCrouching()) return InteractionResultHolder.pass(stack);
        if (!level.isClientSide) {
            setNodes(stack, List.of());
            player.displayClientMessage(Component.translatable("message.cqrepoured.path_tool.cleared"), true);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    public static List<BlockPos> getNodes(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return new ArrayList<>();
        long[] values = data.copyTag().getLongArray(PATH_TAG);
        List<BlockPos> nodes = new ArrayList<>(values.length);
        for (long value : values) nodes.add(BlockPos.of(value));
        return nodes;
    }

    public static void setNodes(ItemStack stack, List<BlockPos> nodes) {
        CustomData old = stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag data = old == null ? new CompoundTag() : old.copyTag();
        long[] values = nodes.stream().limit(MAX_NODES).mapToLong(BlockPos::asLong).toArray();
        data.putLongArray(PATH_TAG, values);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean selected) {
        super.inventoryTick(stack, level, entity, slotId, selected);
        if (!selected || level.isClientSide || entity.tickCount % 10 != 0 || !(level instanceof ServerLevel serverLevel)) return;
        List<BlockPos> nodes = getNodes(stack);
        for (int i = 0; i < nodes.size(); i++) {
            BlockPos node = nodes.get(i);
            if (entity.distanceToSqr(node.getX() + 0.5D, node.getY(), node.getZ() + 0.5D) > 9216.0D) continue;
            serverLevel.sendParticles(i == 0 ? net.minecraft.core.particles.ParticleTypes.FLAME : net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                    node.getX() + 0.5D, node.getY() + 0.2D, node.getZ() + 0.5D,
                    1, 0.08D, 0.08D, 0.08D, 0.0D);
            if (i > 0 && entity.distanceToSqr(nodes.get(i - 1).getX() + 0.5D, nodes.get(i - 1).getY(), nodes.get(i - 1).getZ() + 0.5D) <= 9216.0D) {
                drawLine(serverLevel, nodes.get(i - 1), node);
            }
        }
        if (nodes.size() > 2
                && entity.distanceToSqr(nodes.get(nodes.size() - 1).getX() + 0.5D, nodes.get(nodes.size() - 1).getY(), nodes.get(nodes.size() - 1).getZ() + 0.5D) <= 9216.0D
                && entity.distanceToSqr(nodes.get(0).getX() + 0.5D, nodes.get(0).getY(), nodes.get(0).getZ() + 0.5D) <= 9216.0D) {
            drawLine(serverLevel, nodes.get(nodes.size() - 1), nodes.get(0));
        }
    }

    private static void drawLine(ServerLevel level, BlockPos from, BlockPos to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();
        int steps = Math.min(16, Math.max(1, (int) Math.ceil(Math.sqrt(dx * dx + dy * dy + dz * dz))));
        for (int step = 1; step < steps; step++) {
            double progress = step / (double) steps;
            level.sendParticles(net.minecraft.core.particles.ParticleTypes.ENCHANT,
                    from.getX() + 0.5D + dx * progress,
                    from.getY() + 0.25D + dy * progress,
                    from.getZ() + 0.5D + dz * progress,
                    1, 0, 0, 0, 0);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.cqrepoured.path_tool.tooltip").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("item.cqrepoured.path_tool.nodes", getNodes(stack).size()).withStyle(ChatFormatting.GRAY));
    }
}
