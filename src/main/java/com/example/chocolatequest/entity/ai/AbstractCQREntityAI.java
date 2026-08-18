package com.example.chocolatequest.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;

import java.util.Random;

public abstract class AbstractCQREntityAI<T extends AbstractEntityCQR> extends Goal {

    protected final Random random = new Random();
    protected final T entity;
    protected final Level level;

    protected AbstractCQREntityAI(T entity) {
        this.entity = entity;
        this.level = entity.level();
    }
}
