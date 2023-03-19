package team.cqr.cqrepoured.client.render.entity.layer.special;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.model.entity.ModelCQRPirateParrot;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateParrot;

@OnlyIn(Dist.CLIENT)
public class LayerCQRPirateParrotHeldItem extends HeldItemLayer<EntityCQRPirateParrot, ModelCQRPirateParrot> {

	public LayerCQRPirateParrotHeldItem(IEntityRenderer<EntityCQRPirateParrot, ModelCQRPirateParrot> renderCQREntity) {
		super(renderCQREntity);
	}
	
}
