package team.cqr.cqrepoured.client.render.entity.boss.exterminator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.boss.ModelExterminator;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerGlowingAreasGeo;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;

public class RenderCQRExterminator extends RenderCQREntityGeo<EntityCQRExterminator> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/boss/exterminator.png");
	private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(CQRMain.MODID, "geo/exterminator.geo.json");

	public RenderCQRExterminator(RenderManager renderManager) {
		super(renderManager, new ModelExterminator(MODEL_RESLOC, TEXTURE, "boss/exterminator"));
		
		this.addLayer(new LayerGlowingAreasGeo<EntityCQRExterminator>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
	}

	public static final String HAND_IDENT_LEFT = "item_left_hand";

	@Override
	protected ItemStack getHeldItemForBone(String boneName, EntityCQRExterminator currentEntity) {
		if (boneName.equalsIgnoreCase(HAND_IDENT_LEFT)) {
			return currentEntity.getHeldItem(EnumHand.MAIN_HAND);
		}
		return null;
	}

	@Override
	protected float getDeathMaxRotation(EntityCQRExterminator entityLivingBaseIn) {
		return 0.0F;
	}

	@Override
	protected IBlockState getHeldBlockForBone(String boneName, EntityCQRExterminator currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(ItemStack item, String boneName, EntityCQRExterminator currentEntity) {
		if (boneName.equalsIgnoreCase(HAND_IDENT_LEFT)) {
			GlStateManager.translate(0, 0.35, -0.55);

			GlStateManager.rotate(180, 0, 0, 1);
		}
	}

	@Override
	protected void preRenderBlock(IBlockState block, String boneName, EntityCQRExterminator currentEntity) {
		// Unused
	}

	@Override
	protected void postRenderItem(ItemStack item, String boneName, EntityCQRExterminator currentEntity) {
		// Unused
	}

	@Override
	protected void postRenderBlock(IBlockState block, String boneName, EntityCQRExterminator currentEntity) {
		// Unused
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityCQRExterminator currentEntity) {
		// Unused
		return null;
	}

	@Override
	protected TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
		if (boneName.equalsIgnoreCase(HAND_IDENT_LEFT)) {
			return TransformType.THIRD_PERSON_LEFT_HAND;
		}
		return TransformType.NONE;
	}

}
