package com.example.chocolatequest.client.render.entity.mounts;

import com.example.chocolatequest.entity.mount.EntityGiantSilverfishGreen;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderGiantSilverfishGreen extends AbstractRenderGiantSilverfish<EntityGiantSilverfishGreen> {

    public RenderGiantSilverfishGreen(EntityRendererProvider.Context context) {
        super(context);
    }

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("cqrepoured", "textures/entity/mounts/giant_silverfish_green.png");

    @Override
    public ResourceLocation getTextureLocation(EntityGiantSilverfishGreen entity) {
        return TEXTURE;
    }
}
