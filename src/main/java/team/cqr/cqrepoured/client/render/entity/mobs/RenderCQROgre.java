package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQROgreGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQROgre;

public class RenderCQROgre extends RenderCQRBipedBaseGeo<EntityCQROgre> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/ogre.png");
	
	public RenderCQROgre(Context renderManager) {
		super(renderManager, new ModelCQROgreGeo(CQRMain.prefix("geo/entity/biped_ogre.geo.json"), TEXTURE, "mob/ogre"));
	}

}
