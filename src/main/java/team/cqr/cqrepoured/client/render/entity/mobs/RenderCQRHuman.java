package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQRHumanGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRHuman;

public class RenderCQRHuman extends RenderCQRBipedBaseGeo<EntityCQRHuman> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/human_0.png");
	
	public RenderCQRHuman(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRHumanGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/human"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRHuman currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRHuman currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRHuman currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(ItemStack item, String boneName, EntityCQRHuman currentEntity) {
		
	}

	@Override
	protected void preRenderBlock(BlockState block, String boneName, EntityCQRHuman currentEntity) {
		
	}

	@Override
	protected void postRenderItem(ItemStack item, String boneName, EntityCQRHuman currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(BlockState block, String boneName, EntityCQRHuman currentEntity) {
		
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityCQRHuman currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRHuman currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRHuman currentEntity, IBone bone) {
		
	}

}
