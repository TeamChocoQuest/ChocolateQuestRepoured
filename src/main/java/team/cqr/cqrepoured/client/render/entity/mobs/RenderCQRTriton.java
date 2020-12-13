package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.inventory.EntityEquipmentSlot;
import team.cqr.cqrepoured.client.models.entities.mobs.ModelCQRTriton;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRTriton;

public class RenderCQRTriton extends RenderCQREntity<EntityCQRTriton> {

	public RenderCQRTriton(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRTriton(), 0.5F, "mob/triton", 1.0D, 1.0D);
	}

	@Override
	public void setupRightLegOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
		modelRenderer.showModel = false;
	}

	@Override
	public void setupLeftLegOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
		modelRenderer.showModel = false;
	}

}
