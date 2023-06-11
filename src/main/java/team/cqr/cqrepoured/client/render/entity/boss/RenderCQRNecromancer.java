package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.mage.ModelCQRNecromancerGeo;
import team.cqr.cqrepoured.entity.boss.EntityCQRNecromancer;

public class RenderCQRNecromancer extends RenderCQRMage<EntityCQRNecromancer> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/boss/necromancer.png");

	public RenderCQRNecromancer(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRNecromancerGeo(CQRMain.prefix("geo/entity/boss/mage/biped_mage_necromancer.geo.json"), TEXTURE, "boss/necromancer"));
	}

}
