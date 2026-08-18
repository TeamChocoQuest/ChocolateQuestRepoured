package com.example.chocolatequest.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Component;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.server.MinecraftServer;
import com.example.chocolatequest.registry.ModDataComponents;

import java.util.List;
import net.minecraft.world.entity.Entity;

public class TeleportStoneItem extends Item {

    public TeleportStoneItem(Properties properties) {
        super(properties.durability(100));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, itemSlot, isSelected);

        if (isSelected && entity instanceof Player && level.isClientSide && level.getGameTime() % 4 == 0) {
            GlobalPos pos = stack.get(ModDataComponents.GLOBAL_POS.get());
            if (pos != null && level.dimension().equals(pos.dimension())) {
                double x = pos.pos().getX() + Mth.clamp(level.random.nextGaussian() * 0.3D, -0.5D, 0.5D);
                double y = pos.pos().getY() + Mth.clamp(level.random.nextGaussian() * 0.1D, -0.1D, 0.1D);
                double z = pos.pos().getZ() + Mth.clamp(level.random.nextGaussian() * 0.3D, -0.5D, 0.5D);
                level.addParticle(ParticleTypes.DRAGON_BREATH, x + 0.5D, y + 0.1D, z + 0.5D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 40;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (entityLiving instanceof ServerPlayer player && level instanceof ServerLevel serverLevel) {
            player.getCooldowns().addCooldown(stack.getItem(), 60);
            GlobalPos pos = stack.get(ModDataComponents.GLOBAL_POS.get());

            if (player.isCrouching() && pos != null) {
                stack.remove(ModDataComponents.GLOBAL_POS.get());
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.AMBIENT, 1.0F, 1.0F);
                serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, player.getX() + level.random.nextDouble() - 0.5D, player.getY() + 0.5D, player.getZ() + level.random.nextDouble() - 0.5D, 10, 0D, 0D, 0D, 1.0D);
            } else if (pos == null) {
                GlobalPos newPos = GlobalPos.of(level.dimension(), player.blockPosition());
                stack.set(ModDataComponents.GLOBAL_POS.get(), newPos);
                for (int i = 0; i < 10; i++) {
                    serverLevel.sendParticles(ParticleTypes.FLAME, player.getX() + level.random.nextDouble() - 0.5D, player.getY() + 0.5D, player.getZ() + level.random.nextDouble() - 0.5D, 1, 0D, 0D, 0D, 0.0D);
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.AMBIENT, 1.0F, 1.0F);
            } else if (!player.isCrouching()) {
                if (player.isVehicle()) {
                    player.ejectPassengers();
                }
                if (player.isPassenger()) {
                    player.stopRiding();
                }

                ServerLevel targetDimension = serverLevel;
                if (!pos.dimension().equals(player.level().dimension())) {
                    MinecraftServer server = player.getServer();
                    if (server != null) {
                        targetDimension = server.getLevel(pos.dimension());
                        if (targetDimension == null) {
                            targetDimension = serverLevel;
                        }
                    }
                }
                
                player.teleportTo(targetDimension, pos.pos().getX() + 0.5D, pos.pos().getY(), pos.pos().getZ() + 0.5D, player.getYRot(), player.getXRot());
                
                for (int i = 0; i < 30; i++) {
                    targetDimension.sendParticles(ParticleTypes.PORTAL, player.getX() + level.random.nextDouble() - 0.5D, player.getY() + 0.5D, player.getZ() + level.random.nextDouble() - 0.5D, 1, 0D, 0D, 0D, 0.0D);
                }
                targetDimension.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.AMBIENT, 1.0F, 1.0F);

                if (!player.getAbilities().instabuild) {
                    stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
                }
            }
        }
        return super.finishUsingItem(stack, level, entityLiving);
    }
}
