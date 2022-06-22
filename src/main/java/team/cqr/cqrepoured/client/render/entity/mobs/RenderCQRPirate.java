package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQRPirateGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRPirate;

public class RenderCQRPirate extends RenderCQRBipedBaseGeo<EntityCQRPirate> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/priate_0.png");

	public RenderCQRPirate(EntityRendererManager renderManager) {
		super(renderManager, new ModelCQRPirateGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/pirate"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRPirate currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRPirate currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRPirate currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQRPirate currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQRPirate currentEntity) {
		
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRPirate currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRPirate currentEntity, IBone bone) {
		
	}


}
