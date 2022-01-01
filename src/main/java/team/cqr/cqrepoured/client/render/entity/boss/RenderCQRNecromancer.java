package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import team.cqr.cqrepoured.client.render.entity.layer.special.LayerCQRNecromancerBoneShield;
import team.cqr.cqrepoured.entity.boss.EntityCQRNecromancer;

public class RenderCQRNecromancer extends RenderCQRMage<EntityCQRNecromancer> {

	public RenderCQRNecromancer(EntityRendererManager rendermanagerIn, ModelBiped model, String entityName) {
		super(rendermanagerIn, model, entityName);
		this.addLayer(new LayerCQRNecromancerBoneShield(this));
	}

}
