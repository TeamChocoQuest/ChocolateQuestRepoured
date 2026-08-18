package com.example.chocolatequest.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HealingSlimeEntity extends Slime {

    public HealingSlimeEntity(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 1.0D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.0D);
    }

    @Override
    protected void registerGoals() {
        // Do nothing to prevent the slime from wandering or attacking
    }

    public void tick() {
        super.tick();
        // Force the size to 1 (smallest) constantly, just in case
        if (this.getSize() != 1) {
            this.setSize(1, true);
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (!this.level().isClientSide()) {
            if (player.getHealth() < player.getMaxHealth()) {
                player.heal(2.0f); // Heal 1 heart
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SLIME_SQUISH, SoundSource.NEUTRAL, 1.0f, 1.5f);
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getY() + 0.5, this.getZ(), 10, 0.3, 0.3, 0.3, 0);
                this.discard(); // Remove the slime
            }
        }
    }

    @Override
    protected boolean shouldDropLoot() {
        return false; // Prevent dropping slimeballs
    }

}
