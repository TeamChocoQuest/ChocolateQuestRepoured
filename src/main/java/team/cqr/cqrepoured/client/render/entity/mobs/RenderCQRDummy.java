package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQRDummyGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRDummy;

public class RenderCQRDummy extends RenderCQRBipedBaseGeo<EntityCQRDummy> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/dummy.png");

	public RenderCQRDummy(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRDummyGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/dummy"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRDummy currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRDummy currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRDummy currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(ItemStack item, String boneName, EntityCQRDummy currentEntity) {
		
	}

	@Override
	protected void preRenderBlock(BlockState block, String boneName, EntityCQRDummy currentEntity) {
		
	}

	@Override
	protected void postRenderItem(ItemStack item, String boneName, EntityCQRDummy currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(BlockState block, String boneName, EntityCQRDummy currentEntity) {
		
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityCQRDummy currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRDummy currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRDummy currentEntity, IBone bone) {
		
	}

}
