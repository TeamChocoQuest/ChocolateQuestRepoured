package com.example.chocolatequest.item.staff;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.projectile.ProjectileUtil;

import java.util.List;

import com.example.chocolatequest.item.IRangedWeapon;
public class IceStaffItem extends Item implements IRangedWeapon {

    public IceStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getFoodData().getFoodLevel() >= 1 || player.getAbilities().instabuild) {
            
            // Raytrace to find target
            double reach = 16.0D;
            Vec3 eyePos = player.getEyePosition();
            Vec3 lookVec = player.getViewVector(1.0F);
            Vec3 endPos = eyePos.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
            AABB searchBox = player.getBoundingBox().expandTowards(lookVec.scale(reach)).inflate(1.0D, 1.0D, 1.0D);

            EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(level, player, eyePos, endPos, searchBox, e -> e instanceof LivingEntity && !e.isSpectator());

            if (hitResult != null && hitResult.getEntity() instanceof LivingEntity target) {
                if (!level.isClientSide()) {
                    // Freeze target and nearby entities
                    AABB freezeBox = target.getBoundingBox().inflate(4.0D);
                    List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, freezeBox, e -> e != player && e.isAlive() && !com.example.chocolatequest.entity.ai.target.TargetUtil.isAllyCheckingLeaders(player, e));

                    for (LivingEntity e : targets) {
                        if (target.distanceTo(e) < 16.0D) {
                            e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 70, 0)); // Slowness I for 3.5s
                            e.setTicksFrozen(e.getTicksRequiredToFreeze() + 70); // Freeze for 3.5s
                        }
                    }
                    
                    target.setTicksFrozen(target.getTicksRequiredToFreeze() + 70);

                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, target.getX(), target.getY() + 1.0, target.getZ(), 30, 1.0, 1.0, 1.0, 0.1);
                        serverLevel.sendParticles(ParticleTypes.SPLASH, target.getX(), target.getY() + 1.0, target.getZ(), 10, 1.0, 1.0, 1.0, 0.1);
                    }
                    
                    level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.SNOW_BREAK, SoundSource.PLAYERS, 1.0F, (1.0f + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2f) * 0.7f);
                }

                if (!player.getAbilities().instabuild) {
                    player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);
                }
            }

            com.example.chocolatequest.registry.ModItems.applySharedCooldown(player, this, 20); // 1s cooldown
            return InteractionResultHolder.success(stack);
        }
        
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            if (player.getFoodData().getFoodLevel() >= 1 || player.getAbilities().instabuild) {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1)); // Slowness II for 10s
                target.setTicksFrozen(target.getTicksRequiredToFreeze() + 200); // Freeze for 10s
                
                if (attacker.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, target.getX(), target.getY() + 1.0, target.getZ(), 10, 0.5, 0.5, 0.5, 0.05);
                }

                if (!player.getAbilities().instabuild) {
                    player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);
                }
            }
        }
        return true;
    }

    @Override
    public void shoot(net.minecraft.world.level.Level level, net.minecraft.world.entity.LivingEntity shooter, net.minecraft.world.entity.Entity target, net.minecraft.world.InteractionHand hand) {

        if (!level.isClientSide() && target instanceof net.minecraft.world.entity.LivingEntity t) {
            net.minecraft.world.phys.AABB freezeBox = t.getBoundingBox().inflate(4.0D);
            java.util.List<net.minecraft.world.entity.LivingEntity> targets = level.getEntitiesOfClass(net.minecraft.world.entity.LivingEntity.class, freezeBox, e -> e != shooter && e.isAlive() && !com.example.chocolatequest.entity.ai.target.TargetUtil.isAllyCheckingLeaders(shooter, e));
            for (net.minecraft.world.entity.LivingEntity e : targets) {
                if (t.distanceTo(e) < 16.0D) {
                    e.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN, 70, 0));
                    e.setTicksFrozen(e.getTicksRequiredToFreeze() + 70);
                }
            }
            t.setTicksFrozen(t.getTicksRequiredToFreeze() + 70);
            if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SNOWFLAKE, t.getX(), t.getY() + 1.0, t.getZ(), 30, 1.0, 1.0, 1.0, 0.1);
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SPLASH, t.getX(), t.getY() + 1.0, t.getZ(), 10, 1.0, 1.0, 1.0, 0.1);
            }
            level.playSound(null, t.getX(), t.getY(), t.getZ(), getShootSound(), net.minecraft.sounds.SoundSource.HOSTILE, 1.0F, (1.0f + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2f) * 0.7f);
        }

    }

    @Override
    public net.minecraft.sounds.SoundEvent getShootSound() {
        return net.minecraft.sounds.SoundEvents.SNOW_BREAK;
    }

    @Override
    public double getRange() { return 15.0; }

    @Override
    public int getCooldown() { return 20; }

    @Override
    public int getChargeTicks() { return 20; }

    @Override
    public int getUseDuration(net.minecraft.world.item.ItemStack stack, net.minecraft.world.entity.LivingEntity entity) {
        return 72000;
    }

    @Override
    public net.minecraft.world.item.UseAnim getUseAnimation(net.minecraft.world.item.ItemStack stack) {
        return net.minecraft.world.item.UseAnim.BOW;
    }

}
