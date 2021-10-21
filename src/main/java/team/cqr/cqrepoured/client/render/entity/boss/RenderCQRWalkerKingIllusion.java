package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layers.LayerGlowingEyes;
import team.cqr.cqrepoured.objects.entity.misc.EntityWalkerKingIllusion;

public class RenderCQRWalkerKingIllusion extends RenderCQREntity<EntityWalkerKingIllusion> {

	public RenderCQRWalkerKingIllusion(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "boss/walker_king", true);
		this.addLayer(new LayerGlowingEyes<EntityWalkerKingIllusion>(this, this::getEntityTexture));
	}
}
