package team.cqr.cqrepoured.client.render.entity.boss;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.client.model.geo.entity.boss.ModelWalkerKingGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerGlowingAreasGeo;
import team.cqr.cqrepoured.entity.misc.EntityWalkerKingIllusion;

public class RenderCQRWalkerKingIllusion extends RenderCQRBipedBaseGeo<EntityWalkerKingIllusion> {

	public RenderCQRWalkerKingIllusion(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelWalkerKingGeo<>(STANDARD_BIPED_GEO_MODEL, RenderCQRWalkerKing.TEXTURE_WALKER_KING_DEFAULT, "boss/walker_king"));
		
		this.addLayer(new LayerGlowingAreasGeo<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityWalkerKingIllusion currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityWalkerKingIllusion currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityWalkerKingIllusion currentEntity) {
		return null;
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityWalkerKingIllusion currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityWalkerKingIllusion currentEntity, IBone bone) {
		
	}

	@Override
	protected void preRenderBlock(BlockState block, String boneName, EntityWalkerKingIllusion currentEntity) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityWalkerKingIllusion currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderBlock(BlockState block, String boneName, EntityWalkerKingIllusion currentEntity) {
		
	}
}
