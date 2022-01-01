package team.cqr.cqrepoured.client.render.entity.boss;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.boss.ModelGiantTortoiseGecko;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;

public class RenderCQRGiantTortoiseGecko extends RenderCQREntityGeo<EntityCQRGiantTortoise> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/boss/giant_tortoise.png");
	private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(CQRMain.MODID, "geo/giant_tortoise.geo.json");

	public RenderCQRGiantTortoiseGecko(EntityRendererManager renderManager) {
		super(renderManager, new ModelGiantTortoiseGecko(MODEL_RESLOC, TEXTURE, "boss/giant_tortoise"));
	}

	// we do not hold items, so we can ignore this
	@Override
	protected ItemStack getHeldItemForBone(String boneName, EntityCQRGiantTortoise currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(ItemStack item, String boneName, EntityCQRGiantTortoise currentEntity) {
		// Unused
	}

	@Override
	protected void postRenderItem(ItemStack item, String boneName, EntityCQRGiantTortoise currentEntity) {
		// Unused
	}

	@Nullable
	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRGiantTortoise currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(BlockState block, String boneName, EntityCQRGiantTortoise currentEntity) {
		// Unused
	}

	@Override
	protected void postRenderBlock(BlockState block, String boneName, EntityCQRGiantTortoise currentEntity) {
		// Unused
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityCQRGiantTortoise currentEntity) {
		return null;
	}

	@Override
	protected float getDeathMaxRotation(EntityCQRGiantTortoise entityLivingBaseIn) {
		return 0;
	}

	@Override
	protected TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
		return TransformType.NONE;
	}

}
