package com.example.chocolatequest.client.model.entity.boss;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.boss.EntityCQRDragon;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

/** GeckoLib bridge for the supplied Blockbench Green Dragon assets. */
public class GreenDragonModel extends GeoModel<EntityCQRDragon> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(
            ChocolateQuestReDone.MODID, "geo/entity/boss/green_dragon.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ChocolateQuestReDone.MODID, "textures/entity/boss/green_dragon.png");
    private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(
            ChocolateQuestReDone.MODID, "animations/entity/boss/green_dragon.animation.json");

    @Override
    public ResourceLocation getModelResource(EntityCQRDragon animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(EntityCQRDragon animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(EntityCQRDragon animatable) {
        return ANIMATIONS;
    }

    @Override
    public void setCustomAnimations(EntityCQRDragon animatable, long instanceId,
                                    AnimationState<EntityCQRDragon> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        // The saddle is rendered separately with Minecraft's pig-saddle
        // texture, never with the dragon's base texture.
        this.getBone("dragonSaddle").ifPresent(bone -> bone.setHidden(true));
    }
}
