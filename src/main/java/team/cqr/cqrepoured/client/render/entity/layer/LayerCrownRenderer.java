package team.cqr.cqrepoured.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.client.render.armor.RenderArmorKingCrown;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.armor.ItemArmorCrown;

public class LayerCrownRenderer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends BipedArmorLayer<T, M, A> {

	private static final ResourceLocation CROWN_TEXTURE = new ResourceLocation(CQRConstants.MODID, "textures/models/armor/king_crown_layer_1.png");

	@SuppressWarnings("unchecked")
	public LayerCrownRenderer(IEntityRenderer<T, M> renderer) {
		super(renderer, null, (A) new RenderArmorKingCrown());
	}
	
	@Override
	public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount,
                       float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		if (ItemArmorCrown.hasCrown(pLivingEntity.getItemBySlot(EquipmentSlot.HEAD))) {
			this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlot.HEAD, pPackedLight, this.getArmorModel(EquipmentSlot.HEAD));
		}
	}

	@Override
	protected A getArmorModelHook(T entity, ItemStack itemStack, EquipmentSlot slot, A model) {
		return (A) CQRItems.KING_CROWN.get().getArmorModel(entity, itemStack, slot, model);
	}

	@Override
	public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, String type) {
		return CROWN_TEXTURE;
	}

}
