package com.example.chocolatequest.client.render.boss;

import com.example.chocolatequest.client.model.ModelCQRShulkerGolem;
import com.example.chocolatequest.entity.boss.EntityCQRShulkerGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RenderCQRShulkerGolem extends GeoEntityRenderer<EntityCQRShulkerGolem> {
    public RenderCQRShulkerGolem(EntityRendererProvider.Context context) {
        super(context, new ModelCQRShulkerGolem());
        this.withScale(1.22F);
        this.shadowRadius = 2.15F;
    }
}
