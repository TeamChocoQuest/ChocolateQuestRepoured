package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.mage.ModelCQRLichGeo;
import team.cqr.cqrepoured.entity.boss.EntityCQRLich;

public class RenderCQRLich extends RenderCQRMage<EntityCQRLich> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/boss/lich.png");

	public RenderCQRLich(EntityRendererProvider.Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRLichGeo(CQRMain.prefix("geo/entity/boss/mage/biped_mage_lich.geo.json"), TEXTURE, "boss/lich"));
	}

}
