package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQREndermanGeo;
import com.example.chocolatequest.entity.mob.CQEndermanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQREnderman extends RenderCQRBipedBaseGeo<CQEndermanEntity> {
    public RenderCQREnderman(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelCQREndermanGeo());
    }
}
