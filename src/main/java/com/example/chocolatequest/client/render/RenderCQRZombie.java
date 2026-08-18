package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRZombieGeo;
import com.example.chocolatequest.entity.mob.CQZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRZombie extends RenderCQRBipedBaseGeo<CQZombieEntity> {
    public RenderCQRZombie(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelCQRZombieGeo());
    }
}
