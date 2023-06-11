package team.cqr.cqrepoured.client.render.entity.boss;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.boss.ModelGiantTortoiseGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;

public class RenderCQRGiantTortoiseGecko extends RenderCQREntityGeo<EntityCQRGiantTortoise> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/boss/giant_tortoise.png");
	private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(CQRMain.MODID, "geo/entity/boss/giant_tortoise.geo.json");

	public RenderCQRGiantTortoiseGecko(EntityRendererManager renderManager) {
		super(renderManager, new ModelGiantTortoiseGeo(MODEL_RESLOC, TEXTURE, "boss/giant_tortoise"));
	}

	// we do not hold items, so we can ignore this
	@Override
	protected ItemStack getHeldItemForBone(String boneName, EntityCQRGiantTortoise currentEntity) {
		return null;
	}

	@Nullable
	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRGiantTortoise currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQRGiantTortoise currentEntity) {
		// Unused
	}

	@Override
	protected void postRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQRGiantTortoise currentEntity) {
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

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRGiantTortoise currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRGiantTortoise currentEntity, IBone bone) {
		
	}

	@Override
	protected boolean isArmorBone(GeoBone bone) {
		return false;
	}

}
