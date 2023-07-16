package team.cqr.cqrepoured.client.render.entity.layer.special;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.model.entity.ModelCQRPirateParrot;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateParrot;

@OnlyIn(Dist.CLIENT)
public class LayerCQRPirateParrotHeldItem extends ItemInHandLayer<EntityCQRPirateParrot, ModelCQRPirateParrot> {

	public LayerCQRPirateParrotHeldItem(RenderLayerParent<EntityCQRPirateParrot, ModelCQRPirateParrot> pRenderer, ItemInHandRenderer pItemInHandRenderer) {
		super(pRenderer, pItemInHandRenderer);
	}
	
}
