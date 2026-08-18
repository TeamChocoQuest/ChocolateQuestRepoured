package com.example.chocolatequest.item.staff;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import com.example.chocolatequest.entity.ai.target.TargetUtil;

public class FireStaffItem extends Item {

    public FireStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        // Can only start shooting if we have at least 1 Food Level (or creative mode)
        if (player.getFoodData().getFoodLevel() > 0 || player.getAbilities().instabuild) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity player, ItemStack stack, int remainingUseDuration) {
        if (player instanceof Player p && !p.getAbilities().instabuild) {
            if (p.getFoodData().getFoodLevel() <= 0) {
                p.stopUsingItem();
                return;
            }
        }

        boolean isServer = level instanceof ServerLevel;
        ServerLevel serverLevel = isServer ? (ServerLevel) level : null;

        // Consume Hunger every 10 ticks (0.5 seconds = half a drumstick)
        if (remainingUseDuration % 10 == 0) {
            if (player instanceof Player p && !p.getAbilities().instabuild) {
                int food = p.getFoodData().getFoodLevel();
                if (food > 0) {
                    p.getFoodData().setFoodLevel(food - 1);
                }
            }
        }

        // Tick every 2 ticks for AoE damage and particles
        if (remainingUseDuration % 2 == 0) {
            Vec3 look = player.getLookAngle().normalize();
            Vec3 start = player.getEyePosition();
            double dist = 5.0D;

            // Build a rotating cone instead of scattering unrelated points
            // along the look vector. The coherent rings make the stream read as
            // expanding fire while keeping the particle count modest.
            if (isServer) {
                Vec3 referenceUp = Math.abs(look.y) > 0.95D
                        ? new Vec3(1.0D, 0.0D, 0.0D)
                        : new Vec3(0.0D, 1.0D, 0.0D);
                Vec3 right = look.cross(referenceUp).normalize();
                Vec3 up = right.cross(look).normalize();
                double phase = player.tickCount * 0.55D;
                double goldenAngle = Math.PI * (3.0D - Math.sqrt(5.0D));

                for (int i = 0; i < 12; i++) {
                    double progress = (i + 1.0D) / 12.0D;
                    double distance = 0.45D + progress * 4.55D;
                    double radius = 0.05D + distance * 0.16D;
                    double angle = phase + i * goldenAngle;
                    Vec3 radial = right.scale(Math.cos(angle)).add(up.scale(Math.sin(angle)));
                    Vec3 particlePos = start.add(look.scale(distance)).add(radial.scale(radius));
                    Vec3 velocity = look.scale(0.08D + progress * 0.12D)
                            .add(radial.scale(0.03D + progress * 0.035D))
                            .add(0.0D, 0.012D, 0.0D);
                    serverLevel.sendParticles(ParticleTypes.FLAME,
                            particlePos.x, particlePos.y, particlePos.z,
                            0, velocity.x, velocity.y, velocity.z, 1.0D);

                    if (i % 4 == 0) {
                        Vec3 smokePos = particlePos.add(0.0D, 0.08D, 0.0D);
                        serverLevel.sendParticles(ParticleTypes.SMOKE,
                                smokePos.x, smokePos.y, smokePos.z,
                                0, velocity.x * 0.45D, 0.025D, velocity.z * 0.45D, 1.0D);
                    }
                }

                if (remainingUseDuration % 8 == 0) {
                    Vec3 tip = start.add(look.scale(4.5D));
                    serverLevel.sendParticles(ParticleTypes.LAVA,
                            tip.x, tip.y, tip.z, 2, 0.28D, 0.22D, 0.28D, 0.02D);
                }
            }

            // Damage logic
            if (isServer) {
                AABB aabb = player.getBoundingBox().inflate(dist);
                List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, aabb, e -> e != player && e.isAlive() && !TargetUtil.isAllyCheckingLeaders(player, e));

                for (LivingEntity e : targets) {
                    if (e.distanceTo(player) <= dist) {
                        Vec3 toEntity = e.getEyePosition().subtract(start).normalize();
                        double dot = look.dot(toEntity);
                        // ~31 degree cone
                        if (dot > 0.85D) {
                            // Damage and set on fire
                            e.hurt(level.damageSources().inFire(), 1.0F); // 0.5 hearts
                            e.setRemainingFireTicks(80); // 4 seconds of fire
                            if (remainingUseDuration % 4 == 0) {
                                serverLevel.sendParticles(ParticleTypes.FLAME,
                                        e.getX(), e.getY() + e.getBbHeight() * 0.55D, e.getZ(),
                                        5, e.getBbWidth() * 0.25D, e.getBbHeight() * 0.2D,
                                        e.getBbWidth() * 0.25D, 0.025D);
                            }
                        }
                    }
                }
            }
        }

        if (isServer && remainingUseDuration % 6 == 0) {
            level.playSound(null, player.blockPosition(), SoundEvents.FIRECHARGE_USE,
                    SoundSource.PLAYERS, 0.32F, 0.9F + level.random.nextFloat() * 0.22F);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Melee attack: sets target on fire for 3 seconds and uses half a drumstick
        target.setRemainingFireTicks(60);
        
        if (attacker.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLAME, target.getX(), target.getY() + 1.0, target.getZ(), 10, 0.3, 0.3, 0.3, 0.05);
        }

        if (attacker instanceof Player p && !p.getAbilities().instabuild) {
            int food = p.getFoodData().getFoodLevel();
            if (food > 0) {
                p.getFoodData().setFoodLevel(food - 1);
            }
        }
        return true;
    }
}
