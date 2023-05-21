package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.mage.ModelCQRBoarMageGeo;
import team.cqr.cqrepoured.entity.boss.EntityCQRBoarmage;

public class RenderCQRBoarmage extends RenderCQRMage<EntityCQRBoarmage> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/boss/pig_mage_0.png");

	public RenderCQRBoarmage(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRBoarMageGeo(CQRMain.prefix("geo/entity/boss/mage/biped_mage_boar.geo.json"), TEXTURE, "boss/pig_mage"));
	}

}
