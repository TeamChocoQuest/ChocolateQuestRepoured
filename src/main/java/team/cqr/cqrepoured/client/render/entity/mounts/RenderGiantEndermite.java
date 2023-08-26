package team.cqr.cqrepoured.client.render.entity.mounts;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EndermiteModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.entity.mount.EntityGiantEndermite;

public class RenderGiantEndermite extends MobRenderer<EntityGiantEndermite, EndermiteModel<EntityGiantEndermite>> {

	private static final ResourceLocation ENDERMITE_TEXTURES = new ResourceLocation(CQRConstants.MODID, "textures/entity/mounts/giant_endermite.png");

	public RenderGiantEndermite(Context rendermanagerIn) {
		super(rendermanagerIn, new EndermiteModel<EntityGiantEndermite>(), 1.5F);
	}
	
	@Override
	protected void scale(EntityGiantEndermite pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
		pMatrixStack.scale(2.5F, 2.5F, 2.5F);
	}
	
	@Override
	public ResourceLocation getTextureLocation(EntityGiantEndermite entity) {
		return ENDERMITE_TEXTURES;
	}

}
