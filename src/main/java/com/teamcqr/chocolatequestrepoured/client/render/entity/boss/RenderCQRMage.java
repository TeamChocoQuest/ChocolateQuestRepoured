package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelMageHidden;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.AbstractEntityCQRMageBase;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRMage extends RenderCQREntity<AbstractEntityCQRMageBase> {
	
	public static final ResourceLocation TEXTURES_HIDDEN = new ResourceLocation((Reference.MODID + ":textures/entity/boss/mages_black.png"));
	public static final ResourceLocation TEXTURES_REVEALED = new ResourceLocation((Reference.MODID + ":textures/entity/boss/mages.png"));
	
	private static ModelBiped MODEL_IDENTITY_HIDDEN;
	private ModelBiped MODEL_IDENTITY;

	public RenderCQRMage(RenderManager rendermanagerIn, ModelBiped model, String entityName) {
		super(rendermanagerIn, model, 0.5F, entityName, 1D, 1D);
		RenderCQRMage.MODEL_IDENTITY_HIDDEN = new ModelMageHidden(0F);
		this.MODEL_IDENTITY = model;
	}
	
	@Override
	public void doRender(AbstractEntityCQRMageBase entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		if(entity.isIdentityHidden()) {
			this.mainModel = MODEL_IDENTITY_HIDDEN;
		} else {
			this.mainModel = MODEL_IDENTITY;
		}
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(AbstractEntityCQRMageBase entity) {
		if(entity.isIdentityHidden()) {
			return TEXTURES_HIDDEN;
		}
		return TEXTURES_REVEALED;
	}

}
