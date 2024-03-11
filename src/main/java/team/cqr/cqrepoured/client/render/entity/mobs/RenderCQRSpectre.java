package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.object.Color;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRSpectreGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRSpectre;

public class RenderCQRSpectre extends RenderCQRBipedBaseGeo<EntityCQRSpectre> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/spectre_0.png");
	
	public RenderCQRSpectre(Context renderManager) {
		super(renderManager, new ModelCQRSpectreGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/spectre"));
	}
	
	@Override
	public Color getRenderColor(EntityCQRSpectre animatable, float partialTick, int packedLight) {
		// TODO Auto-generated method stub
		Color sr = super.getRenderColor(animatable, partialTick, packedLight);
		
		return Color.ofRGBA(sr.getRed(), sr.getGreen(), sr.getBlue(), 0.5F);
	}

	/*@Override
	public void doRender(EntityCQRSpectre entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}*/

}
