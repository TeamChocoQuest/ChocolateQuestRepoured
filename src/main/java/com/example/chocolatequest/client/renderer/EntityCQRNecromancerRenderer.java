package com.example.chocolatequest.client.renderer;

import com.example.chocolatequest.client.model.EntityCQRNecromancerModel;
import com.example.chocolatequest.entity.boss.EntityCQRNecromancer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.example.chocolatequest.client.render.RenderCQRBipedBaseGeo;

public class EntityCQRNecromancerRenderer extends RenderCQRBipedBaseGeo<EntityCQRNecromancer> {
    public EntityCQRNecromancerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EntityCQRNecromancerModel());
    }

    @Override
    protected boolean shouldRenderBuiltInCape(EntityCQRNecromancer entity) {
        return true;
    }
}
