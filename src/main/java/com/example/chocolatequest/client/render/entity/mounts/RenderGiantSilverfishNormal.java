package com.example.chocolatequest.client.render.entity.mounts;

import com.example.chocolatequest.entity.mount.EntityGiantSilverfishNormal;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderGiantSilverfishNormal extends AbstractRenderGiantSilverfish<EntityGiantSilverfishNormal> {

    public RenderGiantSilverfishNormal(EntityRendererProvider.Context context) {
        super(context);
    }

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("cqrepoured", "textures/entity/mounts/giant_silverfish.png");

    @Override
    public ResourceLocation getTextureLocation(EntityGiantSilverfishNormal entity) {
        return TEXTURE;
    }
}
