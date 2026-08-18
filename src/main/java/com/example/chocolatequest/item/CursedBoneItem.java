package com.example.chocolatequest.item;

import com.example.chocolatequest.faction.EDefaultFaction;
import com.example.chocolatequest.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

public class CursedBoneItem extends Item {
    public CursedBoneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);

        if (!pLevel.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) pLevel;
            
            pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), 
                SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.5F, 1.5F);

            // Spawn 2-3 skeletons
            int skeletonsToSpawn = 2 + pLevel.random.nextInt(2);
            for (int i = 0; i < skeletonsToSpawn; i++) {
                BlockPos spawnPos = pPlayer.blockPosition().offset(
                    pLevel.random.nextInt(5) - 2,
                    1,
                    pLevel.random.nextInt(5) - 2
                );

                var skeleton = ModEntities.CQ_SKELETON.get().create(serverLevel);
                if (skeleton != null) {
                    skeleton.moveTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, pLevel.random.nextFloat() * 360F, 0.0F);
                    skeleton.finalizeSpawn(serverLevel, pLevel.getCurrentDifficultyAt(spawnPos), MobSpawnType.MOB_SUMMONED, null);
                    
                    // Set faction to PLAYERS so they fight alongside the player
                    skeleton.setFaction(EDefaultFaction.PLAYERS);
                    // Set the player as the leader
                    skeleton.setLeader(pPlayer);
                    
                    serverLevel.addFreshEntity(skeleton);
                    
                    // Spawn some particles
                    serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, 
                        skeleton.getX(), skeleton.getY() + 1.0D, skeleton.getZ(), 
                        10, 0.2D, 0.2D, 0.2D, 0.0D);
                }
            }

            // Put item on cooldown for 20 seconds (400 ticks)
            pPlayer.getCooldowns().addCooldown(this, 400);
            
            // Damage the item
            itemStack.hurtAndBreak(skeletonsToSpawn, pPlayer, Player.getSlotForHand(pHand));
        }

        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }
}
