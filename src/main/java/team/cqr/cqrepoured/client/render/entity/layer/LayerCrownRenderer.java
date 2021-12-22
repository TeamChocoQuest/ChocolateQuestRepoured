package team.cqr.cqrepoured.client.render.entity.layer;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.armor.ModelCrown;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.armor.ItemCrown;

public class LayerCrownRenderer extends LayerBipedArmor {

	protected static final ResourceLocation CROWN_TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/models/armor/king_crown_layer_1.png");

	public LayerCrownRenderer(RenderLivingBase<?> rendererIn) {
		super(rendererIn);
	}

	@Override
	protected void initArmor() {
		this.modelLeggings = new ModelCrown(0.5F);
		this.modelArmor = new ModelCrown(1.0F);
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		// First. Check if we should render at all, if not => don't render!
		if (ItemCrown.hasCrown(entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.HEAD))) {
			super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		}
		//Crown is not attached => you don't need to do anything!
	}

	@Override
	protected ModelBiped getArmorModelHook(EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, ModelBiped model) {
		if (slot != EntityEquipmentSlot.HEAD) {
			return model;
		}
		if (ItemCrown.hasCrown(itemStack)) {
			return CQRItems.KING_CROWN.getArmorModel(entity, itemStack, slot, model);
		}
		return model;
	}

	@Override
	public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type) {
		if (slot == EntityEquipmentSlot.HEAD && ItemCrown.hasCrown(stack)) {
			return CROWN_TEXTURE;
		}
		return super.getArmorResource(entity, stack, slot, type);
	}

}
