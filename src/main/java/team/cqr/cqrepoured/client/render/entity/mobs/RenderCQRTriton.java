package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRTritonGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRTriton;

public class RenderCQRTriton extends RenderCQRBipedBaseGeo<EntityCQRTriton> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/triton_0.png");

	public RenderCQRTriton(Context rendermanagerIn) {
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
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRTriton currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRTriton currentEntity) {
		
	}

	@Override
	protected void preRenderBlock(PoseStack matrixStack, BlockState block, String boneName, EntityCQRTriton currentEntity) {
		
	}

}
