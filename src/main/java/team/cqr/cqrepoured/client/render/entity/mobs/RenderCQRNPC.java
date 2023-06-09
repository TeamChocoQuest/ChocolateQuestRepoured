package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRNPCGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRNPC;

public class RenderCQRNPC extends RenderCQRBipedBaseGeo<EntityCQRNPC> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/lunk.png");
	
	public RenderCQRNPC(Context renderManager) {
		super(renderManager, new ModelCQRNPCGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/lunk"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRNPC currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRNPC currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRNPC currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRNPC currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRNPC currentEntity) {
		
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRNPC currentEntity, IBone bone) {
		
	}

}
