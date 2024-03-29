package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.ModelPirateCaptainGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerPirateCaptainParrot;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateCaptain;

public class RenderCQRPirateCaptain extends RenderCQRBipedBaseGeo<EntityCQRPirateCaptain> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/boss/pirate_captain.png");

	public RenderCQRPirateCaptain(Context rendermanagerIn) {
		super(rendermanagerIn, new ModelPirateCaptainGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "boss/pirate_captain"));
		
		this.addRenderLayer(new LayerPirateCaptainParrot<>(rendermanagerIn, this));
	}

}
