package team.cqr.cqrepoured.client.render.entity.layer.equipment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layer.AbstractLayerCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRItems;

@SideOnly(Side.CLIENT)
public class LayerCQREntityPotion<R extends RenderCQREntity<E>, E extends AbstractEntityCQR> extends AbstractLayerCQR<R, E> {

	private static final ItemStack STACK = new ItemStack(CQRItems.POTION_HEALING);

	public LayerCQREntityPotion(R renderer) {
		super(renderer);
	}

	@Override
	public void doRenderLayer(E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
			float scale) {
		if (entity.getHealingPotions() <= 0) {
			return;
		}
		if (!(this.renderer.getMainModel() instanceof ModelBiped)) {
			return;
		}

		ModelBiped model = (ModelBiped) this.renderer.getMainModel();
		ModelRenderer body = model.bipedBody;

		if (body.cubeList.isEmpty()) {
			return;
		}

		ModelBox box = body.cubeList.get(0);

		if (box == null) {
			return;
		}

		GlStateManager.pushMatrix();

		if (entity.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}
		GlStateManager.translate(body.offsetX + body.rotationPointX * 0.0625F, body.offsetY + body.rotationPointY * 0.0625F,
				body.offsetZ + body.rotationPointZ * 0.0625F);
		GlStateManager.rotate((float) Math.toDegrees(body.rotateAngleX), 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate((float) Math.toDegrees(body.rotateAngleY), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float) Math.toDegrees(body.rotateAngleZ), 0.0F, 0.0F, 1.0F);
		GlStateManager.translate(box.posX1 * 0.0625F, box.posY1 * 0.0625F, box.posZ1 * 0.0625F);
		float f = 0.0F;
		if (!entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()) {
			f = -1.0F;
		} else if (!entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty()) {
			f = -0.5F;
		}
		GlStateManager.translate(f * 0.0625F - 0.0125F, (box.posY2 - box.posY1) * 0.0625F, (box.posZ2 - box.posZ1) * 0.0625F * 0.5F);
		this.renderer.setupPotionOffsets(null);
		GlStateManager.scale(0.4F, 0.4F, 0.4F);
		GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		Minecraft.getMinecraft().getItemRenderer().renderItem(entity, STACK, ItemCameraTransforms.TransformType.NONE);

		GlStateManager.popMatrix();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
