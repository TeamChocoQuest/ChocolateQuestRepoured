package com.example.chocolatequest.client.render.boss;

import com.example.chocolatequest.client.model.ModelCQRMandrilGeo;
import com.example.chocolatequest.entity.boss.EntityCQRMonking;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.example.chocolatequest.client.render.RenderCQRBipedBaseGeo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public class RenderCQRMonking extends RenderCQRBipedBaseGeo<EntityCQRMonking> {
    public RenderCQRMonking(EntityRendererProvider.Context context) {
        // Suppress unchecked cast warning by casting the model directly
        super(context, (software.bernie.geckolib.model.GeoModel<EntityCQRMonking>)(Object)new ModelCQRMandrilGeo());
        this.withScale(2.5F);
    }
}
