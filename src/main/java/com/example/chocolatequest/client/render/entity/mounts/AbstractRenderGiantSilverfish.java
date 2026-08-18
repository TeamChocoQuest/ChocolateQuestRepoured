package com.example.chocolatequest.client.render.entity.mounts;

import com.example.chocolatequest.entity.bases.EntityCQRGiantSilverfishBase;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;

public abstract class AbstractRenderGiantSilverfish<T extends EntityCQRGiantSilverfishBase> extends MobRenderer<T, SilverfishModel<T>> {

    public AbstractRenderGiantSilverfish(EntityRendererProvider.Context context) {
        super(context, new SilverfishModel(context.bakeLayer(ModelLayers.SILVERFISH)), 1.5F);
    }

    @Override
    protected void scale(T entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(4.0F, 4.0F, 4.0F);
    }
}
