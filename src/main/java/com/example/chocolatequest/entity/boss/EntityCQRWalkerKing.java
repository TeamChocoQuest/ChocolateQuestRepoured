package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.mob.CQWalkerEntity;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.example.chocolatequest.registry.ModItems;
import com.example.chocolatequest.entity.projectile.WindProjectileEntity;
import java.util.EnumSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class EntityCQRWalkerKing extends CQWalkerEntity {
    protected final ServerBossEvent bossEvent;
    private boolean desperationUsed;
    private int projectileDodgeCooldown;

    public EntityCQRWalkerKing(EntityType<? extends AbstractEntityCQR> type, Level level) {
        super(type, level);
        this.bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.NOTCHED_10);
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return AbstractEntityCQR.createCQRAttributes()
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 600.0D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 12.0D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR, 20.0D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    public double getBaseHealth() {
        return 600.0D;
    }

    @Override
    public void startSeenByPlayer(net.minecraft.server.level.ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(net.minecraft.server.level.ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    

    

    

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new WalkerKingTornadoGoal(this));
    }

    protected boolean teleport() {
        if (!this.level().isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 16.0D;
            double d1 = this.getY() + (double)(this.random.nextInt(16) - 8);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 16.0D;
            return this.randomTeleport(d0, d1, d2, true);
        }
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof net.minecraft.world.entity.projectile.Projectile
                && this.projectileDodgeCooldown <= 0 && this.random.nextFloat() < 0.55F) {
            double oldX = this.getX();
            double oldY = this.getY() + 1.0D;
            double oldZ = this.getZ();
            for(int i = 0; i < 10; ++i) {
                if (this.teleport()) {
                    this.projectileDodgeCooldown = 50;
                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 0.8F);
                    if (this.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.CLOUD, oldX, oldY, oldZ,
                                18, 0.4D, 0.8D, 0.4D, 0.08D);
                    }
                    return false;
                }
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.projectileDodgeCooldown > 0) this.projectileDodgeCooldown--;
            LivingEntity target = this.getTarget();
            if (target != null) {
                if (this.random.nextInt(40) == 0 && !this.isUsingItem()) {
                    this.startUsingItem(InteractionHand.OFF_HAND);
                }
                
                // Stop blocking after a short time or if target is far
                if (this.isUsingItem() && this.random.nextInt(20) == 0) {
                    this.stopUsingItem();
                }
            } else if (this.isUsingItem()) {
                this.stopUsingItem();
            }
        }
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        if (!this.desperationUsed && this.getHealth() < 100.0F && this.getTarget() != null) {
            this.desperationUsed = true;
            // Desperation move
            if (this.level() instanceof ServerLevel serverLevel) {
                LightningBolt lightningMe = EntityType.LIGHTNING_BOLT.create(serverLevel);
                if (lightningMe != null) {
                    lightningMe.setVisualOnly(true);
                    lightningMe.moveTo(Vec3.atBottomCenterOf(this.blockPosition()));
                    serverLevel.addFreshEntity(lightningMe);
                }
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY() + 1.0D,
                        this.getZ(), 50, 2.0D, 1.5D, 2.0D, 0.15D);
                this.heal(60.0F);
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 0));
                this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 160, 0));
            }
        }
    }

    class WalkerKingTornadoGoal extends Goal {
        private final EntityCQRWalkerKing king;
        private int cooldown = 55;
        private int timer;
        private boolean aimedVolley;

        public WalkerKingTornadoGoal(EntityCQRWalkerKing king) {
            this.king = king;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.king.getTarget() == null) return false;
            if (this.cooldown > 0) {
                this.cooldown--;
                return false;
            }
            return this.king.getTarget().isAlive();
        }

        @Override
        public void start() {
            this.timer = 0;
            this.aimedVolley = this.king.getRandom().nextBoolean();
            this.king.getNavigation().stop();
            this.king.setSpinToWin(true);
            this.king.playSound(SoundEvents.BREEZE_WIND_CHARGE_BURST.value(), 1.2F, 0.6F);
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < 30 && this.king.getTarget() != null;
        }

        @Override
        public void tick() {
            this.timer++;
            LivingEntity target = this.king.getTarget();
            if (target == null) return;
            this.king.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.king.level() instanceof ServerLevel serverLevel && this.timer < 19) {
                serverLevel.sendParticles(ParticleTypes.CLOUD, this.king.getX(), this.king.getY() + 1.0D,
                        this.king.getZ(), 5, 1.0D, 0.8D, 1.0D, 0.08D);
            }
            if (this.timer == 19) {
                this.releaseTornadoes(target);
                this.king.playSound(SoundEvents.WIND_CHARGE_BURST.value(), 1.4F, 0.7F);
            }
        }

        private void releaseTornadoes(LivingEntity target) {
            if (this.aimedVolley) {
                Vec3 direction = target.getEyePosition().subtract(this.king.getEyePosition()).normalize();
                for (double angle : new double[]{-0.28D, 0.0D, 0.28D}) {
                    double cos = Math.cos(angle);
                    double sin = Math.sin(angle);
                    double x = direction.x * cos - direction.z * sin;
                    double z = direction.x * sin + direction.z * cos;
                    WindProjectileEntity tornado = new WindProjectileEntity(this.king.level(), this.king);
                    tornado.shoot(x, direction.y, z, 0.9F, 0.0F);
                    this.king.level().addFreshEntity(tornado);
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    WindProjectileEntity tornado = new WindProjectileEntity(this.king.level(), this.king);
                    double dirX = Math.cos(i * Math.PI / 3.0D);
                    double dirZ = Math.sin(i * Math.PI / 3.0D);
                    tornado.shoot(dirX, 0.12D, dirZ, 0.85F, 0.0F);
                    this.king.level().addFreshEntity(tornado);
                }
            }
        }

        @Override
        public void stop() {
            this.king.setSpinToWin(false);
            this.cooldown = 150;
        }
    }
    @Override
    protected void equipBasedOnTier(com.example.chocolatequest.entity.mob.enums.MobTier tier) {
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.WALKER_SWORD.get()));
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.OFFHAND, new net.minecraft.world.item.ItemStack(ModItems.SHIELD_WALKER_KING.get()));
        
        
        int purple = 8991416;
        net.minecraft.world.item.ItemStack helmet = new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.DIAMOND_DYEABLE_HELMET.get());
        helmet.set(net.minecraft.core.component.DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(purple, true));
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.HEAD, helmet);
        
        net.minecraft.world.item.ItemStack chest = new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.DIAMOND_DYEABLE_CHESTPLATE.get());
        chest.set(net.minecraft.core.component.DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(purple, true));
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.CHEST, chest);
        
        net.minecraft.world.item.ItemStack legs = new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.DIAMOND_DYEABLE_LEGGINGS.get());
        legs.set(net.minecraft.core.component.DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(purple, true));
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.LEGS, legs);
        
        net.minecraft.world.item.ItemStack boots = new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.DIAMOND_DYEABLE_BOOTS.get());
        boots.set(net.minecraft.core.component.DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(purple, true));
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.FEET, boots);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        this.spawnAtLocation(ModItems.WALKER_SWORD.get());
        this.spawnAtLocation(ModItems.SHIELD_WALKER_KING.get());
        this.spawnAtLocation(ModItems.CAPE_WALKER.get());
        this.spawnAtLocation(new ItemStack(Items.BLUE_ICE, 4 + this.random.nextInt(5)));
        this.spawnAtLocation(new ItemStack(Items.ARROW, 12 + this.random.nextInt(13)));

        if (this.random.nextFloat() < 0.5F) {
            int purple = 8991416;
            int piece = this.random.nextInt(4);
            ItemStack armorPiece;
            if (piece == 0) armorPiece = new ItemStack(ModItems.DIAMOND_DYEABLE_HELMET.get());
            else if (piece == 1) armorPiece = new ItemStack(ModItems.DIAMOND_DYEABLE_CHESTPLATE.get());
            else if (piece == 2) armorPiece = new ItemStack(ModItems.DIAMOND_DYEABLE_LEGGINGS.get());
            else armorPiece = new ItemStack(ModItems.DIAMOND_DYEABLE_BOOTS.get());
            armorPiece.set(net.minecraft.core.component.DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(purple, true));
            this.spawnAtLocation(armorPiece);
        }

        this.spawnAtLocation(new ItemStack(Items.DIAMOND, 4 + this.random.nextInt(5)));
    }
}
