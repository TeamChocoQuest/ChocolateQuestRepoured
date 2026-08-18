package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public abstract class AbstractModelGeoCQRBase<T extends AbstractEntityCQR> extends GeoModel<T> {

    private final ResourceLocation modelLocation;
    private final ResourceLocation[] textureVariants;
    private final ResourceLocation animationLocation;

    public AbstractModelGeoCQRBase(ResourceLocation modelLocation, String textureName, ResourceLocation animationLocation, int textureCount) {
        this.modelLocation = modelLocation;
        this.animationLocation = animationLocation;
        
        this.textureVariants = new ResourceLocation[textureCount];
        for (int i = 0; i < textureCount; i++) {
            this.textureVariants[i] = ResourceLocation.fromNamespaceAndPath(
                ChocolateQuestReDone.MODID,
                "textures/entity/mob/" + textureName + (textureCount > 1 ? "_" + i : "") + ".png"
            );
        }
    }

    @Override
    public ResourceLocation getModelResource(T object) {
        return this.modelLocation;
    }

    @Override
    public ResourceLocation getTextureResource(T object) {
        if (this.textureVariants.length == 1) {
            return this.textureVariants[0];
        }

        return this.textureVariants[Math.floorMod(object.getTextureIndex(), this.textureVariants.length)];
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return this.animationLocation;
    }
}
