package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.inventory.EquipmentSlotType;
import team.cqr.cqrepoured.client.model.entity.mobs.ModelCQRIllager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.entity.mobs.EntityCQRIllager;

public class RenderCQRIllager extends RenderCQREntity<EntityCQRIllager> {

	public RenderCQRIllager(EntityRendererManager rendermanager) {
		super(rendermanager, new ModelCQRIllager(), 0.5F, "mob/illager", 0.9375D, 0.9375D);
	}

	@Override
	public void setupHeadOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
		this.applyRotations(modelRenderer);
		GlStateManager.translate(0.0D, -0.125D, 0.0D);
		this.resetRotations(modelRenderer);
	}

	@Override
	public void setupBodyOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
		if (slot == EquipmentSlotType.CHEST) {
			this.applyRotations(modelRenderer);
			GlStateManager.scale(1.0D, 1.0D, 1.25D);
			this.resetRotations(modelRenderer);
		} else if (slot == EquipmentSlotType.LEGS) {
			this.applyRotations(modelRenderer);
			GlStateManager.scale(1.05D, 1.1D, 1.425D);
			this.resetRotations(modelRenderer);
		}
	}

}
