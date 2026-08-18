package com.example.chocolatequest.entity.mob;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.faction.EDefaultFaction;
import net.minecraft.sounds.SoundEvent;
import com.example.chocolatequest.registry.ModSounds;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;

public class CQGremlinEntity extends AbstractEntityCQR {

    public CQGremlinEntity(EntityType<? extends AbstractEntityCQR> type, Level level) {
        super(type, level);
    }

    @Override
    public double getBaseHealth() {
        return 20.0D;
    }

    @Override
    public EDefaultFaction getDefaultFaction() {
        return EDefaultFaction.GREMLINS;
    }

    @Override
    public int getTextureCount() {
        return 1;
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.GREMLIN_SMALLMONSTERSPEAK.get();
    }

    @Override
    protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
        return ModSounds.GREMLIN_SMALLMONSTERHURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.GREMLIN_SMALLMONSTERDEAD.get();
    }
}
