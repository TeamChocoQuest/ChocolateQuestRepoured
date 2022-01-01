package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layer.LayerGlowingAreas;
import team.cqr.cqrepoured.entity.mobs.EntityCQREnderman;

@SideOnly(Side.CLIENT)
public class RenderCQREnderman extends RenderCQREntity<EntityCQREnderman> {

	public RenderCQREnderman(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelEnderman(0.0F), 0.5F, "mob/enderman", 1.0D, 1.0D);
		this.addLayer(new LayerGlowingAreas<>(this, this::getEntityTexture));
	}

	@Override
	public void setupRightLegOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
		if (slot == EquipmentSlotType.LEGS) {
			this.applyRotations(modelRenderer);
			GlStateManager.translate(0.0D, 0.525D, 0.0D);
			GlStateManager.scale(1.0D, 2.6D, 1.0D);
			this.resetRotations(modelRenderer);
		} else if (slot == EquipmentSlotType.FEET) {
			this.applyRotations(modelRenderer);
			GlStateManager.translate(0.0D, 1.1D, 0.0D);
			this.resetRotations(modelRenderer);
		}
	}

	@Override
	public void setupLeftLegOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
		if (slot == EquipmentSlotType.LEGS) {
			this.applyRotations(modelRenderer);
			GlStateManager.translate(0.0D, 0.525D, 0.0D);
			GlStateManager.scale(1.0D, 2.6D, 1.0D);
			this.resetRotations(modelRenderer);
		} else if (slot == EquipmentSlotType.FEET) {
			this.applyRotations(modelRenderer);
			GlStateManager.translate(0.0D, 1.1D, 0.0D);
			this.resetRotations(modelRenderer);
		}
	}

}
