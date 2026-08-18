package com.example.chocolatequest.client.render.boss;

import com.example.chocolatequest.client.model.boss.CQBullModel;
import com.example.chocolatequest.entity.boss.CQBullEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CQBullRenderer extends GeoEntityRenderer<CQBullEntity> {
    public CQBullRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CQBullModel());
        this.withScale(1.3f, 1.3f);
    }
}
