package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layer.LayerGlowingAreas;
import team.cqr.cqrepoured.entity.misc.EntityWalkerKingIllusion;

public class RenderCQRWalkerKingIllusion extends RenderCQREntity<EntityWalkerKingIllusion> {

	public RenderCQRWalkerKingIllusion(RenderManager rendermanagerIn) {
		this.addLayer(new LayerGlowingAreas<>(this, this::getEntityTexture));
		super(rendermanagerIn, "boss/walker_king");
	}

}
