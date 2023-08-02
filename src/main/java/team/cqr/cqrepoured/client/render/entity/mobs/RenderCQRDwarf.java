package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRDwarfGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRDwarf;

public class RenderCQRDwarf extends RenderCQRBipedBaseGeo<EntityCQRDwarf> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/dwarf_0.png");

	public RenderCQRDwarf(EntityRendererProvider.Context renderManager) {
		super(renderManager, new ModelCQRDwarfGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/dwarf"), 0.9F, 0.65F);
	}

}
