package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.inventory.EntityEquipmentSlot;
import team.cqr.cqrepoured.client.models.entities.mobs.ModelCQROgre;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQROgre;

public class RenderCQROgre extends RenderCQREntity<EntityCQROgre> {

	public RenderCQROgre(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQROgre(), 0.5F, "mob/ogre", 1.0D, 1.0D);
	}

	@Override
	public void setupHeadOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
		this.applyRotations(modelRenderer);
		GlStateManager.translate(0.0D, 0.0D, 0.0D);
		this.resetRotations(modelRenderer);
	}

	@Override
	public void setupBodyOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
		if (slot == EntityEquipmentSlot.CHEST) {
			this.applyRotations(modelRenderer);
			GlStateManager.scale(1.0D, 1.0D, 1.25D);
			this.resetRotations(modelRenderer);
		} else if (slot == EntityEquipmentSlot.LEGS) {
			this.applyRotations(modelRenderer);
			GlStateManager.scale(1.1D, 1.0D, 1.45D);
			this.resetRotations(modelRenderer);
		}
	}

}
