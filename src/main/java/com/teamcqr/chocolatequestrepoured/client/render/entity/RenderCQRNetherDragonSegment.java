package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragonSegment;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRNetherDragonSegment extends Render<EntityCQRNetherDragonSegment> {
	
	//TODO: Add the tail peek
	
	public static final ResourceLocation TEXTURES_NORMAL = new ResourceLocation((Reference.MODID + ":textures/entity/boss/nether_dragon.png"));

	private final ModelBase model;

	public RenderCQRNetherDragonSegment(RenderManager manager, ModelBase model) {
		super(manager);
		this.model = model;
	}

	@Override
	public void doRender(EntityCQRNetherDragonSegment entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);

		float yawDiff = entity.rotationYaw - entity.prevRotationYaw;
		if (yawDiff > 180) {
			yawDiff -= 360;
		} else if (yawDiff < -180) {
			yawDiff += 360;
		}
		float yaw = entity.prevRotationYaw + yawDiff * partialTicks;

		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(entity.rotationPitch, 1.0F, 0.0F, 0.0F);
		this.bindTexture(TEXTURES_NORMAL);
		this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRNetherDragonSegment entity) {
		return TEXTURES_NORMAL;
	}

}
