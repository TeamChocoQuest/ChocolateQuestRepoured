package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelShelob;
import com.example.chocolatequest.entity.boss.ShelobEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RenderShelob extends GeoEntityRenderer<ShelobEntity> {
    public RenderShelob(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelShelob());
        this.shadowRadius = 1.0f;
        this.withScale(1.5f, 1.5f);
    }
}
