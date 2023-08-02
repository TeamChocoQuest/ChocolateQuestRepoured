package team.cqr.cqrepoured.client.render.entity.boss;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.boss.ModelGiantTortoiseGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;

import javax.annotation.Nullable;

public class RenderCQRGiantTortoise extends RenderCQREntityGeo<EntityCQRGiantTortoise> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/boss/giant_tortoise.png");
	private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(CQRMain.MODID, "geo/entity/boss/giant_tortoise.geo.json");

	public RenderCQRGiantTortoise(EntityRendererProvider.Context renderManager) {
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
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRGiantTortoise currentEntity) {
		// Unused
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRGiantTortoise currentEntity) {
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
	protected void preRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRGiantTortoise currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRGiantTortoise currentEntity, IBone bone) {
		
	}

	@Override
	protected boolean isArmorBone(GeoBone bone) {
		return false;
	}

}
