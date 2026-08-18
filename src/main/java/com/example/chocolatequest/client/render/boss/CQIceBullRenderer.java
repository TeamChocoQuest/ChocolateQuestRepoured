package com.example.chocolatequest.client.render.boss;

import com.example.chocolatequest.client.model.boss.CQIceBullModel;
import com.example.chocolatequest.entity.boss.CQIceBullEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CQIceBullRenderer extends GeoEntityRenderer<CQIceBullEntity> {
    public CQIceBullRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CQIceBullModel());
        this.withScale(1.3f, 1.3f);
    }
}
