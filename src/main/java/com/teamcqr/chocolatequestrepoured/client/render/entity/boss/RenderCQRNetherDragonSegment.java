package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelNetherDragonBodyParts;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts.EntityCQRNetherDragonSegment;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRNetherDragonSegment extends Render<EntityCQRNetherDragonSegment> {

	// TODO: Add the tail peek

	public static final ResourceLocation TEXTURES_NORMAL = new ResourceLocation((Reference.MODID + ":textures/entity/boss/nether_dragon.png"));

	private final ModelBase modelNormal;
	private final ModelBase modelTail;
	private final ModelBase modelTailTip;

	public RenderCQRNetherDragonSegment(RenderManager manager) {
		super(manager);
		this.modelNormal = new ModelNetherDragonBodyParts.ModelNetherDragonBodyPart();
		this.modelTail = new ModelNetherDragonBodyParts.ModelNetherDragonBodyTailStart();
		this.modelTailTip = new ModelNetherDragonBodyParts.ModelNetherDragonBodyTailTip();
	}

	@Override
	public void doRender(EntityCQRNetherDragonSegment entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);

		ModelBase model = this.modelNormal;
		if (entity.getPartIndex() <= 1) {
			if (entity.getPartIndex() <= 0) {
				model = this.modelTailTip;
			} else {
				model = this.modelTail;
			}
		}
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		// GL11.glScalef(1.0f, 1.0f, 1.0f);

		float yawDiff = entity.rotationYaw - entity.prevRotationYaw;
		if (yawDiff > 180) {
			yawDiff -= 360;
		} else if (yawDiff < -180) {
			yawDiff += 360;
		}
		float yaw = entity.prevRotationYaw + yawDiff * partialTicks;

		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(entity.rotationPitch, 1.0F, 0.0F, 0.0F);
		this.bindTexture(this.getEntityTexture(entity));
		model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}
	
	@Override
	public boolean shouldRender(EntityCQRNetherDragonSegment livingEntity, ICamera camera, double camX, double camY, double camZ) {
		return super.shouldRender(livingEntity, camera, camX, camY, camZ) && !livingEntity.isDead;
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityCQRNetherDragonSegment entity) {
		return TEXTURES_NORMAL;
	}

}
