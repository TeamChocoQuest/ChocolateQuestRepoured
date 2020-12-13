package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.layers.LayerCQRNecromancerBoneShield;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRNecromancer;

public class RenderCQRNecromancer extends RenderCQRMage<EntityCQRNecromancer> {

	public RenderCQRNecromancer(RenderManager rendermanagerIn, ModelBiped model, String entityName) {
		super(rendermanagerIn, model, entityName);
		this.addLayer(new LayerCQRNecromancerBoneShield(this));
	}

}
