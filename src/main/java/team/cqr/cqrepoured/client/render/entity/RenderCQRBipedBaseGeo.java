package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.model.GeoModel;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRItems;

public abstract class RenderCQRBipedBaseGeo<T extends AbstractEntityCQR & GeoEntity> extends RenderCQREntityGeo<T> {
	
	protected final static ResourceLocation STANDARD_BIPED_GEO_MODEL = CQRMain.prefix("geo/entity/biped_base.geo.json");

	public RenderCQRBipedBaseGeo(Context renderManager, GeoModel<T> modelProvider) {
		this(renderManager, modelProvider, 1.0F, 1.0F);
	}
	
	protected RenderCQRBipedBaseGeo(Context renderManager, GeoModel<T> modelProvider, final float widthScale, final float heightScale) {
		super(renderManager, modelProvider, widthScale, heightScale, widthScale / 2);
	}

	@Override
	public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		if(bone.getName().equalsIgnoreCase(StandardBipedBones.CAPE_BONE)) {
			bone.setHidden(isReRender || !animatable.hasCape());
			bone.setChildrenHidden(false);
		}
		
		super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	@Override
	protected ResourceLocation getTextureOverrideForBone(GeoBone bone, T currentEntity, float partialTick) {
		if(bone.getName().equalsIgnoreCase(StandardBipedBones.CAPE_BONE) && currentEntity.hasCape()) {
			return currentEntity.getResourceLocationOfCape();
		}
		return super.getTextureOverrideForBone(bone, animatable, partialTick);
	}
	
	public static final ItemStack HEALING_POTION_FOR_DISPLAY = new ItemStack(CQRItems.POTION_HEALING.get());
	
	
	@Override
	public RenderType getRenderType(T animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityCutoutNoCull(texture);
	}

}
