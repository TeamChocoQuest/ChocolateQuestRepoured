package com.example.chocolatequest.client.render.boss;

import com.example.chocolatequest.client.model.ModelCQRGiantTortoiseGeo;
import com.example.chocolatequest.entity.boss.EntityCQRGiantTortoise;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RenderCQRGiantTortoise extends GeoEntityRenderer<EntityCQRGiantTortoise> {
    public RenderCQRGiantTortoise(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelCQRGiantTortoiseGeo());
        this.shadowRadius = 1.5f;
    }
}
