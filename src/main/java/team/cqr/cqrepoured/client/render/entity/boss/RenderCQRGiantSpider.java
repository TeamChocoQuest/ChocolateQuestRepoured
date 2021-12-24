package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.model.entity.boss.ModelGiantSpider;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.entity.boss.EntityCQRGiantSpider;

public class RenderCQRGiantSpider extends RenderCQREntity<EntityCQRGiantSpider> {

	public RenderCQRGiantSpider(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelGiantSpider(), 2.0F, "boss/giant_spider", 1.0D, 1.0D);
	}

	@Override
	protected float getDeathMaxRotation(EntityCQRGiantSpider entityLivingBaseIn) {
		return 180F;
	}

}
