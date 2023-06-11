package team.cqr.cqrepoured.client.render.entity.boss.netherdragon;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.model.geo.entity.boss.ModelNetherDragonHeadGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerGlowingAreasGeo;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;

public class RenderNetherDragonHead extends RenderCQREntityGeo<EntityCQRNetherDragon> {

	public RenderNetherDragonHead(EntityRendererManager renderManager) {
		super(renderManager, new ModelNetherDragonHeadGeo(ModelNetherDragonHeadGeo.TEXTURE_NORMAL, ModelNetherDragonHeadGeo.MODEL_IDENT_NORMAL, "boss/nether_dragon_head"));
		
		this.addLayer(new LayerGlowingAreasGeo<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER, CQRRenderTypes::emissive));
	}

	@Override
	protected boolean isArmorBone(GeoBone bone) {
		return false;
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityCQRNetherDragon currentEntity) {
		return null;
	}

	@Override
	protected ItemStack getHeldItemForBone(String boneName, EntityCQRNetherDragon currentEntity) {
		return null;
	}

	@Override
	protected TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
		return null;
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRNetherDragon currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRNetherDragon currentEntity, IBone bone) {
		
	}

	@Override
	protected void preRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, EntityCQRNetherDragon currentEntity) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRNetherDragon currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, EntityCQRNetherDragon currentEntity) {
		
	}

}
