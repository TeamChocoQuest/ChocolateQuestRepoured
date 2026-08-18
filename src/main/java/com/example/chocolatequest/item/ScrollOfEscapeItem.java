package com.example.chocolatequest.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public class ScrollOfEscapeItem extends Item {

    public ScrollOfEscapeItem(Properties properties) {
        super(properties.stacksTo(16));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 40; // 2 seconds charging
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
            player.getCooldowns().addCooldown(this, 100);

            if (player.isVehicle()) {
                player.ejectPassengers();
            }
            if (player.isPassenger()) {
                player.stopRiding();
            }

            BlockPos targetPos = null;
            ServerLevel targetLevel = serverLevel;
            MinecraftServer server = player.getServer();

            // Try bed / anchor respawn position first
            BlockPos respawnPos = player.getRespawnPosition();
            if (respawnPos != null && server != null) {
                ServerLevel respawnLevel = server.getLevel(player.getRespawnDimension());
                if (respawnLevel != null) {
                    targetLevel = respawnLevel;
                    targetPos = respawnPos;
                }
            }

            // Fallback to world spawn top block if no bed respawn set
            if (targetPos == null && server != null) {
                ServerLevel overworld = server.overworld();
                if (overworld != null) {
                    targetLevel = overworld;
                    BlockPos worldSpawn = targetLevel.getSharedSpawnPos();
                    targetPos = targetLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, worldSpawn);
                }
            }

            // Ultimate fallback to top block of current position
            if (targetPos == null) {
                targetPos = serverLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, player.blockPosition());
            }

            player.teleportTo(targetLevel, targetPos.getX() + 0.5D, targetPos.getY() + 0.1D, targetPos.getZ() + 0.5D, player.getYRot(), player.getXRot());

            for (int i = 0; i < 30; i++) {
                targetLevel.sendParticles(ParticleTypes.PORTAL, player.getX() + level.random.nextDouble() - 0.5D, player.getY() + 0.5D, player.getZ() + level.random.nextDouble() - 0.5D, 1, 0D, 0D, 0D, 0.0D);
                targetLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, player.getX() + level.random.nextDouble() - 0.5D, player.getY() + 0.5D, player.getZ() + level.random.nextDouble() - 0.5D, 1, 0D, 0D, 0D, 0.0D);
            }

            targetLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return super.finishUsingItem(stack, level, entityLiving);
    }
}
