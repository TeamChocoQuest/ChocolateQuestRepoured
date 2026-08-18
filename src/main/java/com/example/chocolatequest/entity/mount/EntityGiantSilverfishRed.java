package com.example.chocolatequest.entity.mount;

import com.example.chocolatequest.entity.bases.EntityCQRGiantSilverfishBase;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;

public class EntityGiantSilverfishRed extends EntityCQRGiantSilverfishBase {

    public EntityGiantSilverfishRed(EntityType<? extends EntityGiantSilverfishRed> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 40.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.35D)
            .add(Attributes.ATTACK_DAMAGE, 5.0D)
            .add(Attributes.ARMOR, 4.0D)
            .add(Attributes.STEP_HEIGHT, 1.0D);
    }
}
