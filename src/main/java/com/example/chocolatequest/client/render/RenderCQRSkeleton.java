package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRSkeletonGeo;
import com.example.chocolatequest.entity.mob.CQSkeletonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRSkeleton extends RenderCQRBipedBaseGeo<CQSkeletonEntity> {
    public RenderCQRSkeleton(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelCQRSkeletonGeo());
    }
}
