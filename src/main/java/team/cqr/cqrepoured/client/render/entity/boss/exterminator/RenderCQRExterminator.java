package team.cqr.cqrepoured.client.render.entity.boss.exterminator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.boss.ModelExterminator;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerGlowingAreasGeo;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.item.gun.ItemFlamethrower;

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
			//move left or right (from the entity's POV, positive: Right), move up or down the arm, move above (negative) or under the arm (positive)
			GlStateManager.translate(0.0, 0.0, -0.25);
			//Since the golem is massive we need to scale it up a bit
			
			//Standard code from LayerHeldItem
			GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			if(!(item.getItem() instanceof ItemFlamethrower)) {
				GlStateManager.rotate(100, 1, 0, 0);
				GlStateManager.translate(0, -0.3, -0.2);
				GlStateManager.scale(1.25, 1.25, 1.25);
			} else {
				//Different scale cause flamethrower is very small
				GlStateManager.scale(1.5, 1.5, 1.5);
			}
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
