package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelNetherDragonBodyPart;
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

	public RenderCQRNetherDragonSegment(RenderManager manager) {
		super(manager);
		this.model = new ModelNetherDragonBodyPart();
	}

	@Override
	public void doRender(EntityCQRNetherDragonSegment entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		
		if(entity.isTailPeek()) {
			//start of the tail
			if(entity.getTailPartIndex() == 3) {
				//GL11.glScalef(0.8f,0.8f,0.8f);
				GlStateManager.scale(0.8F, 0.8F, 1.0F);
				GlStateManager.translate(0, 1.7, 0);
				//Middle tail part
			} else if(entity.getTailPartIndex() == 2) {
				//GL11.glScalef(0.6f,0.6f,0.6f);
				GlStateManager.scale(0.6F, 0.6F, 1.0F);
				GlStateManager.translate(0, 2.0, 0);
				//Tail end
			} else {
				//GL11.glScalef(0.4f,0.4f,0.4f);
				GlStateManager.scale(0.4F, 0.4F, 1.0F);
				GlStateManager.translate(0, 2.6, 0);
			}
		} else {
			GlStateManager.scale(-1.0F, -1.0F, 1.0F);
			//GL11.glScalef(1.0f, 1.0f, 1.0f);
		}

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
