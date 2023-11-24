package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import mod.azure.azurelib.renderer.layer.AutoGlowingGeoLayer;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.ModelWalkerKingGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.misc.EntityWalkerKingIllusion;

public class RenderCQRWalkerKingIllusion extends RenderCQRBipedBaseGeo<EntityWalkerKingIllusion> {

	public RenderCQRWalkerKingIllusion(EntityRendererProvider.Context rendermanagerIn) {
		super(rendermanagerIn, new ModelWalkerKingGeo<>(STANDARD_BIPED_GEO_MODEL, RenderCQRWalkerKing.TEXTURE_WALKER_KING_DEFAULT, "boss/walker_king"));

		this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
	}

}
