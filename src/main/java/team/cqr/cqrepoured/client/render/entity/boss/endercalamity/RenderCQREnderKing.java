package team.cqr.cqrepoured.client.render.entity.boss.endercalamity;

import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layers.LayerGlowingEyes;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderKing;
import team.cqr.cqrepoured.util.Reference;

@SideOnly(Side.CLIENT)
public class RenderCQREnderKing extends RenderCQREntity<EntityCQREnderKing> {

	public RenderCQREnderKing(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelEnderman(0.0F), 0.5F, "mob/enderman", 1.0D, 1.0D);
		this.addLayer(new LayerGlowingEyes<EntityCQREnderKing>(this, new ResourceLocation(Reference.MODID, "textures/entity/mob/enderman_eyes.png")));
	}

	@Override
	public void setupRightLegOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
		if (slot == EntityEquipmentSlot.LEGS) {
			this.applyRotations(modelRenderer);
			GlStateManager.translate(0.0D, 0.525D, 0.0D);
			GlStateManager.scale(1.0D, 2.6D, 1.0D);
			this.resetRotations(modelRenderer);
		} else if (slot == EntityEquipmentSlot.FEET) {
			this.applyRotations(modelRenderer);
			GlStateManager.translate(0.0D, 1.1D, 0.0D);
			this.resetRotations(modelRenderer);
		}
	}

	@Override
	public void setupLeftLegOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
		if (slot == EntityEquipmentSlot.LEGS) {
			this.applyRotations(modelRenderer);
			GlStateManager.translate(0.0D, 0.525D, 0.0D);
			GlStateManager.scale(1.0D, 2.6D, 1.0D);
			this.resetRotations(modelRenderer);
		} else if (slot == EntityEquipmentSlot.FEET) {
			this.applyRotations(modelRenderer);
			GlStateManager.translate(0.0D, 1.1D, 0.0D);
			this.resetRotations(modelRenderer);
		}
	}

	@Override
	protected double getWidthScale(EntityCQREnderKing entity) {
		double superVal = super.getWidthScale(entity);
		if (entity.isWide()) {
			superVal *= 2;
		}
		return superVal;
	}

}
