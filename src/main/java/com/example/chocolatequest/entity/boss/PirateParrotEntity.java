package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.ai.boss.piratecaptain.parrot.BossAIPirateParrotLandOnCaptainsShoulder;
import com.example.chocolatequest.entity.ai.boss.piratecaptain.parrot.BossAIPirateParrotThrowPotions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

public class PirateParrotEntity extends Parrot {

    public PirateParrotEntity(EntityType<? extends Parrot> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Parrot.createAttributes()
                .add(Attributes.MAX_HEALTH, 48.0D)
                .add(Attributes.FLYING_SPEED, 0.8D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(1, new BossAIPirateParrotThrowPotions(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new BossAIPirateParrotLandOnCaptainsShoulder(this));

        // Attack players
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public void forceAddEffect(MobEffectInstance effect, net.minecraft.world.entity.Entity entity) {
        if (!effect.getEffect().value().isBeneficial()) {
            return;
        }
        super.forceAddEffect(effect, entity);
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, net.minecraft.world.entity.Entity entity) {
        if (effectInstance.getEffect().value().isBeneficial()) {
            return super.addEffect(effectInstance, entity);
        }
        return false;
    }

    @SuppressWarnings("resource")
    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (!this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.MOB);
        }
    }

    @Override
    public boolean setEntityOnShoulder(net.minecraft.server.level.ServerPlayer player) {
        return false; // Pirates only, not players
    }

    public boolean canSitOnShoulder() {
        return true;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isTame() && this.getOwnerUUID() != null && this.getOwnerUUID().equals(player.getUUID())) {
            return InteractionResult.SUCCESS; // Ignore player interactions if owned by Captain
        }
        return super.mobInteract(player, hand);
    }
}

