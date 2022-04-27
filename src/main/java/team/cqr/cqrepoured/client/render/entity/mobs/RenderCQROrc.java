package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQROrcGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQROrc;

public class RenderCQROrc extends RenderCQRBipedBaseGeo<EntityCQROrc> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/orc_0.png");

	public RenderCQROrc(EntityRendererManager renderManager) {
		super(renderManager, new ModelCQROrcGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/orc"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQROrc currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQROrc currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQROrc currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(BlockState block, String boneName, EntityCQROrc currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(BlockState block, String boneName, EntityCQROrc currentEntity) {
		
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityCQROrc currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQROrc currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQROrc currentEntity, IBone bone) {
		
	}


}
