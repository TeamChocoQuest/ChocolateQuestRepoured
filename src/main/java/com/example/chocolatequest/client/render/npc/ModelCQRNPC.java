package com.example.chocolatequest.client.render.npc;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.animatable.GeoEntity;

public class ModelCQRNPC<T extends GeoEntity> extends GeoModel<T> {

    private final ResourceLocation texture;

    public ModelCQRNPC(String textureName) {
        this.texture = ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/mob/" + textureName + ".png");
    }

    @Override
    public ResourceLocation getModelResource(T object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_base.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T object) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/human.animation.json");
    }

    @Override
    public ResourceLocation[] getAnimationResourceFallbacks(T animatable) {
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/_generic_humanoid.animation.json")};
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        software.bernie.geckolib.cache.object.GeoBone head = this.getAnimationProcessor().getBone("bipedHead");
        if (head != null) {
            software.bernie.geckolib.model.data.EntityModelData entityData = animationState.getData(software.bernie.geckolib.constant.DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }

        software.bernie.geckolib.cache.object.GeoBone rightArm = this.getAnimationProcessor().getBone("bipedArmRight");
        software.bernie.geckolib.cache.object.GeoBone leftArm = this.getAnimationProcessor().getBone("bipedArmLeft");

        float limbSwing = animationState.getLimbSwing();
        float limbSwingAmount = animationState.getLimbSwingAmount();

        float rightArmX = net.minecraft.util.Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        float leftArmX = net.minecraft.util.Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;

        if (rightArm != null) {
            rightArm.setRotX(rightArm.getInitialSnapshot().getRotX() + rightArmX);
        }
        if (leftArm != null) {
            leftArm.setRotX(leftArm.getInitialSnapshot().getRotX() + leftArmX);
        }
    }
}
