package com.example.chocolatequest.client.renderer;

import com.example.chocolatequest.entity.projectile.CQRArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CQRArrowRenderer extends ArrowRenderer<CQRArrowEntity> {
    public CQRArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(CQRArrowEntity entity) {
        String type = entity.getElementType();
        return ResourceLocation.fromNamespaceAndPath("cqrepoured", "textures/entity/projectiles/" + type + "_arrow.png");
    }
}
