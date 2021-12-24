package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layer.LayerBossDeath;
import team.cqr.cqrepoured.entity.boss.EntityCQRWalkerKing;

public class RenderCQRWalkerKing extends RenderCQREntity<EntityCQRWalkerKing> {

	public RenderCQRWalkerKing(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "boss/walker_king");

		this.addLayer(new LayerBossDeath<>(this, 191, 0, 255));
	}

	@Override
	protected float getDeathMaxRotation(EntityCQRWalkerKing entityLivingBaseIn) {
		return 0;
	}

}
