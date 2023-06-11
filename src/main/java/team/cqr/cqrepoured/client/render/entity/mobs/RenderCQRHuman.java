package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRHumanGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRHuman;

public class RenderCQRHuman extends RenderCQRBipedBaseGeo<EntityCQRHuman> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/human_0.png");
	
	public RenderCQRHuman(EntityRendererProvider.Context rendermanagerIn) {
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
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRHuman currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRHuman currentEntity) {
		
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRHuman currentEntity, IBone bone) {
		
	}

}
