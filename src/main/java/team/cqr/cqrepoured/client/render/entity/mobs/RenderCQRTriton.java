package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.inventory.EquipmentSlotType;
import team.cqr.cqrepoured.client.model.entity.mobs.ModelCQRTriton;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.entity.mobs.EntityCQRTriton;

public class RenderCQRTriton extends RenderCQREntity<EntityCQRTriton> {

	public RenderCQRTriton(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRTriton(), 0.5F, "mob/triton", 1.0D, 1.0D);
	}

	@Override
	public void setupRightLegOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
		modelRenderer.showModel = false;
	}

	@Override
	public void setupLeftLegOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
		modelRenderer.showModel = false;
	}

}
