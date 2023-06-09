package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRDummyGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRDummy;

public class RenderCQRDummy extends RenderCQRBipedBaseGeo<EntityCQRDummy> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/dummy.png");

	public RenderCQRDummy(EntityRendererProvider.Context rendermanagerIn) {
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
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRDummy currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRDummy currentEntity) {
		
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRDummy currentEntity, IBone bone) {
		
	}

}
