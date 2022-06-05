package team.cqr.cqrepoured.client.render.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRArmorModels;
import team.cqr.cqrepoured.item.armor.ItemCrown;

public class LayerCrownRenderer<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends BipedArmorLayer<T, M, A> {

	private static final ResourceLocation CROWN_TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/models/armor/king_crown_layer_1.png");

	@SuppressWarnings("unchecked")
	public LayerCrownRenderer(IEntityRenderer<T, M> renderer) {
		super(renderer, null, (A) CQRArmorModels.CROWN);
	}

	@Override
	public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount,
			float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		if (ItemCrown.hasCrown(pLivingEntity.getItemBySlot(EquipmentSlotType.HEAD))) {
			this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlotType.HEAD, pPackedLight, this.getArmorModel(EquipmentSlotType.HEAD));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected A getArmorModelHook(T entity, ItemStack itemStack, EquipmentSlotType slot, A model) {
		return (A) CQRArmorModels.CROWN;
	}

	@Override
	public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlotType slot, String type) {
		return CROWN_TEXTURE;
	}

}
