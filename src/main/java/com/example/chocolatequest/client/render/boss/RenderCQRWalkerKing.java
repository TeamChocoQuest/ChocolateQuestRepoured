package com.example.chocolatequest.client.render.boss;

import com.example.chocolatequest.client.model.ModelCQRWalkerGeo;
import com.example.chocolatequest.entity.boss.EntityCQRWalkerKing;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.example.chocolatequest.client.render.RenderCQRBipedBaseGeo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public class RenderCQRWalkerKing extends RenderCQRBipedBaseGeo<EntityCQRWalkerKing> {
    public RenderCQRWalkerKing(EntityRendererProvider.Context context) {
        // Suppress unchecked cast warning by casting the model directly
        super(context, (software.bernie.geckolib.model.GeoModel<EntityCQRWalkerKing>)(Object)new ModelCQRWalkerGeo());
        this.withScale(1.0F);
    }
}
