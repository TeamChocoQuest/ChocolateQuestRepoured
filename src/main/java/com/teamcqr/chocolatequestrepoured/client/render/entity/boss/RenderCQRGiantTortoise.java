package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRGiantTortoise extends RenderLiving<EntityCQRGiantTortoise> {
	
	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/giant_tortoise.png");
	
	private ModelGiantTortoise model = new ModelGiantTortoise();
	private int animState = 0;
	private boolean mouthIsOpen = false;

	public RenderCQRGiantTortoise(RenderManager rendermanagerIn, ModelGiantTortoise modelbaseIn, float shadowsizeIn) {
		super(rendermanagerIn, modelbaseIn, shadowsizeIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRGiantTortoise entity) {
		return TEXTURE;
	}
	
	@Override
	public void doRender(EntityCQRGiantTortoise entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		//DONE: Rotate move around z axis when the mouth is open
		
		if(animState < 11 && entity.isMouthOpen() && !mouthIsOpen) {
			float angle = (animState) * 3.375F;
			this.model.mouth.rotateAngleZ = new Float(Math.toRadians(angle));
			
			animState++;
			if(animState == 11) {
				mouthIsOpen = true;
				animState = 0;
			}
		}
		
		else if(animState < 11 && !entity.isMouthOpen() && mouthIsOpen) {
			//this.Mouth_Bottom.rotateAngleZ = new Float(Math.toRadians(33.75D));
			float angle = (10 - animState) * 3.375F;
			this.model.mouth.rotateAngleZ = new Float(Math.toRadians(angle));
			
			animState++;
			if(animState == 11) {
				mouthIsOpen = false;
				animState = 0;
			}
		}
		else {
			
		}
		
	}

}
