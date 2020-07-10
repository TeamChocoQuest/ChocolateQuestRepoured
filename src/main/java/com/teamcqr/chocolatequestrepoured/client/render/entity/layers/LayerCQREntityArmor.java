package com.teamcqr.chocolatequestrepoured.client.render.entity.layers;

import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelCustomArmorBase;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class LayerCQREntityArmor extends LayerBipedArmor {

	private final RenderLivingBase<?> renderer;
	private float alpha = 1.0F;
	private float colorR = 1.0F;
	private float colorG = 1.0F;
	private float colorB = 1.0F;
	private boolean skipRenderGlint;

	public LayerCQREntityArmor(RenderLivingBase<?> rendererIn) {
		super(rendererIn);
		this.renderer = rendererIn;
	}

	@Override
	protected void initArmor() {
		this.modelLeggings = new ModelCustomArmorBase(0.5F, 64, 32);
		this.modelArmor = new ModelCustomArmorBase(1.0F, 64, 32);
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
	}

	private void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn) {
		ItemStack itemstack = entityLivingBaseIn.getItemStackFromSlot(slotIn);

		if (itemstack.getItem() instanceof ItemArmor) {
			ItemArmor itemarmor = (ItemArmor) itemstack.getItem();

			if (itemarmor.getEquipmentSlot() == slotIn) {
				ModelBiped model = this.getModelFromSlot(slotIn);
				model = this.getArmorModelHook(entityLivingBaseIn, itemstack, slotIn, model);
				model.setModelAttributes(this.renderer.getMainModel());
				model.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
				this.setModelSlotVisible(model, slotIn);
				this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, null));

				{
					if (itemarmor.hasOverlay(itemstack)) // Allow this for anything, not only cloth
					{
						int i = itemarmor.getColor(itemstack);
						float f = (float) (i >> 16 & 255) / 255.0F;
						float f1 = (float) (i >> 8 & 255) / 255.0F;
						float f2 = (float) (i & 255) / 255.0F;
						GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
						if (model instanceof ModelCustomArmorBase && this.renderer.getMainModel() instanceof ModelBiped) {
							((ModelCustomArmorBase) model).render(entityLivingBaseIn, scale, (RenderCQREntity<?>) this.renderer, this, (ModelBiped) this.renderer.getMainModel(), slotIn);
						} else {
							model.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
						}
						this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, "overlay"));
					}
					{ // Non-colored
						GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
						if (model instanceof ModelCustomArmorBase && this.renderer.getMainModel() instanceof ModelBiped) {
							((ModelCustomArmorBase) model).render(entityLivingBaseIn, scale, (RenderCQREntity<?>) this.renderer, this, (ModelBiped) this.renderer.getMainModel(), slotIn);
						} else {
							model.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
						}
					} // Default
					if (!this.skipRenderGlint && itemstack.hasEffect()) {
						renderEnchantedGlint(this.renderer, entityLivingBaseIn, model, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
					}
				}
			}
		}
	}

}
