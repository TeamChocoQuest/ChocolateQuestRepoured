package com.example.chocolatequest.client.render.entity.mounts;

import com.example.chocolatequest.entity.mount.EntityGiantSilverfishRed;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderGiantSilverfishRed extends AbstractRenderGiantSilverfish<EntityGiantSilverfishRed> {

    public RenderGiantSilverfishRed(EntityRendererProvider.Context context) {
        super(context);
    }

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("cqrepoured", "textures/entity/mounts/giant_silverfish_red.png");

    @Override
    public ResourceLocation getTextureLocation(EntityGiantSilverfishRed entity) {
        return TEXTURE;
    }
}
