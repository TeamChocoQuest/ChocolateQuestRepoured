package team.cqr.cqrepoured.client.render.entity.boss.exterminator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.ModelExterminatorGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.client.render.entity.StandardBipedBones;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerGlowingAreasGeo;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;

public class RenderCQRExterminator extends RenderCQREntityGeo<EntityCQRExterminator> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/boss/exterminator.png");
	private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(CQRMain.MODID, "geo/entity/boss/exterminator.geo.json");

	public RenderCQRExterminator(EntityRendererProvider.Context renderManager) {
		super(renderManager, new ModelExterminatorGeo(MODEL_RESLOC, TEXTURE, "boss/exterminator"));
		
		this.addLayer(new LayerGlowingAreasGeo<EntityCQRExterminator>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
	}

	public static final String HAND_IDENT_LEFT = "item_left_hand";

	@Override
	protected ItemStack getHeldItemForBone(String boneName, EntityCQRExterminator currentEntity) {
		if (boneName.equalsIgnoreCase(HAND_IDENT_LEFT)) {
			return currentEntity.getItemInHand(InteractionHand.MAIN_HAND);
		}
		return null;
	}

	@Override
	protected float getDeathMaxRotation(EntityCQRExterminator entityLivingBaseIn) {
		return 0.0F;
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRExterminator currentEntity) {
		return null;
	}

	/*@Override
	protected void preRenderItem(ItemStack item, String boneName, EntityCQRExterminator currentEntity) {
		if (boneName.equalsIgnoreCase(HAND_IDENT_LEFT)) {
			//TODO: Figure out values
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
	}*/

	@Override
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRExterminator currentEntity) {
		// Unused
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRExterminator currentEntity) {
		// Unused
	}

	@Override
	protected TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
		if (boneName.equalsIgnoreCase(HAND_IDENT_LEFT)) {
			return TransformType.THIRD_PERSON_RIGHT_HAND;
		}
		return TransformType.NONE;
	}

	@Override
	protected void preRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRExterminator currentEntity, IBone bone) {
		if(boneName != null && boneName.equals(HAND_IDENT_LEFT)) {
			matrixStack.scale(1.5F, 1.5F, 1.5F);
		}
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRExterminator currentEntity, IBone bone) {
		
	}

	@Override
	public RenderType getRenderType(EntityCQRExterminator animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		return RenderType.entityCutoutNoCull(textureLocation);
	}
	
	@Override
	protected boolean isArmorBone(GeoBone bone) {
		return false;
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityCQRExterminator currentEntity) {
		if(boneName.equalsIgnoreCase(StandardBipedBones.CAPE_BONE) && currentEntity.hasCape()) {
			return currentEntity.getResourceLocationOfCape();
		}
		return null;
	}

}
