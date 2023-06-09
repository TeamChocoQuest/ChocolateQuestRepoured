package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.mage.ModelCQRBoarMageGeo;
import team.cqr.cqrepoured.entity.boss.EntityCQRBoarmage;

public class RenderCQRBoarmage extends RenderCQRMage<EntityCQRBoarmage> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/boss/pig_mage_0.png");

	public RenderCQRBoarmage(EntityRendererProvider.Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRBoarMageGeo(CQRMain.prefix("geo/entity/boss/mage/biped_mage_boar.geo.json"), TEXTURE, "boss/pig_mage"));
	}

}
