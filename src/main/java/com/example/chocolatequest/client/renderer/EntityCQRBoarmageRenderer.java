package com.example.chocolatequest.client.renderer;

import com.example.chocolatequest.client.model.EntityCQRBoarmageModel;
import com.example.chocolatequest.entity.boss.EntityCQRBoarmage;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.example.chocolatequest.client.render.RenderCQRBipedBaseGeo;

public class EntityCQRBoarmageRenderer extends RenderCQRBipedBaseGeo<EntityCQRBoarmage> {

    public EntityCQRBoarmageRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EntityCQRBoarmageModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    protected boolean shouldRenderBuiltInCape(EntityCQRBoarmage entity) {
        return true;
    }
}
