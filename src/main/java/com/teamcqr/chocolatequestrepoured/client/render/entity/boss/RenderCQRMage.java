package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.AbstractEntityCQRMageBase;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRMage extends RenderCQREntity<AbstractEntityCQRMageBase> {
	
	public static final ResourceLocation TEXTURES_HIDDEN = new ResourceLocation((Reference.MODID + ":textures/entity/boss/mages_black.png"));
	public static final ResourceLocation TEXTURES_REVEALED = new ResourceLocation((Reference.MODID + ":textures/entity/boss/mages.png"));

	public RenderCQRMage(RenderManager rendermanagerIn, ModelBiped model, String entityName) {
		super(rendermanagerIn, model, 0.5F, entityName, 1D, 1D);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(AbstractEntityCQRMageBase entity) {
		if(entity.isIdentityHidden()) {
			return TEXTURES_HIDDEN;
		}
		return TEXTURES_REVEALED;
	}

}
