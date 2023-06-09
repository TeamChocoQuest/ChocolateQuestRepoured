package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRBoarmanGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRBoarman;

public class RenderCQRBoarman extends RenderCQRBipedBaseGeo<EntityCQRBoarman> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/boarman_0.png");

	public RenderCQRBoarman(EntityRendererProvider.Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRBoarmanGeo(CQRMain.prefix("geo/entity/biped_boarman.geo.json"), TEXTURE, "mob/boarman"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRBoarman currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRBoarman currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRBoarman currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRBoarman currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRBoarman currentEntity) {
		
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRBoarman currentEntity, IBone bone) {
		
	}

}
