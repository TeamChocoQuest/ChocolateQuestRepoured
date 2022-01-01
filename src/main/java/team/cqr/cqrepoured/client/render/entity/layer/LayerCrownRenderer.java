package team.cqr.cqrepoured.client.render.entity.layer;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.armor.ModelCrown;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.armor.ItemCrown;

public class LayerCrownRenderer extends BipedArmorLayer {

	protected static final ResourceLocation CROWN_TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/models/armor/king_crown_layer_1.png");

	public LayerCrownRenderer(LivingRenderer<?> rendererIn) {
		super(rendererIn);
	}

	@Override
	protected void initArmor() {
		this.modelLeggings = new ModelCrown(0.5F);
		this.modelArmor = new ModelCrown(1.0F);
	}

	@Override
	public void doRenderLayer(LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		// First. Check if we should render at all, if not => don't render!
		if (ItemCrown.hasCrown(entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.HEAD))) {
			super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		}
		//Crown is not attached => you don't need to do anything!
	}

	@Override
	protected ModelBiped getArmorModelHook(LivingEntity entity, ItemStack itemStack, EquipmentSlotType slot, ModelBiped model) {
		if (slot != EquipmentSlotType.HEAD) {
			return model;
		}
		if (ItemCrown.hasCrown(itemStack)) {
			return CQRItems.KING_CROWN.getArmorModel(entity, itemStack, slot, model);
		}
		return model;
	}

	@Override
	public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlotType slot, String type) {
		if (slot == EquipmentSlotType.HEAD && ItemCrown.hasCrown(stack)) {
			return CROWN_TEXTURE;
		}
		return super.getArmorResource(entity, stack, slot, type);
	}

}
