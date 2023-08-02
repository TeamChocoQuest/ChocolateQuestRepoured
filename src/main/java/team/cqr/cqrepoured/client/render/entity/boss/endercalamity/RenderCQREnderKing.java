package team.cqr.cqrepoured.client.render.entity.boss.endercalamity;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQREndermanGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderKing;

@OnlyIn(Dist.CLIENT)
public class RenderCQREnderKing extends RenderCQRBipedBaseGeo<EntityCQREnderKing> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/enderman.png");

	public RenderCQREnderKing(Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQREndermanGeo<EntityCQREnderKing>(CQRMain.prefix("geo/entity/biped_enderman.geo.json"), TEXTURE, "mob/enderman"));

		this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
	}

	@Override
	public float getWidthScale(EntityCQREnderKing entity) {
		float superVal = super.getWidthScale(entity);
		if (entity.isWide()) {
			superVal *= 2;
		}
		return superVal;
	}

}
