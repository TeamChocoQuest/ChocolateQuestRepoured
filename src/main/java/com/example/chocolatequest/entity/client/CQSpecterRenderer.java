package com.example.chocolatequest.entity.client;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQSpecterEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class CQSpecterRenderer extends HumanoidMobRenderer<CQSpecterEntity, HumanoidModel<CQSpecterEntity>> {

    private static final ResourceLocation SPECTER_LOCATION = ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/mob/spectre_0.png");

    public CQSpecterRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(CQSpecterEntity entity) {
        int index = entity.getTextureIndex() % Math.max(1, entity.getTextureCount());
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/mob/spectre_" + index + ".png");
    }

    @Nullable
    @Override
    protected RenderType getRenderType(CQSpecterEntity entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        // Force translucent rendering
        return RenderType.entityTranslucent(getTextureLocation(entity));
    }
}
