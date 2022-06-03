package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQRTritonGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRTriton;

public class RenderCQRTriton extends RenderCQRBipedBaseGeo<EntityCQRTriton> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/triton_0.png");

	public RenderCQRTriton(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRTritonGeo(CQRMain.prefix("geo/entity/biped_triton.geo.json"), TEXTURE, "mob/triton"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRTriton currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRTriton currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRTriton currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRTriton currentEntity, IBone bone) {
		
	}

	@Override
	protected void preRenderBlock(BlockState block, String boneName, EntityCQRTriton currentEntity) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRTriton currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderBlock(BlockState block, String boneName, EntityCQRTriton currentEntity) {
		
	}

}
