package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layers.LayerGlowingEyes;
import team.cqr.cqrepoured.objects.entity.misc.EntityWalkerKingIllusion;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQRWalkerKingIllusion extends RenderCQREntity<EntityWalkerKingIllusion> {

	public RenderCQRWalkerKingIllusion(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "boss/walker_king", true);
		this.addLayer(new LayerGlowingEyes<EntityWalkerKingIllusion>(this, new ResourceLocation(Reference.MODID, "textures/entity/boss/walker_king_eyes.png")));
	}
}
