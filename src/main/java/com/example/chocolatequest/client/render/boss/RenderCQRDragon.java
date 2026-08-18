package com.example.chocolatequest.client.render.boss;

import com.example.chocolatequest.client.model.entity.boss.GreenDragonModel;
import com.example.chocolatequest.client.render.layer.DragonSaddleGeoLayer;
import com.example.chocolatequest.entity.boss.EntityCQRDragon;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RenderCQRDragon extends GeoEntityRenderer<EntityCQRDragon> {
    public RenderCQRDragon(EntityRendererProvider.Context context) {
        super(context, new GreenDragonModel());
        this.shadowRadius = 2.75F;
        this.withScale(0.96F);
        this.addRenderLayer(new DragonSaddleGeoLayer(this));
    }
}
