package team.cqr.cqrepoured.client.render.entity.mounts;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.model.EndermiteModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.mount.EntityGiantEndermite;

public class RenderGiantEndermite extends MobRenderer<EntityGiantEndermite, EndermiteModel<EntityGiantEndermite>> {

	private static final ResourceLocation ENDERMITE_TEXTURES = new ResourceLocation(CQRMain.MODID, "textures/entity/mounts/giant_endermite.png");

	public RenderGiantEndermite(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new EndermiteModel<EntityGiantEndermite>(), 1.5F);
	}
	
	@Override
	protected void scale(EntityGiantEndermite pLivingEntity, MatrixStack pMatrixStack, float pPartialTickTime) {
		pMatrixStack.scale(2.5F, 2.5F, 2.5F);
	}
	
	@Override
	public ResourceLocation getTextureLocation(EntityGiantEndermite entity) {
		return ENDERMITE_TEXTURES;
	}

}
