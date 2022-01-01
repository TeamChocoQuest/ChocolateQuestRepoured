package team.cqr.cqrepoured.client.render.entity.layer.equipment;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import team.cqr.cqrepoured.client.model.armor.ModelCustomArmorBase;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;

public class LayerCQREntityArmor extends BipedArmorLayer {

	private final LivingRenderer<?> renderer;
	private float alpha = 1.0F;
	private float colorR = 1.0F;
	private float colorG = 1.0F;
	private float colorB = 1.0F;
	private boolean skipRenderGlint;

	public LayerCQREntityArmor(LivingRenderer<?> rendererIn) {
		super(rendererIn);
		this.renderer = rendererIn;
	}

	@Override
	protected void initArmor() {
		this.modelLeggings = new ModelCustomArmorBase(0.5F, 64, 32);
		this.modelArmor = new ModelCustomArmorBase(1.0F, 64, 32);
	}

	@Override
	public void doRenderLayer(LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EquipmentSlotType.CHEST);
		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EquipmentSlotType.LEGS);
		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EquipmentSlotType.FEET);
		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EquipmentSlotType.HEAD);
	}

	private void renderArmorLayer(LivingEntity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EquipmentSlotType slotIn) {
		ItemStack itemstack = entityLivingBaseIn.getItemStackFromSlot(slotIn);

		if (itemstack.getItem() instanceof ArmorItem) {
			ArmorItem itemarmor = (ArmorItem) itemstack.getItem();

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
						float f = (i >> 16 & 255) / 255.0F;
						float f1 = (i >> 8 & 255) / 255.0F;
						float f2 = (i & 255) / 255.0F;
						GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
						if (model instanceof ModelCustomArmorBase && this.renderer.getMainModel() instanceof ModelBiped) {
							((ModelCustomArmorBase) model).render(entityLivingBaseIn, scale, (RenderCQREntity<?>) this.renderer, (ModelBiped) this.renderer.getMainModel(), slotIn);
						} else {
							model.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
						}
						this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, "overlay"));
					}
					{ // Non-colored
						GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
						if (model instanceof ModelCustomArmorBase && this.renderer.getMainModel() instanceof ModelBiped) {
							((ModelCustomArmorBase) model).render(entityLivingBaseIn, scale, (RenderCQREntity<?>) this.renderer, (ModelBiped) this.renderer.getMainModel(), slotIn);
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
