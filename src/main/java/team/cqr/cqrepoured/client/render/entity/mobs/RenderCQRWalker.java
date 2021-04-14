package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layers.LayerGlowingEyes;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRWalker;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQRWalker extends RenderCQREntity<EntityCQRWalker> {

	public RenderCQRWalker(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/walker", true);
		this.addLayer(new LayerGlowingEyes<EntityCQRWalker>(this, new ResourceLocation(Reference.MODID, "textures/entity/mob/walker_eyes.png")));
	}

}
