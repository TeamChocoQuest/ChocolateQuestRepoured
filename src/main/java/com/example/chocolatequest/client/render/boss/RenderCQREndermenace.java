package com.example.chocolatequest.client.render.boss;

import com.example.chocolatequest.client.model.ModelCQREndermenaceGeo;
import com.example.chocolatequest.entity.boss.EntityCQREndermenace;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RenderCQREndermenace extends GeoEntityRenderer<EntityCQREndermenace> {
    public RenderCQREndermenace(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelCQREndermenaceGeo());
        this.withScale(1.38F);
        this.shadowRadius = 1.45f;
    }
}
