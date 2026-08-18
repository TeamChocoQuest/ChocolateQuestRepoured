package com.example.chocolatequest.block;

import com.example.chocolatequest.block.entity.BannerStandBlockEntity;
import com.example.chocolatequest.item.CQBannerItem;
import com.example.chocolatequest.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BannerStandBlock extends BaseEntityBlock {

    public static final com.mojang.serialization.MapCodec<BannerStandBlock> CODEC = simpleCodec(BannerStandBlock::new);

    protected static final VoxelShape SHAPE = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 2.0D, 10.0D);

    public BannerStandBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected com.mojang.serialization.MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.BANNER_STAND.get().create(pos, state);
    }

    @Override
    protected net.minecraft.world.ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BannerStandBlockEntity bannerStand) {
            if (bannerStand.hasBanner()) {
                if (player.isShiftKeyDown()) {
                    if (!level.isClientSide) {
                        player.getInventory().placeItemBackInInventory(bannerStand.getBanner());
                        bannerStand.setBanner(ItemStack.EMPTY);
                    }
                    return net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
            } else if (!stack.isEmpty() && stack.getItem() instanceof CQBannerItem) {
                if (!level.isClientSide) {
                    ItemStack bannerCopy = stack.copyWithCount(1);
                    bannerStand.setBanner(bannerCopy);
                    bannerStand.setRotation((int) player.getYRot() - 180);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                return net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BannerStandBlockEntity bannerStand && bannerStand.hasBanner()) {
                Block.popResource(level, pos, bannerStand.getBanner());
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}

