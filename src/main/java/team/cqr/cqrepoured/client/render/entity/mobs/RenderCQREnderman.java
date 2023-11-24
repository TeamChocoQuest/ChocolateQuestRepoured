package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import mod.azure.azurelib.renderer.layer.AutoGlowingGeoLayer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQREndermanGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQREnderman;

@OnlyIn(Dist.CLIENT)
public class RenderCQREnderman extends RenderCQRBipedBaseGeo<EntityCQREnderman> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/enderman.png");

	public RenderCQREnderman(Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQREndermanGeo<EntityCQREnderman>(CQRMain.prefix("geo/entity/biped_enderman.geo.json"), TEXTURE, "mob/enderman"));
		
		this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
	}

}
