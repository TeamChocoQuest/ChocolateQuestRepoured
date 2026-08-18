package com.example.chocolatequest.entity.mob;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.faction.EDefaultFaction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class CQWalkerEntity extends AbstractEntityCQR {

    public CQWalkerEntity(EntityType<? extends AbstractEntityCQR> type, Level level) {
        super(type, level);
    }

    @Override
    public double getBaseHealth() {
        return 20.0D;
    }

    @Override
    public EDefaultFaction getDefaultFaction() {
        return EDefaultFaction.WALKERS;
    }

    @Override
    public int getTextureCount() {
        return 3;
    }
}
