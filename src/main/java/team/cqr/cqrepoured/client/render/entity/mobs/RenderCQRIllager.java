package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRIllagerGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRIllager;

public class RenderCQRIllager extends RenderCQRBipedBaseGeo<EntityCQRIllager> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/illager_0.png");
	
	public RenderCQRIllager(EntityRendererProvider.Context rendermanager) {
		super(rendermanager, new ModelCQRIllagerGeo(CQRMain.prefix("geo/entity/biped_illager.geo.json"), TEXTURE, "mob/illager"));
	}

}
