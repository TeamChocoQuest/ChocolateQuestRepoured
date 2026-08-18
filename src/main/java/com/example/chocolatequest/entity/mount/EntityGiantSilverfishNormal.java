package com.example.chocolatequest.entity.mount;

import com.example.chocolatequest.entity.bases.EntityCQRGiantSilverfishBase;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;

public class EntityGiantSilverfishNormal extends EntityCQRGiantSilverfishBase {

    public EntityGiantSilverfishNormal(EntityType<? extends EntityGiantSilverfishNormal> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 30.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.35D)
            .add(Attributes.ATTACK_DAMAGE, 3.0D)
            .add(Attributes.ARMOR, 2.0D)
            .add(Attributes.STEP_HEIGHT, 1.0D);
    }
}
