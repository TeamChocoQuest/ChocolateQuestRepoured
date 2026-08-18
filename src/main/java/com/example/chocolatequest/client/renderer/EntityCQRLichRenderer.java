package com.example.chocolatequest.client.renderer;

import com.example.chocolatequest.client.model.EntityCQRLichModel;
import com.example.chocolatequest.entity.boss.EntityCQRLich;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.example.chocolatequest.client.render.RenderCQRBipedBaseGeo;

public class EntityCQRLichRenderer extends RenderCQRBipedBaseGeo<EntityCQRLich> {
    public EntityCQRLichRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EntityCQRLichModel());
    }

    @Override
    protected boolean shouldRenderBuiltInCape(EntityCQRLich entity) {
        return true;
    }
}
